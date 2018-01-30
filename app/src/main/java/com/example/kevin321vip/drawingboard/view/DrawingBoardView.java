package com.example.kevin321vip.drawingboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by kevin321vip on 2017/7/18.
 */

public class DrawingBoardView extends View implements View.OnClickListener {
    private Paint mPaint;
    private Bitmap mBitmap;
    private float start_x;
    private float start_y;
    private float end_x;
    private float end_y;
    private Canvas mCanvas;
    private Context mContext;
    private int mWidth_size;
    private int mHeight_size;
    private Path mPath;
    private RectF mRectF = new RectF();
    /**
     * 绘制的线的类型,默认是曲线
     */
    private PatternType mPatternType = PatternType.ROUND;

    /**
     * 设置绘制的线的类型
     *
     * @param patternType
     */
    public void setPatternType(PatternType patternType) {
        mPatternType = patternType;
    }

    public DrawingBoardView(Context context) {
        super(context);
        this.mContext = context;
    }

    public DrawingBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawingBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //初始化操作
    private void init() {
        mPaint = new Paint(Paint.DITHER_FLAG);
        mBitmap = Bitmap.createBitmap(mWidth_size, mHeight_size, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint.setStyle(Paint.Style.STROKE);//非填充的画笔
        mPaint.setStrokeWidth(6);
        mPaint.setColor(Color.RED);//画笔的颜色
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//设置头像抖动处理
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置头像结合的方式
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔为原型的样式
        //绘制的路径
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth_size = MeasureSpec.getSize(widthMeasureSpec);
        mHeight_size = MeasureSpec.getSize(heightMeasureSpec);
        init();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO 自动生成的方法存根
        super.onDraw(canvas);
        if (mPatternType == PatternType.STRAIGHT_LINE) {
            //绘制直线
            canvas.save();
            canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);
        } else {
            //绘制矩形,曲线，圆形
            canvas.drawPath(mPath, mPaint);
        }
    }

    //控件被触摸的时候
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //检测手指落下的动作
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            start_x = event.getX();//手指再屏幕上面按下的x点的坐标
            start_y = event.getY();//手指再屏幕上面按下的y点的坐标
            mPath.moveTo(start_x, start_y);
        }
        //当手指再滑动操作的时候
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            end_x = event.getX();
            end_y = event.getY();
            //曲线类型
            if (mPatternType == PatternType.CURVE) {
                mPath.lineTo(end_x, end_y);
            } else if (mPatternType == PatternType.RECTANGLE) {
                //绘制
                if (Math.abs(start_y - end_y) > 30) {
                    mPath.addRect(start_x, start_y, end_x, end_y, Path.Direction.CCW);
                }
            } else if (mPatternType == PatternType.ROUND) {
                //绘制园
                mRectF.left = start_x;
                mRectF.top = start_y;
                mRectF.bottom = end_y;
                mRectF.right = end_x;
                mPath.reset();
                mPath.addArc(mRectF, 0, 360);
            }
        }
        invalidate();//是绘画的动作生效
        return true;
    }

    //清空画布
    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    @Override
    public void onClick(View view) {
        //TODO 存根处理
        mCanvas.save();
        Toast.makeText(mContext, "你点我干嘛？", Toast.LENGTH_SHORT).show();
    }
}
