package com.example.kevin321vip.drawingboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
        mCanvas.save();
        mCanvas.restore();
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }

    //控件被触摸的时候
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //检测手指落下的动作
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            start_x = event.getX();//手指再屏幕上面按下的x点的坐标
            start_y = event.getY();//手指再屏幕上面按下的y点的坐标
            mPath.moveTo(start_x,start_y);
//            mCanvas.drawPoint(start_x, start_y, mPaint);//画出当前按下的点
        }
        //当手指再滑动操作的时候
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            end_x = event.getX();
            end_y = event.getY();
            mPath.lineTo(end_x,end_y);
//            mCanvas.drawLine(start_x, start_y, end_x, end_y, mPaint);//在画布上面画线
            //上一个点的结束点是下一个位置的开始点
//            start_x = end_x;
//            start_y = end_y;
        }
        invalidate();//是绘画的动作生效
        return true;
    }

    //清空画布
    public void clearCanvas() {
        mPath.reset();
      //  mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    @Override
    public void onClick(View view) {
        //TODO 存根处理
        mCanvas.save();
        Toast.makeText(mContext, "你点我干嘛？", Toast.LENGTH_SHORT).show();
    }
}
