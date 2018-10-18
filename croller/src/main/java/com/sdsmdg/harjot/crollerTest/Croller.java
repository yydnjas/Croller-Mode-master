package com.sdsmdg.harjot.crollerTest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sdsmdg.harjot.croller.R;
import com.sdsmdg.harjot.crollerTest.utilities.Log4j;
import com.sdsmdg.harjot.crollerTest.utilities.Utils;

public class Croller extends View {
    RectF oval;
    private float midx, midy;
    private Paint textPaint, circlePaint, circlePaint2, circlePaint3, linePaint;
    private float currdeg = 0, drawDeg = 3, downdeg = 0;

    private boolean isContinuous = false;

    private int backCircleColor = Color.parseColor("#222222");
    private int mainCircleColor = Color.parseColor("#000000");
    private int indicatorColor = Color.parseColor("#EDEDED");
    private int progressPrimaryColor = Color.parseColor("#FFA036");
    private int[] progressSecondaryColor =
        new int[]{Color.parseColor("#6DE952"), Color.parseColor("#5589D2")};

    private float progressPrimaryCircleSize = -1;
    private float progressSecondaryCircleSize = -1;

    private float progressPrimaryStrokeWidth = 25;
    private float progressSecondaryStrokeWidth = 10;

    private float mainCircleRadius = -1;
    private float backCircleRadius = -1;
    private float progressRadius = -1;

    private int max = 25;
    private int min = 1;

    private float indicatorWidth = 7;

    private String label = "";
    private int labelSize = 40;
    private int labelColor = Color.WHITE;

    private int startOffset = 30;
    private int startOffset2 = 0;
    private int sweepAngle = -1;

    private boolean isAntiClockwise = false;

    private boolean startEventSent = false;
    private int secondaryPossion = 3;
    private int possion = 0;
    private int mStartPossion = -1;

    private onProgressChangedListener mProgressChangeListener;
    private OnCrollerChangeListener mCrollerChangeListener;
    private int[] modeArray = new int[]
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1
            , 0, 1, 0, 1, 0, 1, 0, 1, 0, 1
            , 0, 1, 0, 1, 0, 1, 0, 1, 0, 1
            , 0, 1, 0, 1, 0, 1, 0, 1, 0, 1
            , 0, 1, 0, 1, 0, 1, 0, 1};

    public interface onProgressChangedListener {
        void onProgressChanged(int progress);
    }

    public void setOnProgressChangedListener(onProgressChangedListener mProgressChangeListener) {
        this.mProgressChangeListener = mProgressChangeListener;
    }

    public void setOnCrollerChangeListener(OnCrollerChangeListener mCrollerChangeListener) {
        this.mCrollerChangeListener = mCrollerChangeListener;
    }

    public Croller(Context context) {
        super(context);
        init();
    }

    public Croller(Context context, AttributeSet attrs) {
        super(context, attrs);
        initXMLAttrs(context, attrs);
        init();
    }

    public Croller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXMLAttrs(context, attrs);
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(labelColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(labelSize);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(getProgressSecondaryColor());
        circlePaint.setStrokeWidth(progressSecondaryStrokeWidth);
        circlePaint.setStyle(Paint.Style.FILL);

        circlePaint2 = new Paint();
        circlePaint2.setAntiAlias(true);
        circlePaint2.setColor(progressPrimaryColor);
        circlePaint2.setStrokeWidth(progressPrimaryStrokeWidth);
        circlePaint2.setStyle(Paint.Style.FILL);

        circlePaint3 = new Paint();
        circlePaint3.setAntiAlias(true);
        circlePaint3.setColor(progressPrimaryColor);
        circlePaint3.setStrokeWidth(progressPrimaryStrokeWidth);
        circlePaint3.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(indicatorColor);
        linePaint.setStrokeWidth(indicatorWidth);

        oval = new RectF();

    }

    private void initXMLAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Croller);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.Croller_progress) {
                setProgress(a.getInt(attr, 1));
            } else if (attr == R.styleable.Croller_label) {
                setLabel(a.getString(attr));
            } else if (attr == R.styleable.Croller_back_circle_color) {
                setBackCircleColor(a.getColor(attr, Color.parseColor("#222222")));
            } else if (attr == R.styleable.Croller_main_circle_color) {
                setMainCircleColor(a.getColor(attr, Color.parseColor("#000000")));
            } else if (attr == R.styleable.Croller_indicator_color) {
                setIndicatorColor(a.getColor(attr, Color.parseColor("#FFA036")));
            } else if (attr == R.styleable.Croller_progress_primary_color) {
                setProgressPrimaryColor(a.getColor(attr, Color.parseColor("#FFA036")));
            } else if (attr == R.styleable.Croller_progress_secondary_color) {
                setProgressSecondaryColor(a.getColor(attr, Color.parseColor("#111111")));
            } else if (attr == R.styleable.Croller_label_size) {
                setLabelSize(a.getInteger(attr, 40));
            } else if (attr == R.styleable.Croller_label_color) {
                setLabelColor(a.getColor(attr, Color.WHITE));
            } else if (attr == R.styleable.Croller_indicator_width) {
                setIndicatorWidth(a.getFloat(attr, 7));
            } else if (attr == R.styleable.Croller_is_continuous) {
                setIsContinuous(a.getBoolean(attr, false));
            } else if (attr == R.styleable.Croller_progress_primary_circle_size) {
                setProgressPrimaryCircleSize(a.getFloat(attr, -1));
            } else if (attr == R.styleable.Croller_progress_secondary_circle_size) {
                setProgressSecondaryCircleSize(a.getFloat(attr, -1));
            } else if (attr == R.styleable.Croller_progress_primary_stroke_width) {
                setProgressPrimaryStrokeWidth(a.getFloat(attr, 25));
            } else if (attr == R.styleable.Croller_progress_secondary_stroke_width) {
                setProgressSecondaryStrokeWidth(a.getFloat(attr, 10));
            } else if (attr == R.styleable.Croller_sweep_angle) {
                setSweepAngle(a.getInt(attr, -1));
            } else if (attr == R.styleable.Croller_start_offset) {
                setStartOffset(a.getInt(attr, 30));
            } else if (attr == R.styleable.Croller_max) {
                setMax(a.getInt(attr, 25));
            } else if (attr == R.styleable.Croller_min) {
                setMin(a.getInt(attr, 1));
                drawDeg = min + 2;
            } else if (attr == R.styleable.Croller_main_circle_radius) {
                setMainCircleRadius(a.getFloat(attr, -1));
            } else if (attr == R.styleable.Croller_back_circle_radius) {
                setBackCircleRadius(a.getFloat(attr, -1));
            } else if (attr == R.styleable.Croller_progress_radius) {
                setProgressRadius(a.getFloat(attr, -1));
            } else if (attr == R.styleable.Croller_anticlockwise) {
                setAntiClockwise(a.getBoolean(attr, false));
            }
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minWidth = (int) Utils.convertDpToPixel(160, getContext());
        int minHeight = (int) Utils.convertDpToPixel(160, getContext());

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(minWidth, widthSize);
        } else {
            // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
            // If width is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make width equal to height
            width = heightSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(minHeight, heightSize);
        } else {
            // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
            // If height is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make height equal to width
            height = widthSize;
        }

        if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            width = minWidth;
            height = minHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        midx = getWidth() / 2;
        midy = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.possion = (int) (drawDeg - 2);
        mStartPossion = possion;

        if (mProgressChangeListener != null) {
            mProgressChangeListener.onProgressChanged(possion);
        }

        if (mCrollerChangeListener != null) {
            mCrollerChangeListener.onProgressChanged(this,possion);
        }
        if (!isContinuous) {

            startOffset2 = startOffset - 15;
            circlePaint.setColor(getProgressSecondaryColor());
            circlePaint2.setColor(getProgressSecondaryColor());
            linePaint.setStrokeWidth(indicatorWidth);
            linePaint.setColor(indicatorColor);
            textPaint.setColor(labelColor);
            textPaint.setTextSize(labelSize);

            int radius = (int) (Math.min(midx, midy) * ((float) 14.5 / 16));

            if (sweepAngle == -1) {
                sweepAngle = 360 - (2 * startOffset2);
            }

            if (mainCircleRadius == -1) {
                mainCircleRadius = radius * ((float) 11 / 15);
            }
            if (backCircleRadius == -1) {
                backCircleRadius = radius * ((float) 13 / 15);
            }
            if (progressRadius == -1) {
                progressRadius = radius;
            }

            float x, y;
            float deg2 = Math.max(3, drawDeg);
            float deg3 = Math.min(drawDeg, max + 2);
            for (int i = (int) (deg2); i < max + 3; i++) {
                float tmp = ((float) startOffset2 / 360) + ((float) sweepAngle / 360) * (float) i / (max + 5);

                if (isAntiClockwise) {
                    tmp = 1.0f - tmp;
                }
                x = midx + (float) (progressRadius * Math.sin(2 * Math.PI * (1.0 - tmp)));
                y = midy + (float) (progressRadius * Math.cos(2 * Math.PI * (1.0 - tmp)));
                secondaryPossion = i;
                circlePaint.setColor(getProgressSecondaryColor());
                if (progressSecondaryCircleSize == -1) {
                    Log4j.e("isContinuous:" + "deg2" + "i:" + i);
                    canvas.drawCircle(x, y, ((float) radius / 15 * ((float) 20 / max) * ((float) sweepAngle / 270)), circlePaint);
//                    canvas.drawCircle(x, y, (progressRadius / 15 * ((float) 20 / max) * ((float) sweepAngle / 270)), circlePaint3);

                } else {
                    Log4j.e("isContinuous:" + "deg2" + "progressSecondaryCircleSize != -1");
                    canvas.drawCircle(x, y, progressSecondaryCircleSize, circlePaint);
                }
            }
            for (int i = 3; i <= deg3; i++) {
                float tmp = ((float) startOffset2 / 360) + ((float) sweepAngle / 360) * (float) i / (max + 5);

                if (isAntiClockwise) {
                    tmp = 1.0f - tmp;
                }
                secondaryPossion = i;
                circlePaint.setColor(getProgressSecondaryColor());
                x = midx + (float) (progressRadius * Math.sin(2 * Math.PI * (1.0 - tmp)));
                y = midy + (float) (progressRadius * Math.cos(2 * Math.PI * (1.0 - tmp)));
                if (progressPrimaryCircleSize == -1) {

                    Log4j.e("deg3:" + secondaryPossion + "progressPrimaryCircleSize == -1");
                    canvas.drawCircle(x, y, ((float) radius / 15 * ((float) 20 / max) * ((float) sweepAngle / 270)), circlePaint);
//                    canvas.drawCircle(x, y, ((float) radius / 15 * ((float) 20 / max) * ((float) sweepAngle / 270)), circlePaint2);
                    canvas.drawCircle(x, y, (progressRadius / 15 * ((float) 20 / max) * ((float) sweepAngle / 270)), circlePaint3);

                } else {
                    Log4j.e("deg3"+"progressPrimaryCircleSize != -1");
//                    canvas.drawCircle(x, y, progressPrimaryCircleSize, circlePaint2);

                }
            }

            float tmp2 = ((float) startOffset2 / 360) + ((float) sweepAngle / 360) * drawDeg / (max + 5);

            if (isAntiClockwise) {
                tmp2 = 1.0f - tmp2;
            }

            float x1 = midx + (float) (radius * ((float) 2 / 5) * Math.sin(2 * Math.PI * (1.0 - tmp2)));
            float y1 = midy + (float) (radius * ((float) 2 / 5) * Math.cos(2 * Math.PI * (1.0 - tmp2)));
            float x2 = midx + (float) (radius * ((float) 3 / 5) * Math.sin(2 * Math.PI * (1.0 - tmp2)));
            float y2 = midy + (float) (radius * ((float) 3 / 5) * Math.cos(2 * Math.PI * (1.0 - tmp2)));

            circlePaint.setColor(backCircleColor);
            canvas.drawCircle(midx, midy, backCircleRadius, circlePaint);
            circlePaint.setColor(mainCircleColor);
            canvas.drawCircle(midx, midy, mainCircleRadius, circlePaint);
            canvas.drawText(label, midx, midy + (float) (radius * 1.1) - textPaint.getFontMetrics().descent, textPaint);
            canvas.drawLine(x1, y1, x2, y2, linePaint);

        } else {
            int radius = (int) (Math.min(midx, midy) * ((float) 14.5 / 16));

            if (sweepAngle == -1) {
                sweepAngle = 360 - (2 * startOffset);
            }

            if (mainCircleRadius == -1) {
                mainCircleRadius = radius * ((float) 11 / 15);
            }
            if (backCircleRadius == -1) {
                backCircleRadius = radius * ((float) 13 / 15);
            }
            if (progressRadius == -1) {
                progressRadius = radius;
            }

            circlePaint.setColor(getProgressSecondaryColor());
            circlePaint.setStrokeWidth(progressSecondaryStrokeWidth);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint2.setColor(progressPrimaryColor);
            circlePaint2.setStrokeWidth(progressPrimaryStrokeWidth);
            circlePaint2.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(indicatorWidth);
            linePaint.setColor(indicatorColor);
            textPaint.setColor(labelColor);
            textPaint.setTextSize(labelSize);

            float deg3 = Math.min(drawDeg, max + 2);

            oval.set(midx - progressRadius, midy - progressRadius, midx + progressRadius, midy + progressRadius);

            canvas.drawArc(oval, (float) 90 + startOffset, (float) sweepAngle, false, circlePaint);
            if (isAntiClockwise) {
                canvas.drawArc(oval, (float) 90 - startOffset, -1 * ((deg3 - 2) * ((float) sweepAngle / max)), false, circlePaint2);
            } else {
                canvas.drawArc(oval, (float) 90 + startOffset, ((deg3 - 2) * ((float) sweepAngle / max)), false, circlePaint2);
            }

            float tmp2 = ((float) startOffset / 360) + (((float) sweepAngle / 360) * ((drawDeg - 2) / (max)));

            if (isAntiClockwise) {
                tmp2 = 1.0f - tmp2;
            }

            float x1 = midx + (float) (radius * ((float) 2 / 5) * Math.sin(2 * Math.PI * (1.0 - tmp2)));
            float y1 = midy + (float) (radius * ((float) 2 / 5) * Math.cos(2 * Math.PI * (1.0 - tmp2)));
            float x2 = midx + (float) (radius * ((float) 3 / 5) * Math.sin(2 * Math.PI * (1.0 - tmp2)));
            float y2 = midy + (float) (radius * ((float) 3 / 5) * Math.cos(2 * Math.PI * (1.0 - tmp2)));

            circlePaint.setStyle(Paint.Style.FILL);

            circlePaint.setColor(backCircleColor);
            canvas.drawCircle(midx, midy, backCircleRadius, circlePaint);
            circlePaint.setColor(mainCircleColor);
            canvas.drawCircle(midx, midy, mainCircleRadius, circlePaint);
            canvas.drawText(label, midx, midy + (float) (radius * 1.1) - textPaint.getFontMetrics().descent, textPaint);
            canvas.drawLine(x1, y1, x2, y2, linePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (Utils.getDistance(e.getX(), e.getY(), midx, midy) >
            Math.max(mainCircleRadius, Math.max(backCircleRadius, progressRadius))) {
            if (startEventSent && mCrollerChangeListener != null) {
                mCrollerChangeListener.onStopTrackingTouch(this);
                Log4j.e("Stop==>" + "mpossion:" + possion);
                startEventSent = false;
            }
            return super.onTouchEvent(e);
        }

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            float dx = e.getX() - midx;
            float dy = e.getY() - midy;
            downdeg = (float) ((Math.atan2(dy, dx) * 180) / Math.PI);
            downdeg -= 90;
            if (downdeg < 0) {
                downdeg += 360;
            }
            downdeg = (float) Math.floor((downdeg / 360) * (max + 5));

            if (mCrollerChangeListener != null) {
                mCrollerChangeListener.onStartTrackingTouch(this);
                startEventSent = true;
            }
            return true;
        }
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = e.getX() - midx;
            float dy = e.getY() - midy;
            currdeg = (float) ((Math.atan2(dy, dx) * 180) / Math.PI);
            currdeg -= 90;
            if (currdeg < 0) {
                currdeg += 360;
            }
            currdeg = (float) Math.floor((currdeg / 360) * (max + 5));

            if ((currdeg / (max + 4)) > 0.75f && ((downdeg - 0) / (max + 4)) < 0.25f) {
                if (isAntiClockwise) {
                    drawDeg++;
                    if (drawDeg > max + 2) {
                        drawDeg = max + 2;
                    }
                } else {
                    drawDeg--;
                    if (drawDeg < (min + 2)) {
                        drawDeg = (min + 2);
                    }
                }
            } else if ((downdeg / (max + 4)) > 0.75f && ((currdeg - 0) / (max + 4)) < 0.25f) {
                if (isAntiClockwise) {
                    drawDeg--;
                    if (drawDeg < (min + 2)) {
                        drawDeg = (min + 2);
                    }
                } else {
                    drawDeg++;
                    if (drawDeg > max + 2) {
                        drawDeg = max + 2;
                    }
                }
            } else {
                if (isAntiClockwise) {
                    drawDeg -= (currdeg - downdeg);
                } else {
                    drawDeg += (currdeg - downdeg);
                }
                if (drawDeg > max + 2) {
                    drawDeg = max + 2;
                }
                if (drawDeg < (min + 2)) {
                    drawDeg = (min + 2);
                }
            }
            Log4j.e("Move==>" + "mpossion:" + possion);
            downdeg = currdeg;
            invalidate();
            return true;

        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (mCrollerChangeListener != null) {
                mCrollerChangeListener.onStopTrackingTouch(this);
                startEventSent = false;
            }
            return true;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getParent() != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
    }

    public int getProgress() {
        return (int) (drawDeg - 2);
    }

    public void setProgress(int x) {
        drawDeg = x + 2;
        invalidate();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String txt) {
        label = txt;
        invalidate();
    }

    public int getBackCircleColor() {
        return backCircleColor;
    }

    public void setBackCircleColor(int backCircleColor) {
        this.backCircleColor = backCircleColor;
        invalidate();
    }

    public int getMainCircleColor() {
        return mainCircleColor;
    }

    public void setMainCircleColor(int mainCircleColor) {
        this.mainCircleColor = mainCircleColor;
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor ;
    }

    public void setIndicatorColor(int indicatorColor) {
        if (indicatorColor!=0 && indicatorColor!=1){
            this.indicatorColor = Color.parseColor("#EDEDED");
        }else {

        this.indicatorColor = progressSecondaryColor[indicatorColor];
        }
        invalidate();
    }

    public int getProgressPrimaryColor() {
        return progressPrimaryColor;
    }

    public void setProgressPrimaryColor(int progressPrimaryColor) {
        this.progressPrimaryColor = progressPrimaryColor;
        invalidate();
    }

    public int getProgressSecondaryColor() {
        if (modeArray[secondaryPossion - 3] == 0) {
            return progressSecondaryColor[0];
        } else if (modeArray[secondaryPossion - 3] == 1) {
            return progressSecondaryColor[1];
        }
        return progressSecondaryColor[0];
    }

    public void setProgressSecondaryColor(int progressSecondaryColor) {
        this.progressSecondaryColor[0] = progressSecondaryColor;
        invalidate();
    }

    public void setModeArray(int[] modeArray) {
        this.modeArray = modeArray;
        invalidate();
    }






    public int getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(int labelSize) {
        this.labelSize = labelSize;
        invalidate();
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        invalidate();
    }

    public float getIndicatorWidth() {
        return indicatorWidth;
    }

    public void setIndicatorWidth(float indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
        invalidate();
    }

    public boolean isContinuous() {
        return isContinuous;
    }

    public void setIsContinuous(boolean isContinuous) {
        this.isContinuous = isContinuous;
        invalidate();
    }

    public float getProgressPrimaryCircleSize() {
        return progressPrimaryCircleSize;
    }

    public void setProgressPrimaryCircleSize(float progressPrimaryCircleSize) {
        this.progressPrimaryCircleSize = progressPrimaryCircleSize;
        invalidate();
    }

    public float getProgressSecondaryCircleSize() {
        return progressSecondaryCircleSize;
    }

    public void setProgressSecondaryCircleSize(float progressSecondaryCircleSize) {
        this.progressSecondaryCircleSize = progressSecondaryCircleSize;
        invalidate();
    }

    public float getProgressPrimaryStrokeWidth() {
        return progressPrimaryStrokeWidth;
    }

    public void setProgressPrimaryStrokeWidth(float progressPrimaryStrokeWidth) {
        this.progressPrimaryStrokeWidth = progressPrimaryStrokeWidth;
        invalidate();
    }

    public float getProgressSecondaryStrokeWidth() {
        return progressSecondaryStrokeWidth;
    }

    public void setProgressSecondaryStrokeWidth(float progressSecondaryStrokeWidth) {
        this.progressSecondaryStrokeWidth = progressSecondaryStrokeWidth;
        invalidate();
    }

    public int getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(int sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max < min) {
            this.max = min;
        } else {
            this.max = max;
        }
        invalidate();
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        if (min < 0) {
            this.min = 0;
        } else if (min > max) {
            this.min = max;
        } else {
            this.min = min;
        }
        invalidate();
    }

    public float getMainCircleRadius() {
        return mainCircleRadius;
    }

    public void setMainCircleRadius(float mainCircleRadius) {
        this.mainCircleRadius = mainCircleRadius;
        invalidate();
    }

    public float getBackCircleRadius() {
        return backCircleRadius;
    }

    public void setBackCircleRadius(float backCircleRadius) {
        this.backCircleRadius = backCircleRadius;
        invalidate();
    }

    public float getProgressRadius() {
        return progressRadius;
    }

    public void setProgressRadius(float progressRadius) {
        this.progressRadius = progressRadius;
        invalidate();
    }

    public boolean isAntiClockwise() {
        return isAntiClockwise;
    }

    public void setAntiClockwise(boolean antiClockwise) {
        isAntiClockwise = antiClockwise;
        invalidate();
    }
}
