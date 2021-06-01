package ph.easyaf.eaviews.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import ph.easyaf.eaviews.R;

public class DangoSkewerView extends View {

    private static final int START_ANGLE_POINT = -90;

    private boolean hideTail = false;
    private int width = 50, height = 50, value = 0;
    private int strokeColor = R.color.blue10, circleColor = R.color.blue10;
    private int headHeight = 35, circleSize = 50, tailHeight = 5;

    private float circleStrokeWidth = 5f, strokeWidth = 2f;
    private float maxFirstLine = 35f, maxSecondLine = 5f;

    private Paint paint, paintCircle, paintCircleFill;
    private RectF rect, rectFill;

    @SuppressLint("ResourceAsColor")
    public DangoSkewerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DangoSkewerView);
        try {
            hideTail = a.getBoolean(R.styleable.DangoSkewerView_hideTail, false);
            headHeight = a.getDimensionPixelSize(R.styleable.DangoSkewerView_headHeight, 35);
            tailHeight = a.getDimensionPixelSize(R.styleable.DangoSkewerView_tailHeight, 5);
            circleSize = a.getDimensionPixelSize(R.styleable.DangoSkewerView_circleSize, 50);

            strokeWidth = a.getDimension(R.styleable.DangoSkewerView_strokeWidth, 2f);
            strokeColor = a.getColor(R.styleable.DangoSkewerView_strokeColor,
                    getResources().getColor(R.color.blue10));
            circleColor = a.getColor(R.styleable.DangoSkewerView_circleColor,
                    getResources().getColor(R.color.blue10));
            circleStrokeWidth = a.getDimension(R.styleable.DangoSkewerView_circleStrokeWidth, 5f);
            value = a.getInteger(R.styleable.DangoSkewerView_value, 0);
        } finally {
            a.recycle();
        }
        init();
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(strokeColor);

        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setStrokeWidth(circleStrokeWidth);
        paintCircle.setColor(strokeColor);

        paintCircleFill = new Paint();
        paintCircleFill.setAntiAlias(true);
        paintCircleFill.setStyle(Paint.Style.FILL_AND_STROKE);
        paintCircleFill.setColor(circleColor);

        //size 200x200 example
        // strokeWidth, strokeWidth

        maxFirstLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35f,
                getContext().getResources().getDisplayMetrics());
        maxSecondLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f,
                getContext().getResources().getDisplayMetrics());

        float top = (float)headHeight;
        float start = (float)(width / 2) - (float)(circleSize / 2);
        rect = new RectF((start + (circleStrokeWidth - (circleStrokeWidth / 2f))),
                top + circleStrokeWidth - (circleStrokeWidth / 2f),
                start + circleSize - circleStrokeWidth + (circleStrokeWidth / 2f),
                top + circleSize - circleStrokeWidth + (circleStrokeWidth / 2f));

        rectFill = new RectF(start, top, start + circleSize, top + circleSize);
    }

    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        width = xNew;
        height = yNew;

        init();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = circleSize;
        int desiredHeight = headHeight + tailHeight + circleSize;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        // Measure Width
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(desiredWidth, widthSize);
                break;
            default:
                width = desiredWidth;
                break;
        }

        // Measure Height
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(desiredHeight, heightSize);
                break;
            default:
                height = desiredHeight;
                break;
        }

        setMeasuredDimension(width, height);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int valueAcquired = 0, valueToUse = 0;

        if ((value - 40) <= 0) valueToUse = value;
        else valueToUse = 40;

        canvas.drawLine((float)width / 2, 0,
                (float)width / 2, valueToUse / 40f * headHeight, paint);

        valueAcquired += valueToUse;
        if ((value - valueAcquired) > 0) {

            if ((value - valueAcquired - 50) <= 0) valueToUse = value - valueAcquired;
            else valueToUse = 50;

            float halfAngle = valueToUse / 50f * 180, fullAngle = valueToUse / 50f * 360;
            canvas.drawArc(rectFill, 270 - halfAngle,
                    fullAngle, false, paintCircleFill);
            canvas.drawArc(rect, 270 - halfAngle,
                    fullAngle, false, paintCircle);

            //canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paintCircle);
            //canvas.drawArc(rect, START_ANGLE_POINT, -angle, false, paintCircle);

            valueAcquired += valueToUse;
        }

        if (!hideTail) {
            if ((value - valueAcquired) > 0) {
                if ((value - valueAcquired - 10) <= 0) valueToUse = value - valueAcquired;
                else valueToUse = 10;

                canvas.drawLine((float)width / 2f, headHeight + circleSize, (float)width / 2f,
                        headHeight + circleSize + (valueToUse / 10f * tailHeight), paint);
            }
        }
    }

    public boolean isHideTail() { return hideTail; }
    public void setHideTail(boolean hideTail) {
        this.hideTail = hideTail;
        invalidate();
    }

    public int getStrokeColor() { return strokeColor; }
    public void setStrokeColor(int color) {
        paint.setColor(color);
        paintCircle.setColor(color);
        invalidate();
    }

    public int getValue() { return value; }
    public void setValue(int value) {
        this.value = value;
        invalidate();
    }
}
