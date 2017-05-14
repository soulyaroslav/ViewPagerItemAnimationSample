package steelkiwi.com.customtestpager.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import steelkiwi.com.customtestpager.R;

/**
 * Created by yaroslav on 4/25/17.
 */

public class ShadowImageView extends ImageView {
    private static final float DEFAULT_SHADOW_RADIUS = 8.0f;

    private float borderWidth;
    private int canvasSize;
    private float shadowRadius;
    private int shadowColor;

    private Bitmap image;
    private Drawable drawable;
    private Paint paint;
    private Paint whiteBackgroundPaint;

    public ShadowImageView(final Context context) {
        this(context, null);
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        shadowColor = ContextCompat.getColor(getContext(), R.color.transparent);
        // Init paint
        paint = new Paint();
        paint.setAntiAlias(true);

        whiteBackgroundPaint = new Paint();
        whiteBackgroundPaint.setAntiAlias(true);
        whiteBackgroundPaint.setColor(Color.WHITE);
    }

    public void addShadow() {
        if (shadowRadius == 0)
            shadowRadius = DEFAULT_SHADOW_RADIUS;
        drawShadow(shadowRadius, shadowColor);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Load the bitmap
        loadBitmap();
        if (!isInEditMode()) {
            canvasSize = canvas.getWidth();
            if (canvas.getHeight() < canvasSize) {
                canvasSize = canvas.getHeight();
            }
        }
        addShadow();
        int circleCenter = (int) (canvasSize - (borderWidth * 2)) / 2;
        // Draw White Background
        canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter - (shadowRadius + shadowRadius / 2), whiteBackgroundPaint);
        // Draw ImageView
        if(image != null) {
            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter - (shadowRadius + shadowRadius / 2), paint);
        }
    }

    private void loadBitmap() {
        if (this.drawable == getDrawable())
            return;

        this.drawable = getDrawable();
        this.image = drawableToBitmap(this.drawable);
        updateShader();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasSize = w;
        if (h < canvasSize)
            canvasSize = h;
        if (image != null)
            updateShader();
    }

    private void drawShadow(float shadowRadius, int shadowColor) {
        this.shadowRadius = shadowRadius;
        this.shadowColor = shadowColor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, whiteBackgroundPaint);
        }
        whiteBackgroundPaint.setShadowLayer(shadowRadius, 0.0f, shadowRadius, shadowColor);
    }

    private void updateShader() {
        if (image == null)
            return;
        image = cropBitmap(image);
        BitmapShader shader = new BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setScale((float) canvasSize / (float) image.getWidth(), (float) canvasSize / (float) image.getHeight());
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
    }

    private Bitmap cropBitmap(Bitmap bitmap) {
        Bitmap bmp;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            bmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    bitmap.getHeight(), bitmap.getHeight());
        } else {
            bmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(), bitmap.getWidth());
        }
        return bmp;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();

        if (!(intrinsicWidth > 0 && intrinsicHeight > 0))
            return null;

        try {
            // Create Bitmap object out of the drawable
            Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Simply return null of failed bitmap creations
            Log.e(getClass().toString(), "Encountered OutOfMemoryError while generating bitmap!");
            return null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        /*int imageSize = (width < height) ? width : height;
        setMeasuredDimension(imageSize, imageSize);*/
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            // The parent has determined an exact size for the child.
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize;
        } else {
            // The parent has not imposed any constraint on the child.
            result = canvasSize;
        }

        return result;
    }

    private int measureHeight(int measureSpecHeight) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = canvasSize;
        }
        return (result + 2);
    }
}
