package com.xubo.scrolltextview;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 类名: ScrollTextView</br>
 * 包名：com.xubo.scrolltext.view </br>
 * 描述: 滚动的文字View</br>
 * 发布版本号：</br>
 * 开发人员： 徐波</br>
 * 创建时间： 2016年6月15日
 */
public class ScrollTextView extends View implements OnClickListener
{
    /** 文字默认颜色 */
    private static final int TEXT_COLOR = Color.BLACK;
    
    /** 文字默认大小(单位sp) */
    private static final int TEXT_SIZE = 16;
    
    /** 单行模式 */
    private static final boolean SINGLE_LINE = true;
    
    /** 单行显示不下默认带省略号(单行模式下才有效) */
    private static final boolean ELLIPSIS = true;
    
    /** 文字滚动时间 */
    private static final long SCROLL_TIME = 500;
    
    /** 文字切换时间 */
    private static final long CUT_TIME = 3000;
    
    /** 是否单行模式 */
    private boolean isSingleLine;
    
    /** 单行显示不下是否自带省略号 */
    private boolean isEllipsis;
    
    /** 文字绘制画笔 */
    private Paint mPaint;
    
    /** 文字高度 */
    private float mTextHeight;
    
    /** 文字偏移距离 */
    private float mTextOffsetY;
    
    /** 滚动文字集合 */
    private List<String> mContents;
    
    /** 滚动文字点击监听集合 */
    private List<OnScrollClickListener> mListeners;
    
    /** 文字信息集合 */
    private Queue<List<TextInfo>> mTextInfos;
    
    /** 省略号信息集合 */
    private List<TextInfo> mEllipsisTextInfos;
    
    /** 省略号宽度 */
    private float mEllipsisTextWidth;
    
    /** 滚动的高度 */
    private int mTop;
    
    /** 当前显示的文字信息 */
    private List<TextInfo> mCurrentTextInfos;
    
    /** 索引信息集合 */
    private Map<List<TextInfo>, OnScrollClickListener> mIndexMap;
    
    public ScrollTextView(Context context)
    {
        // TODO Auto-generated constructor stub
        this(context, null);
    }
    
    public ScrollTextView(Context context, AttributeSet attrs)
    {
        // TODO Auto-generated constructor stub
        this(context, attrs, 0);
    }
    
    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollTextLayout, defStyleAttr, 0);
        int textColor = TEXT_COLOR;
        float textSize = sp2px(context, TEXT_SIZE);
        if (typedArray != null)
        {
            textColor = typedArray.getColor(R.styleable.ScrollTextLayout_textColor, textColor);
            textSize = typedArray.getDimension(R.styleable.ScrollTextLayout_textSize, textSize);
            isSingleLine = typedArray.getBoolean(R.styleable.ScrollTextLayout_singleLine, SINGLE_LINE);
            isEllipsis = typedArray.getBoolean(R.styleable.ScrollTextLayout_ellipsis, ELLIPSIS);
        }
        typedArray.recycle();
        
        mPaint = new Paint();
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.setAntiAlias(true);
        
        FontMetrics fontMetrics = mPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom - fontMetrics.top;
        mTextOffsetY = -fontMetrics.top;
        
        mIndexMap = new HashMap<List<TextInfo>, OnScrollClickListener>();
        mTextInfos = new LinkedList<List<TextInfo>>();
        mEllipsisTextInfos = new ArrayList<TextInfo>();
        
        setOnClickListener(this);
    }
    
    ValueAnimator mValueAnimator;
    
    Handler mHandler = new Handler();
    
    Runnable mRunnable = new Runnable()
    {
        
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            if (mTextInfos.size() > 1)
            {
                mValueAnimator = ValueAnimator.ofFloat(0.0f, -1.0f);
                mValueAnimator.setDuration(SCROLL_TIME);
                mValueAnimator.addListener(new AnimatorListener()
                {
                    
                    @Override
                    public void onAnimationStart(Animator animation)
                    {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        // TODO Auto-generated method stub
                        mTop = 0;
                        mCurrentTextInfos = mTextInfos.poll();
                        mTextInfos.offer(mCurrentTextInfos);
                        startTextScroll();
                    }
                    
                    @Override
                    public void onAnimationCancel(Animator animation)
                    {
                        // TODO Auto-generated method stub
                        mTop = 0;
                    }
                });
                mValueAnimator.addUpdateListener(new AnimatorUpdateListener()
                {
                    
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation)
                    {
                        // TODO Auto-generated method stub
                        float value = (Float)animation.getAnimatedValue();
                        mTop = (int)(value * (mTextHeight + getPaddingTop() + getPaddingBottom()));
                        invalidate();
                    }
                    
                });
                mValueAnimator.start();
            }
        }
    };
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        int textMaxWidth = 0;
        if (mContents != null && mContents.size() > 0)
        {
            textMaxWidth = textTypesetting(measuredWidth - getPaddingLeft() - getPaddingRight(), mContents);
            mCurrentTextInfos = mTextInfos.poll();
            mTextInfos.offer(mCurrentTextInfos);
        }
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED)
        {
            measuredWidth = textMaxWidth + getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED)
        {
            measuredHeight = (int)(mTextHeight + getPaddingBottom() + getPaddingTop());
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
        startTextScroll();
    }
    
    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.dispatchDraw(canvas);
        if (mCurrentTextInfos != null && mCurrentTextInfos.size() > 0)
        {
            for (TextInfo textInfo : mCurrentTextInfos)
            {
                canvas.drawText(textInfo.text,
                    textInfo.x + getPaddingLeft(),
                    textInfo.y + getPaddingTop() + mTop,
                    mPaint);
            }
        }
        if (mTextInfos.size() > 1)
        {
            List<TextInfo> nextTextInfos = mTextInfos.peek();
            if (nextTextInfos != null && nextTextInfos.size() > 0)
            {
                for (TextInfo textInfo : nextTextInfos)
                {
                    canvas.drawText(textInfo.text, textInfo.x + getPaddingLeft(), textInfo.y + getPaddingTop() + mTop
                        + mTextHeight + getPaddingTop() + getPaddingBottom(), mPaint);
                }
            }
        }
    }
    
    /**
     * 描述: 设置滚动内容</br>
     * 开发人员：徐波</br>
     * 创建时间：2016年6月15日</br>
     * @param list 滚动内容集合
     */
    public void setTextContent(List<String> list)
    {
        this.mContents = list;
        this.mListeners = null;
        requestLayout();
        invalidate();
    }
    
    /**
     * 描述: 设置滚动内容</br>
     * 开发人员：徐波</br>
     * 创建时间：2016年6月15日</br>
     * @param list 滚动内容集合
     * @param listener 滚动内容的点击监听集合
     */
    public void setTextContent(List<String> list, List<OnScrollClickListener> listener)
    {
        this.mContents = list;
        this.mListeners = listener;
        requestLayout();
        invalidate();
    }
    
    /**
     * 描述: 设置文字颜色</br>
     * 开发人员：徐波</br>
     * 创建时间：2016年6月15日</br>
     * @param color 文字颜色
     */
    public void setTextColor(int color)
    {
        mPaint.setColor(color);
        invalidate();
    }
    
    /**
     * 描述: 设置文字大小</br>
     * 开发人员：徐波</br>
     * 创建时间：2016年6月15日</br>
     * @param size 文字大小
     */
    public void setTextSize(float size)
    {
        mPaint.setTextSize(size);
        FontMetrics fontMetrics = mPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom - fontMetrics.top;
        mTextOffsetY = -fontMetrics.top;
        requestLayout();
        invalidate();
    }
    
    /**
     * 描述: 文字开始自动滚动,初始化会自动调用该方法(适当的时机调用)</br>
     * 开发人员：徐波</br>
     * 创建时间：2016年6月15日</br>
     */
    public synchronized void startTextScroll()
    {
        if (mValueAnimator != null && mValueAnimator.isRunning())
        {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, CUT_TIME);
    }
    
    /**
     * 描述: 文字暂停滚动(适当的时机调用)</br>
     * 开发人员：徐波</br>
     * 创建时间：2016年6月15日</br>
     */
    public synchronized void stopTextScroll()
    {
        if (mValueAnimator != null && mValueAnimator.isRunning())
        {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
        mHandler.removeCallbacks(mRunnable);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        if (mCurrentTextInfos != null && mListeners != null)
        {
            OnScrollClickListener onScrollClickListener = mIndexMap.get(mCurrentTextInfos);
            if (onScrollClickListener != null)
            {
                onScrollClickListener.onClick();
            }
        }
    }
    
    /**
     * 描述: 文字排版</br>
     * 开发人员：徐波</br>
     * 创建时间：2016年6月15日</br>
     * @param maxParentWidth
     * @param list 排版的滚动文字
     * @return 排版文字的宽度
     */
    private int textTypesetting(float maxParentWidth, List<String> list)
    {
        // 清空数据及初始化数据
        mTextInfos.clear();
        mIndexMap.clear();
        mEllipsisTextInfos.clear();
        mEllipsisTextWidth = 0f;
        // 初始化省略号
        if (isSingleLine && isEllipsis)
        {
            String ellipsisText = "...";
            for (int i = 0; i < ellipsisText.length(); i++)
            {
                char ch = ellipsisText.charAt(i);
                float[] widths = new float[1];
                String srt = String.valueOf(ch);
                mPaint.getTextWidths(srt, widths);
                TextInfo textInfo = new TextInfo();
                textInfo.text = srt;
                textInfo.x = mEllipsisTextWidth;
                textInfo.y = mTextOffsetY;
                mEllipsisTextInfos.add(textInfo);
                mEllipsisTextWidth += widths[0];
            }
        }
        // 文字排版
        float maxWidth = 0;
        // 文字排版最大宽度
        float tempMaxWidth = 0f;
        int index = 0;
        for (String text : list)
        {
            if (isNullOrEmpty(text))
            {
                continue;
            }
            // 排版文字当前的宽度
            float textWidth = 0;
            // 文字信息集合
            List<TextInfo> textInfos = new ArrayList<TextInfo>();
            if (isSingleLine)
            {
                // 临时文字信息集合
                List<TextInfo> tempTextInfos = new ArrayList<TextInfo>();
                // 单行排不下
                boolean isLess = false;
                // 省略号的起始位置
                float ellipsisStartX = 0;
                for (int j = 0; j < text.length(); j++)
                {
                    char ch = text.charAt(j);
                    float[] widths = new float[1];
                    String srt = String.valueOf(ch);
                    mPaint.getTextWidths(srt, widths);
                    TextInfo textInfo = new TextInfo();
                    textInfo.text = srt;
                    textInfo.x = textWidth;
                    textInfo.y = mTextOffsetY;
                    textWidth += widths[0];
                    if (textWidth <= maxParentWidth - mEllipsisTextWidth) // 当排版的宽度小于等于最大宽度去除省略号长度时
                    {
                        textInfos.add(textInfo);
                        ellipsisStartX = textWidth;
                    }
                    else if (textWidth <= maxParentWidth) // 当排版宽度小于最大宽度时
                    {
                        tempTextInfos.add(textInfo);
                    }
                    else
                    // 最大宽度排版不下
                    {
                        isLess = true;
                        break;
                    }
                }
                if (isLess)
                {
                    tempMaxWidth = maxParentWidth;
                    for (TextInfo ellipsisTextInfo : mEllipsisTextInfos)
                    {
                        TextInfo textInfo = new TextInfo();
                        textInfo.text = ellipsisTextInfo.text;
                        textInfo.x = (ellipsisTextInfo.x + ellipsisStartX);
                        textInfo.y = ellipsisTextInfo.y;
                        textInfos.add(textInfo);
                    }
                }
                else
                {
                    tempMaxWidth = textWidth;
                    textInfos.addAll(tempTextInfos);
                }
                if (tempMaxWidth > maxWidth)
                {
                    maxWidth = tempMaxWidth;
                }
                mTextInfos.offer(textInfos);
                if (mListeners != null && mListeners.size() > index)
                {
                    mIndexMap.put(textInfos, mListeners.get(index));
                }
                index++;
            }
            else
            {
                for (int j = 0; j < text.length(); j++)
                {
                    char ch = text.charAt(j);
                    float[] widths = new float[1];
                    String srt = String.valueOf(ch);
                    mPaint.getTextWidths(srt, widths);
                    TextInfo textInfo = new TextInfo();
                    textInfo.text = srt;
                    textInfo.x = textWidth;
                    textInfo.y = mTextOffsetY;
                    textWidth += widths[0];
                    if (textWidth > maxParentWidth) // 当排版宽度小于最大宽度时
                    {
                        tempMaxWidth = maxParentWidth;
                        mTextInfos.offer(textInfos);
                        if (mListeners != null && mListeners.size() > index)
                        {
                            mIndexMap.put(textInfos, mListeners.get(index));
                        }
                        
                        textInfos = new ArrayList<TextInfo>();
                        textInfo.x = 0;
                        textInfo.y = mTextOffsetY;
                        textWidth = widths[0];
                    }
                    textInfos.add(textInfo);
                }
                if (textWidth > tempMaxWidth)
                {
                    
                    tempMaxWidth = textWidth;
                }
                mTextInfos.offer(textInfos);
                if (tempMaxWidth > maxWidth)
                {
                    maxWidth = tempMaxWidth;
                }
                if (mListeners != null && mListeners.size() > index)
                {
                    mIndexMap.put(textInfos, mListeners.get(index));
                }
                index++;
            }
        }
        return (int)maxWidth;
    }
    
    @Override
    protected void onDetachedFromWindow()
    {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        if (mValueAnimator != null && mValueAnimator.isRunning())
        {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
        mHandler.removeCallbacks(mRunnable);
        mHandler = null;
    }
    
    /**
     * 描述: sp转px</br>
     * 开发人员：徐波</br>
     * 创建时间：2016年6月15日</br>
     * @param context 上下文
     * @param spValue sp值
     * @return 转换后对应的px值
     */
    private float sp2px(Context context, float spValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale;
    }
    
    /**
     * 描述: 判断字符串是否为空</br>
     * 开发人员：徐波</br>
     * 创建时间：2016年6月15日</br>
     * @param text
     * @return true为空,fale不为空
     */
    private boolean isNullOrEmpty(String text)
    {
        if (text == null || "".equals(text.trim()) || text.trim().length() == 0 || "null".equals(text.trim())
            || "empty".equals(text))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 类名: TextInfo</br>
     * 包名：com.xubo.scrolltext.view </br>
     * 描述: 绘制文字信息</br>
     * 发布版本号：</br>
     * 开发人员： 徐波</br>
     * 创建时间： 2016年6月15日
     */
    public class TextInfo
    {
        /** x坐标 */
        public float x;
        
        /** y坐标 */
        public float y;
        
        /** 内容 */
        public String text;
    }
    
    /**
     * 类名: OnScrollClickListener</br>
     * 包名：com.xubo.scrolltext.view </br>
     * 描述: 滚动内容点击监听事件</br>
     * 发布版本号：</br>
     * 开发人员： 徐波</br>
     * 创建时间： 2016年6月15日
     */
    public interface OnScrollClickListener
    {
        public void onClick();
    }
}
