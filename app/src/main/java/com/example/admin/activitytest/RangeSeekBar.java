package com.example.admin.activitytest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by Admin on 2018/3/27.
 */

public class RangeSeekBar extends View {

    //the progress hint mode
    //defaultMode: show hint when you move the thumb
    //alwaysHide: hide progress hint all the time
    //alwaysShow: show progress hint all the time
    public static final int HINT_MODE_DEFAULT = 0;
    public static final int HINT_MODE_ALWAYS_HIDE = 1;
    public static final int HINT_MODE_TOUCH_ALWAYS_SHOW = 2;
    public static final int HINT_MODE_ALWAYS_SHOW = 3;

    private static final float DEFAULT_RADIUS = 0.5f;

    //default seekbar's padding left and right
    private int DEFAULT_PADDING_LEFT_AND_RIGHT;

    private int defaultPaddingTop;
    //进度提示的背景 The background of the progress
    private int mProgressHintBGId;

    // 按钮的背景   The background of the Drag button
    private int mThumbResId;

    //刻度模式：number根据数字实际比例排列；other 均分排列
    //Scale mode:
    // number according to the actual proportion of the number of arranged;
    // other equally arranged
    private int mCellMode;

    //single是Seekbar模式，range是RangeSeekbar
    //single is Seekbar mode, range is angeSeekbar
    //single = 1; range = 2
    private int mSeekBarMode ;

    //默认为1，当大于1时自动切回刻度模式
    //The default is 1, and when it is greater than 1,
    // it will automatically switch back to the scale mode
    private int cellsCount = 1;

    //刻度与进度条间的间距
    //The spacing between the scale and the progress bar
    private int textPadding;

    //进度提示背景与按钮之间的距离
    //The progress indicates the distance between the background and the button
    private int mHintBGPadding;

    private int mSeekBarHeight;
    private int mThumbSize;

    //两个按钮之间的最小距离
    //The minimum distance between two buttons
    private int reserveCount;

    private int mCursorTextHeight;
    private int mPartLength;
    private int heightNeeded;
    private int lineWidth;

    //进度条圆角
    //radius of progress bar
    private float lineRadius;

    //选择过的进度条颜色
    // the color of the selected progress bar
    private int colorLineSelected;

    //未选则的进度条颜色
    // the color of the unselected progress bar
    private int colorLineEdge;

    //The foreground color of progress bar and thumb button.
    private int colorPrimary;

    //The background color of progress bar and thumb button.
    private int colorSecondary;

    //刻度文字与提示文字的大小
    //Scale text and prompt text size
    private int mTextSize;

    private int lineTop, lineBottom, lineLeft, lineRight;

    //进度提示背景的高度，宽度如果是0的话会自适应调整
    //Progress prompted the background height, width,
    // if it is 0, then adaptively adjust
    private float mHintBGHeight;
    private float mHintBGWith;

    private float offsetValue;
    private float cellsPercent;
    private float reserveValue;
    private float reservePercent;
    private float maxValue, minValue;

    //真实的最大值和最小值
    //True maximum and minimum values
    private int mMin,mMax;
    private int equal;

    private boolean isEnable = true;

    private int mProgressHintMode;

    //刻度上显示的文字
    //The texts displayed on the scale
    private CharSequence[] mTextArray;

    private Bitmap mProgressHintBG;
    private Paint mMainPaint = new Paint();
    private Paint mCursorPaint = new Paint();
    private Paint mProgressPaint;
    private RectF backgroundLineRect = new RectF();
    private RectF foregroundLineRect = new RectF();
    private SeekBar leftSB ;
    private SeekBar rightSB;
    private SeekBar currTouch;

    private OnRangeChangedListener callback;
    private android.widget.SeekBar.OnSeekBarChangeListener mBarChangeListener;


    public RangeSeekBar(Context context) {
        this(context, null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.RangeSeekBar);
        cellsCount = t.getInt(R.styleable.RangeSeekBar_rsb_cells, 1);
        reserveValue = t.getFloat(R.styleable.RangeSeekBar_rsb_reserve, 0);
        mMin = t.getInt(R.styleable.RangeSeekBar_rsb_min, 0);
        mMax = t.getInt(R.styleable.RangeSeekBar_rsb_max, 100);
        equal = t.getInt(R.styleable.RangeSeekBar_rsb_equal,4);
        mThumbResId = t.getResourceId(R.styleable.RangeSeekBar_rsb_thumbResId, 0);
        mProgressHintBGId = t.getResourceId(R.styleable.RangeSeekBar_rsb_progressHintResId, 0);
        colorLineSelected = t.getColor(R.styleable.RangeSeekBar_rsb_lineColorSelected, 0xFF4BD962);
        colorLineEdge = t.getColor(R.styleable.RangeSeekBar_rsb_lineColorEdge, 0xFFD7D7D7);
        colorPrimary = t.getColor(R.styleable.RangeSeekBar_rsb_thumbPrimaryColor, 0);
        colorSecondary = t.getColor(R.styleable.RangeSeekBar_rsb_thumbSecondaryColor, 0);
        mTextArray = t.getTextArray(R.styleable.RangeSeekBar_rsb_markTextArray);
        mProgressHintMode = t.getInt(R.styleable.RangeSeekBar_rsb_progressHintMode,HINT_MODE_DEFAULT);
        textPadding = (int)t.getDimension(R.styleable.RangeSeekBar_rsb_textPadding,dp2px(context,7));
        mTextSize = (int)t.getDimension(R.styleable.RangeSeekBar_rsb_textSize,dp2px(context,12));
        mHintBGHeight = t.getDimension(R.styleable.RangeSeekBar_rsb_hintBGHeight,0);
        mHintBGWith = t.getDimension(R.styleable.RangeSeekBar_rsb_hintBGWith,0);
        mSeekBarHeight = (int)t.getDimension(R.styleable.RangeSeekBar_rsb_seekBarHeight,dp2px(context,2));
        mHintBGPadding = (int)t.getDimension(R.styleable.RangeSeekBar_rsb_hintBGPadding,0);
        mThumbSize = (int)t.getDimension(R.styleable.RangeSeekBar_rsb_thumbSize,dp2px(context,26));
        mCellMode = t.getInt(R.styleable.RangeSeekBar_rsb_cellMode , 0);
        mSeekBarMode = t.getInt(R.styleable.RangeSeekBar_rsb_seekBarMode , 2);
        lineRadius = (int)t.getDimension(R.styleable.RangeSeekBar_rsb_lineRadius,-1);

        if (mSeekBarMode == 2){
            leftSB = new SeekBar(-1);
            rightSB = new SeekBar(1);
        }else {
            leftSB = new SeekBar(-1);
        }

        // if you don't set the mHintBGWith or the mHintBGWith < default value, if will use default value
        if (mHintBGWith == 0) {
            DEFAULT_PADDING_LEFT_AND_RIGHT = dp2px(context, 25);
        }else {
            DEFAULT_PADDING_LEFT_AND_RIGHT = Math.max((int)(mHintBGWith / 2+ dp2px(context, 5)),dp2px(context, 25));
        }

        setRules(mMin, mMax, reserveValue, cellsCount);
        initPaint();
        initBitmap();
        t.recycle();

        defaultPaddingTop = mSeekBarHeight / 2 ;

        if (mProgressHintMode == HINT_MODE_ALWAYS_HIDE  && mTextArray == null){
            mHintBGHeight = mCursorPaint.measureText("国");
        }else {
            mHintBGHeight = mHintBGHeight == 0 ? (mCursorPaint.measureText("国") * 3) : mHintBGHeight;
        }

        //Android 7.0以后，优化了View的绘制，onMeasure和onSizeChanged调用顺序有所变化
        //Android7.0以下：onMeasure--->onSizeChanged--->onMeasure
        //Android7.0以上：onMeasure--->onSizeChanged
        lineTop = (int)mHintBGHeight + mThumbSize/2 -mSeekBarHeight/2 ;
        lineBottom = lineTop + mSeekBarHeight ;
        //default value
        if (lineRadius < 0) lineRadius = (int) ((lineBottom - lineTop) * 0.45f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize  = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        heightNeeded = 2 * lineTop + mSeekBarHeight;

        /**
         * onMeasure传入的widthMeasureSpec和heightMeasureSpec不是一般的尺寸数值，而是将模式和尺寸组合在一起的数值
         * MeasureSpec.EXACTLY 是精确尺寸
         * MeasureSpec.AT_MOST 是最大尺寸
         * MeasureSpec.UNSPECIFIED 是未指定尺寸
         */
        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = MeasureSpec.makeMeasureSpec(heightNeeded, MeasureSpec.AT_MOST);
        } else {
            heightSize = MeasureSpec.makeMeasureSpec(
                    heightNeeded, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //计算进度条的位置，并根据它初始化两个按钮的位置
        // Calculates the position of the progress bar and initializes the positions of
        // the two buttons based on it
        lineLeft = DEFAULT_PADDING_LEFT_AND_RIGHT  + getPaddingLeft();
        lineRight = w - lineLeft - getPaddingRight();
        lineWidth = lineRight - lineLeft;
        backgroundLineRect.set(lineLeft, lineTop, lineRight, lineBottom);

        leftSB.onSizeChanged(lineLeft, lineBottom, mThumbSize, lineWidth, cellsCount > 1, mThumbResId, getContext());
        if (mSeekBarMode == 2) {
            rightSB.onSizeChanged(lineLeft, lineBottom, mThumbSize, lineWidth, cellsCount > 1, mThumbResId, getContext());
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制刻度，并且根据当前位置是否在刻度范围内设置不同的颜色显示
        // Draw the scales, and according to the current position is set within
        // the scale range of different color display
        mPartLength = lineWidth / equal-1;
        for (int a = mMin; a <= mMax; a += (mMax - mMin) / equal)
        {
        if (mCellMode == 1){
            mCursorPaint.setColor(colorLineEdge);
                }else {
                    float x;
                    float[] result = getCurrentRange();
                    if (compareFloat(a,result[0]) != -1 && compareFloat(a,result[1]) != 1 && mSeekBarMode == 2){
                        mCursorPaint.setColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                    }else {
                        mCursorPaint.setColor(colorLineEdge);
                    }
                    //按实际比例显示
                            x = lineLeft + (float)(a-(mMax-mMin)*0.02)* lineWidth / (mMax - mMin);
                            float y = lineTop - textPadding;
                            canvas.drawText(String.valueOf(a), x, y, mCursorPaint);
                        }


                }



        //绘制进度条
        // draw the progress bar
        mMainPaint.setColor(colorLineEdge);

        canvas.drawRoundRect(backgroundLineRect, lineRadius, lineRadius, mMainPaint);
        mMainPaint.setColor(colorLineSelected);

        if (mSeekBarMode == 2) {
            foregroundLineRect.top = lineTop;
            foregroundLineRect.left = leftSB.left + leftSB.widthSize / 2 + leftSB.lineWidth * leftSB.currPercent;
            foregroundLineRect.right = rightSB.left + rightSB.widthSize / 2 + rightSB.lineWidth * rightSB.currPercent;
            foregroundLineRect.bottom = lineBottom;
            canvas.drawRoundRect(foregroundLineRect, lineRadius, lineRadius, mMainPaint);
        }else {
            foregroundLineRect.top = lineTop;
            foregroundLineRect.left = leftSB.left + leftSB.widthSize / 2 ;
            foregroundLineRect.right = leftSB.left + leftSB.widthSize / 2 + leftSB.lineWidth * leftSB.currPercent;
            foregroundLineRect.bottom = lineBottom;
            canvas.drawRoundRect(foregroundLineRect, lineRadius, lineRadius, mMainPaint);
        }
        if (mProgressHintMode == HINT_MODE_ALWAYS_SHOW){
            isShowProgressHint(leftSB, true);
        }
        leftSB.draw(canvas);
        if (mSeekBarMode == 2) {
            if (mProgressHintMode == HINT_MODE_ALWAYS_SHOW){
                isShowProgressHint(rightSB, true);
            }
            rightSB.draw(canvas);
        }
    }

    /**
     * 初始化画笔
     * init the paints
     */
    private void initPaint() {

        mMainPaint.setStyle(Paint.Style.FILL);
        mMainPaint.setColor(colorLineEdge);

        mCursorPaint.setStyle(Paint.Style.FILL);
        mCursorPaint.setColor(colorLineEdge);
        mCursorPaint.setTextSize(mTextSize);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setTypeface(Typeface.DEFAULT);

        //计算文字的高度
        //Calculate the height of the text
        Paint.FontMetrics fm = mCursorPaint.getFontMetrics();
        mCursorTextHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);
    }



    /**
     * 初始化进度提示的背景
     */
    private void initBitmap() {
        if (mProgressHintBGId != 0) {
            mProgressHintBG = BitmapFactory.decodeResource(getResources(), mProgressHintBGId);
        }else {
            mProgressHintBG = BitmapFactory.decodeResource(getResources(), R.drawable.progress_hint_bg);
        }
    }

    //*********************************** SeekBar ***********************************//

    private class SeekBar {
        private int lineWidth;
        private int widthSize, heightSize;
        private int left, right, top, bottom;
        private float currPercent;
        private float material = 0;
        public boolean isShowingHint;
        private boolean isLeft;
        private Bitmap bmp;
        private ValueAnimator anim;
        private RadialGradient shadowGradient;
        private Paint defaultPaint;
        private String mHintText2Draw;
        private Boolean isPrimary = true;


        public SeekBar(int position) {
            if (position < 0){
                isLeft = true;
            }else {
                isLeft = false;
            }
        }

        /**
         * 计算每个按钮的位置和尺寸
         * Calculates the position and size of each button
         *
         * @param x
         * @param y
         * @param hSize
         * @param parentLineWidth
         * @param cellsMode
         * @param bmpResId
         * @param context
         */
        protected void onSizeChanged(int x, int y, int hSize, int parentLineWidth, boolean cellsMode, int bmpResId, Context context) {
            heightSize = hSize;
            widthSize = heightSize ;
            left = x - widthSize / 2;
            right = x + widthSize / 2;
            top = y - heightSize / 2;
            bottom = y + heightSize / 2;

            if (cellsMode) {
                lineWidth = parentLineWidth;
            } else {
                lineWidth = parentLineWidth ;
            }

            if (bmpResId > 0) {
                Bitmap original = drawableToBitmap(getResources().getDrawable(bmpResId));

                if (original != null) {
                    Matrix matrix = new Matrix();
                    float scaleHeight = mThumbSize * 1.0f / original.getHeight();
                    float scaleWidth = scaleHeight;
                    matrix.postScale(scaleWidth, scaleHeight);
                    bmp = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
                }

            } else {
                defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                int radius = (int) (widthSize * DEFAULT_RADIUS);
                int barShadowRadius = (int) (radius * 0.95f);
                int mShadowCenterX = widthSize/2;
                int mShadowCenterY = heightSize/2;
                shadowGradient = new RadialGradient(mShadowCenterX, mShadowCenterY, barShadowRadius, Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
            }
        }

        /**
         * 绘制按钮和提示背景和文字
         * Draw buttons and tips for background and text
         * @param canvas
         */
        protected void draw(Canvas canvas) {
            int offset = (int) (lineWidth * currPercent);
            canvas.save();
            canvas.translate(offset, 0);
            String text2Draw = "";
            int hintW = 0,hintH = 0;
            float[] result = getCurrentRange();

            if (isLeft){
                if (mHintText2Draw == null){
                    text2Draw = (int)result[0] + "";
                }else {
                    text2Draw = mHintText2Draw;
                }

                // if is the start，change the thumb color
                isPrimary = (compareFloat(result[0],mMin) == 0);

            }else {
                if (mHintText2Draw == null){
                    text2Draw = (int)result[1] + "";
                }else {
                    text2Draw = mHintText2Draw;
                }

                isPrimary = (compareFloat(result[1],mMax) == 0);
            }

            hintH = (int)mHintBGHeight;
            hintW =(int)(mHintBGWith == 0 ? (mCursorPaint.measureText(text2Draw)+  DEFAULT_PADDING_LEFT_AND_RIGHT)
                    : mHintBGWith);

            if (hintW < 1.5f * hintH) hintW = (int)(1.5f * hintH);

            if (bmp != null) {
                canvas.drawBitmap(bmp, left,  lineTop + (mSeekBarHeight - bmp.getHeight()) / 2, null);
                if (isShowingHint) {

                    Rect rect = new Rect();
                    rect.left = left - (hintW / 2 - bmp.getWidth() / 2);
                    rect.top = bottom -  hintH - bmp.getHeight();
                    rect.right = rect.left + hintW;
                    rect.bottom = rect.top + hintH;
                    drawNinePath(canvas,mProgressHintBG,rect);
                    mCursorPaint.setColor(Color.WHITE);

                    int x = (int)(left + (bmp.getWidth() / 2) - mCursorPaint.measureText(text2Draw) / 2);
                    int y =  bottom -  hintH - bmp.getHeight() + hintH / 2;
                    canvas.drawText(text2Draw,x,y,mCursorPaint);
                }
            } else {
                canvas.translate(left, 0);
                if (isShowingHint) {
                    Rect rect = new Rect();
                    rect.left = widthSize / 2 - hintW / 2;
                    rect.top = defaultPaddingTop;
                    rect.right = rect.left + hintW;
                    rect.bottom = rect.top + hintH;
                    drawNinePath(canvas,mProgressHintBG,rect);

                    mCursorPaint.setColor(Color.WHITE);

                    int x = (int)(widthSize / 2 - mCursorPaint.measureText(text2Draw) / 2);

                    // TODO: 2017/2/6
                    //这里和背景形状有关，暂时根据本图形状比例计算
                    //Here and the background shape, temporarily based on the shape of this figure ratio calculation
                    int y = hintH / 3   + defaultPaddingTop  + mCursorTextHeight / 2  ;

                    canvas.drawText(text2Draw,x,y,mCursorPaint);
                }
                drawDefault(canvas);
            }

            canvas.restore();
        }

        /**
         * 绘制 9Path
         * @param c
         * @param bmp
         * @param rect
         */
        public void drawNinePath(Canvas c, Bitmap bmp, Rect rect){
            NinePatch patch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
            patch.draw(c, rect);
        }

        /**
         * 如果没有图片资源，则绘制默认按钮
         *
         * If there is no image resource, draw the default button
         * @param canvas
         */
        private void drawDefault(Canvas canvas) {

            int centerX = widthSize/2;
            int centerY = lineBottom - mSeekBarHeight / 2 ;
            int radius = (int) (widthSize * DEFAULT_RADIUS);
            // draw shadow
            defaultPaint.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.translate(0, radius * 0.25f);
            canvas.scale(1 + (0.1f * material), 1 + (0.1f * material), centerX, centerY);
            defaultPaint.setShader(shadowGradient);
            canvas.drawCircle(centerX, centerY, radius, defaultPaint);
            defaultPaint.setShader(null);
            canvas.restore();
            // draw body
            defaultPaint.setStyle(Paint.Style.FILL);
            if (isPrimary) {
                //if not set the color，it will use default color
                if (colorPrimary == 0){
                    defaultPaint.setColor(te.evaluate(material, 0xFFFFFFFF, 0xFFE7E7E7));
                }else {
                    defaultPaint.setColor(colorPrimary);
                }
            }else {
                if (colorSecondary == 0){
                    defaultPaint.setColor(te.evaluate(material, 0xFFFFFFFF, 0xFFE7E7E7));
                }else {
                    defaultPaint.setColor(colorSecondary);
                }
            }
            canvas.drawCircle(centerX, centerY, radius, defaultPaint);
            // draw border
            defaultPaint.setStyle(Paint.Style.STROKE);
            defaultPaint.setColor(0xFFD7D7D7);
            canvas.drawCircle(centerX, centerY, radius, defaultPaint);

        }

        final TypeEvaluator<Integer> te = new TypeEvaluator<Integer>() {
            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                int alpha = (int) (Color.alpha(startValue) + fraction * (Color.alpha(endValue) - Color.alpha(startValue)));
                int red = (int) (Color.red(startValue) + fraction * (Color.red(endValue) - Color.red(startValue)));
                int green = (int) (Color.green(startValue) + fraction * (Color.green(endValue) - Color.green(startValue)));
                int blue = (int) (Color.blue(startValue) + fraction * (Color.blue(endValue) - Color.blue(startValue)));
                return Color.argb(alpha, red, green, blue);
            }
        };

        /**
         * 拖动检测
         * @param event
         * @return
         */
        protected boolean collide(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();
            int offset = (int) (lineWidth * currPercent);
            return x > left + offset && x < right + offset && y > top && y < bottom;
        }

        private void slide(float percent) {
            if (percent < 0) percent = 0;
            else if (percent > 1) percent = 1;
            currPercent = percent;
        }


        private void materialRestore() {
            if (anim != null) anim.cancel();
            anim = ValueAnimator.ofFloat(material, 0);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    material = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    material = 0;
                    invalidate();
                }
            });
            anim.start();
        }

        public void setProgressHint(String hint){
            mHintText2Draw = hint;
        }
    }

    //*********************************** SeekBar ***********************************//

    public interface OnRangeChangedListener {

        void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser);
        void onStartTrackingTouch(RangeSeekBar view, boolean isLeft);
        void onStopTrackingTouch(RangeSeekBar view, boolean isLeft);
    }



    public void setOnRangeChangedListener(OnRangeChangedListener listener) {
        callback = listener;
    }

    public void setValue(float min, float max) {
        min = min + offsetValue;
        max = max + offsetValue;

        if (min < minValue) {
            throw new IllegalArgumentException("setValue() min < (preset min - offsetValue) . #min:" + min + " #preset min:" + minValue + " #offsetValue:" + offsetValue);
        }
        if (max > maxValue) {
            throw new IllegalArgumentException("setValue() max > (preset max - offsetValue) . #max:" + max + " #preset max:" + maxValue + " #offsetValue:" + offsetValue);
        }

        if (reserveCount > 1) {
            if ((min - minValue) % reserveCount != 0) {
                throw new IllegalArgumentException("setValue() (min - preset min) % reserveCount != 0 . #min:" + min + " #preset min:" + minValue + "#reserveCount:" + reserveCount + "#reserve:" + reserveValue);
            }
            if ((max - minValue) % reserveCount != 0) {
                throw new IllegalArgumentException("setValue() (max - preset min) % reserveCount != 0 . #max:" + max + " #preset min:" + minValue + "#reserveCount:" + reserveCount + "#reserve:" + reserveValue);
            }
            leftSB.currPercent = (min - minValue) / reserveCount * cellsPercent;
            if (mSeekBarMode == 2) {
                rightSB.currPercent = (max - minValue) / reserveCount * cellsPercent;
            }
        } else {
            leftSB.currPercent = (min - minValue) / (maxValue - minValue);
            if (mSeekBarMode == 2) {
                rightSB.currPercent = (max - minValue) / (maxValue - minValue);
            }
        }
        if (callback != null) {
            if (mSeekBarMode == 2) {
                callback.onRangeChanged(this, leftSB.currPercent, rightSB.currPercent, false);
            }else {
                callback.onRangeChanged(this, leftSB.currPercent, leftSB.currPercent, false);
            }
        }
        invalidate();
    }

    public void setValue(float value) {
        setValue(value,mMax);
    }

    public void setRange(float min, float max) {
        setRules(min, max, reserveCount, cellsCount);
    }

    public void setLineColor(int colorLineEdge, int colorLineSelected){
        this.colorLineEdge = colorLineEdge;
        this.colorLineSelected = colorLineSelected;
    }

    public void setThumbPrimaryColor(int thumbPrimaryColor){
        this.colorPrimary = thumbPrimaryColor;
    }

    public void setThumbSecondaryColor(int thumbSecondaryColor){
        this.colorSecondary = thumbSecondaryColor;
    }

    public void setCellsCount(int cellsCount) {
        this.cellsCount = cellsCount;
    }

    public void setThumbSize(int mThumbSize) {
        this.mThumbSize = mThumbSize;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setProgressHintMode(int mProgressHintMode) {
        this.mProgressHintMode = mProgressHintMode;
    }

    public void setProgressHintBGId(int mProgressHintBGId) {
        this.mProgressHintBGId = mProgressHintBGId;
    }

    public void setThumbResId(int mThumbResId) {
        this.mThumbResId = mThumbResId;
    }

    public void setCellMode(int mCellMode) {
        this.mCellMode = mCellMode;
    }

    public void setSeekBarMode(int mSeekBarMode) {
        this.mSeekBarMode = mSeekBarMode;
    }

    public void setRules(float min, float max, float reserve, int cells) {
        if (max <= min) {
            throw new IllegalArgumentException("setRules() max must be greater than min ! #max:" + max + " #min:" + min);
        }
        mMax = (int)max;
        mMin = (int)min;
        if (min < 0) {
            offsetValue = 0 - min;
            min = min + offsetValue;
            max = max + offsetValue;
        }
        minValue = min;
        maxValue = max;

        if (reserve < 0) {
            throw new IllegalArgumentException("setRules() reserve must be greater than zero ! #reserve:" + reserve);
        }
        if (reserve >= max - min) {
            throw new IllegalArgumentException("setRules() reserve must be less than (max - min) ! #reserve:" + reserve + " #max - min:" + (max - min));
        }
        if (cells < 1) {
            throw new IllegalArgumentException("setRules() cells must be greater than 1 ! #cells:" + cells);
        }
        cellsCount = cells;
        cellsPercent = 1f / cellsCount;
        reserveValue = reserve;
        reservePercent = reserve / (max - min);
        reserveCount = (int) (reservePercent / cellsPercent + (reservePercent % cellsPercent != 0 ? 1 : 0));
        if (cellsCount > 1) {
            if (mSeekBarMode == 2) {
                if (leftSB.currPercent + cellsPercent * reserveCount <= 1 && leftSB.currPercent + cellsPercent * reserveCount > rightSB.currPercent) {
                    rightSB.currPercent = leftSB.currPercent + cellsPercent * reserveCount;
                } else if (rightSB.currPercent - cellsPercent * reserveCount >= 0 && rightSB.currPercent - cellsPercent * reserveCount < leftSB.currPercent) {
                    leftSB.currPercent = rightSB.currPercent - cellsPercent * reserveCount;
                }
            }else {
                if (1 - cellsPercent * reserveCount >= 0 && 1 - cellsPercent * reserveCount < leftSB.currPercent) {
                    leftSB.currPercent = 1 - cellsPercent * reserveCount;
                }
            }
        } else {
            if (mSeekBarMode == 2) {
                if (leftSB.currPercent + reservePercent <= 1 && leftSB.currPercent + reservePercent > rightSB.currPercent) {
                    rightSB.currPercent = leftSB.currPercent + reservePercent;
                } else if (rightSB.currPercent - reservePercent >= 0 && rightSB.currPercent - reservePercent < leftSB.currPercent) {
                    leftSB.currPercent = rightSB.currPercent - reservePercent;
                }
            }else {
                if (1 - reservePercent >= 0 && 1 - reservePercent < leftSB.currPercent) {
                    leftSB.currPercent = 1 - reservePercent;
                }
            }
        }
        invalidate();
    }

    public float getMax(){
        return mMax;
    }

    public float getMin(){
        return mMin;
    }

    public float[] getCurrentRange() {
        float range = maxValue - minValue;
        if (mSeekBarMode == 2) {
            return new float[]{-offsetValue + minValue + range * leftSB.currPercent,
                    -offsetValue + minValue + range * rightSB.currPercent};
        }else {
            return new float[]{-offsetValue + minValue + range * leftSB.currPercent,
                    -offsetValue + minValue + range * 1.0f};
        }
    }



    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.isEnable = enabled;
    }

    public void setProgressDescription(String progress){
        if (leftSB != null){
            leftSB.setProgressHint(progress);
        }
        if (rightSB != null){
            rightSB.setProgressHint(progress);
        }
    }

    public void setLeftProgressDescription(String progress){
        if (leftSB != null){
            leftSB.setProgressHint(progress);
        }
    }

    public void setRightProgressDescription(String progress){
        if (rightSB != null){
            rightSB.setProgressHint(progress);
        }
    }

    private void isShowProgressHint(SeekBar seekBar, boolean isEnable){

        switch (mProgressHintMode){
            case HINT_MODE_DEFAULT:
                seekBar.isShowingHint = isEnable;
                break;
            case HINT_MODE_ALWAYS_SHOW:
            case HINT_MODE_TOUCH_ALWAYS_SHOW:
                seekBar.isShowingHint = true;
                break;
            case HINT_MODE_ALWAYS_HIDE:
                seekBar.isShowingHint = false;
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnable)return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                boolean touchResult = false;
                if (rightSB != null && rightSB.currPercent >= 1 && leftSB.collide(event)) {
                    currTouch = leftSB;
                    touchResult = true;

                } else if (rightSB != null && rightSB.collide(event)) {
                    currTouch = rightSB;
                    touchResult = true;

                } else if (leftSB.collide(event)) {
                    currTouch = leftSB;
                    touchResult = true;

                }

                //Intercept parent TouchEvent
                if(getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                if (callback != null) {
                    callback.onStartTrackingTouch(this, currTouch == leftSB);
                }
                return touchResult;
            case MotionEvent.ACTION_MOVE:

                float percent;
                float x = event.getX();

                currTouch.material = currTouch.material >= 1 ? 1 : currTouch.material + 0.1f;

                if (currTouch == leftSB) {
                    if (cellsCount > 1) {
                        if (x < lineLeft) {
                            percent = 0;
                        } else {
                            percent = (x - lineLeft) * 1f / (lineWidth);
                        }
                        int touchLeftCellsValue = Math.round(percent / cellsPercent);
                        int currRightCellsValue;
                        if (mSeekBarMode == 2) {
                            currRightCellsValue = Math.round(rightSB.currPercent / cellsPercent);
                        }else {
                            currRightCellsValue = Math.round(1.0f / cellsPercent);
                        }
                        percent = touchLeftCellsValue * cellsPercent;

                        while (touchLeftCellsValue > currRightCellsValue - reserveCount) {
                            touchLeftCellsValue--;
                            if (touchLeftCellsValue < 0) break;
                            percent = touchLeftCellsValue * cellsPercent;
                        }
                    } else {
                        if (x < lineLeft) {
                            percent = 0;
                        } else {
                            percent = (x - lineLeft) * 1f / (lineWidth );
                        }
                        if (mSeekBarMode == 2) {
                            if (percent > rightSB.currPercent - reservePercent) {
                                percent = rightSB.currPercent - reservePercent;
                            }
                        }else {
                            if (percent > 1.0f - reservePercent) {
                                percent = 1.0f - reservePercent;
                            }
                        }
                    }
                    leftSB.slide(percent);
                    isShowProgressHint(leftSB, true);

                    //Intercept parent TouchEvent
                    if(getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else if (currTouch == rightSB) {
                    if (cellsCount > 1) {
                        if (x > lineRight) {
                            percent = 1;
                        } else {
                            percent = (x - lineLeft) * 1f / (lineWidth);
                        }
                        int touchRightCellsValue = Math.round(percent / cellsPercent);
                        int currLeftCellsValue = Math.round(leftSB.currPercent / cellsPercent);
                        percent = touchRightCellsValue * cellsPercent;

                        while (touchRightCellsValue < currLeftCellsValue + reserveCount) {
                            touchRightCellsValue++;
                            if (touchRightCellsValue > maxValue - minValue) break;
                            percent = touchRightCellsValue * cellsPercent;
                        }
                    } else {
                        if (x > lineRight) {
                            percent = 1;
                        } else {
                            percent = (x - lineLeft ) * 1f / (lineWidth );
                        }
                        if (percent < leftSB.currPercent + reservePercent) {
                            percent = leftSB.currPercent + reservePercent;
                        }
                    }
                    rightSB.slide(percent);
                    isShowProgressHint(rightSB, true);
                }

                if (callback != null) {
                    float[] result = getCurrentRange();
                    callback.onRangeChanged(this, result[0], result[1],true);
                }
                invalidate();

                //Intercept parent TouchEvent
                if(getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                if (mSeekBarMode == 2) {
                    isShowProgressHint(rightSB, false);
                }
                isShowProgressHint(leftSB, false);
                if (callback != null) {
                    float[] result = getCurrentRange();
                    callback.onRangeChanged(this, result[0], result[1],false);
                }

                //Intercept parent TouchEvent
                if(getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mSeekBarMode == 2) {
                    isShowProgressHint(rightSB, false);
                }
                isShowProgressHint(leftSB, false);
                currTouch.materialRestore();

                if (callback != null) {
                    float[] result = getCurrentRange();
                    callback.onRangeChanged(this, result[0], result[1],false);
                }

                //Intercept parent TouchEvent
                if(getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                if (callback != null) {
                    callback.onStopTrackingTouch(this, currTouch == leftSB);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            // Single color bitmap will be created of mThumbSize x mThumbSize pixel
            bitmap = Bitmap.createBitmap(mThumbSize, mThumbSize, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.minValue = minValue - offsetValue;
        ss.maxValue = maxValue - offsetValue;
        ss.reserveValue = reserveValue;
        ss.cellsCount = cellsCount;
        float[] results = getCurrentRange();
        ss.currSelectedMin = results[0];
        ss.currSelectedMax = results[1];
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        float min = ss.minValue;
        float max = ss.maxValue;
        float reserve = ss.reserveValue;
        int cells = ss.cellsCount;
        setRules(min, max, reserve, cells);
        float currSelectedMin = ss.currSelectedMin;
        float currSelectedMax = ss.currSelectedMax;
        setValue(currSelectedMin, currSelectedMax);
    }

    private static class SavedState extends BaseSavedState {
        private float minValue;
        private float maxValue;
        private float reserveValue;
        private int cellsCount;
        private float currSelectedMin;
        private float currSelectedMax;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            minValue = in.readFloat();
            maxValue = in.readFloat();
            reserveValue = in.readFloat();
            cellsCount = in.readInt();
            currSelectedMin = in.readFloat();
            currSelectedMax = in.readFloat();


        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(minValue);
            out.writeFloat(maxValue);
            out.writeFloat(reserveValue);
            out.writeInt(cellsCount);
            out.writeFloat(currSelectedMin);
            out.writeFloat(currSelectedMax);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private  int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Compare the size of two floating point numbers
     * @param a
     * @param b
     * @return
     * 1 is a > b
     * -1 is a < b
     * 0 is a == b
     */
    private int compareFloat(float a, float b){

        int ta = Math.round(a * 1000);
        int tb = Math.round(b * 1000);
        if (ta > tb){
            return 1;
        }else if (ta < tb){
            return -1;
        }else {
            return 0;
        }
    }



}