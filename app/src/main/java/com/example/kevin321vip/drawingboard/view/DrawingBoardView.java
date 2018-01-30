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
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    private int mStrokeWidth = 5;
    private List<Path> mPaths = new ArrayList<>();
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
        mPaint.setStrokeWidth(mStrokeWidth);
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
        canvas.save();
        if (mPatternType == PatternType.STRAIGHT_LINE) {
            //绘制直线
            canvas.save();
            canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);
        } else {
            canvas.drawPath(mPath, mPaint);
            if (mPaths.size() > 0) {
                for (int i = 0; i < mPaths.size(); i++) {
                    Path path = mPaths.get(i);
                    canvas.drawPath(path, mPaint);
                    Log.i("draw", "绘制了Path:" + path + "path的Index：" + i);
                }
            }
        }
        canvas.restore();
    }

    //控件被触摸的时候
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start_x = event.getX();
                start_y = event.getY();
                mPath.moveTo(start_x, start_y);
                break;
            case MotionEvent.ACTION_MOVE:
                end_x = event.getX();
                end_y = event.getY();
                if (mPatternType == PatternType.CURVE) {
                    mPath.lineTo(end_x, end_y);
                } else if (mPatternType == PatternType.RECTANGLE) {
                    mPath.reset();
                    mPath.addRect(start_x, start_y, end_x, end_y, Path.Direction.CCW);
                } else if (mPatternType == PatternType.ROUND) {
                    //绘制椭圆的路径
                    mPath.reset();
                    mPath.addArc(start_x, start_y, end_x, end_y, 0, 360);
                }
                //是绘画的动作生效
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Path path = new Path();
                path.addPath(mPath);
                mPaths.add(path);
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }


    //清空画布

    public void clearCanvas() {
        //清除了界面
        mPaths.clear();
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
