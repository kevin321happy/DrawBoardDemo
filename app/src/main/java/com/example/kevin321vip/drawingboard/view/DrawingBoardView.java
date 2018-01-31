package com.example.kevin321vip.drawingboard.view;

import android.content.Context;
import android.content.res.TypedArray;
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

import com.example.kevin321vip.drawingboard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin321vip on 2017/7/18.
 */

public class DrawingBoardView extends View implements View.OnClickListener, PopUpView.OnChildMenuClickListener {
    private Paint mPaint;
    private float start_x;
    private float start_y;
    private float end_x;
    private float end_y;
    private Context mContext;

    private Path mPath;
    private int mStrokeWidth = 5;
    private List<Path> mPaths = new ArrayList<>();
    /**
     * 标志是否需要重绘
     */
    private boolean REPEAL_FLAG = false;
    /**
     * 绘制的线的类型,默认是曲线
     */
    private PatternType mPatternType = PatternType.ROUND;
    private int mPaintColor;
    /**
     * 控制的View
     */
    private PopUpView mControlView;

    /**
     * 是否菜单展开了
     */
    private boolean MENU_OPEN = false;

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
        this(context, attrs, 0);
    }

    public DrawingBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        getAttr(context, attrs);
    }

    /**
     * 获取自定义属性
     * @param context
     * @param attrs
     */
    private void getAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawingBoardView);
        mPaintColor = typedArray.getColor(R.styleable.DrawingBoardView_DrawBoardPaintColor, Color.RED);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.DrawingBoardView_DrawBoardStrokeWidth, 5);
        typedArray.recycle();
        init();

    }

    //初始化操作
    private void init() {
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);//非填充的画笔
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mPaintColor);//画笔的颜色
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//设置头像抖动处理
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置头像结合的方式
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔为原型的样式
        //绘制的路径
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO 自动生成的方法存根
        super.onDraw(canvas);
        canvas.save();
        //如果是重绘操作
        if (REPEAL_FLAG) {
            if (mPaths.size() == 0) {
                clearCanvas();
            } else {
                for (int i = 0; i < mPaths.size(); i++) {
                    Path path = mPaths.get(i);
                    canvas.drawPath(path, mPaint);
                    Log.i("draw", "绘制了Path:" + path + "path的Index：" + i);
                }
            }
            REPEAL_FLAG = false;
            return;
        }
        if (mPatternType == PatternType.STRAIGHT_LINE) {
            //绘制直线
            canvas.save();
            canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);
        } else {
            //先绘制临时的mpath，然后绘制path集合中的path
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
                //移动到按下的点
                mPath.moveTo(start_x, start_y);
                break;
            case MotionEvent.ACTION_MOVE:
                end_x = event.getX();
                end_y = event.getY();

                if (mPatternType == PatternType.CURVE) {
                    //曲线
                    mPath.lineTo(end_x, end_y);
                } else if (mPatternType == PatternType.RECTANGLE) {
                    //矩形
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
                //在Up中添加在最后绘制的Path到集合中
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
        Toast.makeText(mContext, "你点我干嘛？", Toast.LENGTH_SHORT).show();
    }

    /**
     * 撤销
     */
    public void repeal() {
        //移除最后一次绘制的path
        if (mPaths != null && mPaths.size() > 0) {
            mPaths.remove(mPaths.size() - 1);
            REPEAL_FLAG = true;
            invalidate();
        }
    }

    /**
     * 设置控制的弹窗
     *
     * @param pop_view
     */
    public void setControlledPopover(PopUpView pop_view) {
        this.mControlView = pop_view;
        mControlView.setOnChildMenuClickListener(this);

    }

    @Override
    public void onChildClick(int currentChild) {
        switch (currentChild) {
            case 0:
                setVisibility(View.VISIBLE);
                setPatternType(PatternType.CURVE);
                Toast.makeText(mContext, "开启画笔", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                setVisibility(View.VISIBLE);
                setPatternType(PatternType.RECTANGLE);
                Toast.makeText(mContext, "绘制矩形", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                setVisibility(View.VISIBLE);
                setPatternType(PatternType.ROUND);
                Toast.makeText(mContext, "绘制圆形", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                repeal();
                break;
            case 4:
                clearCanvas();
                Toast.makeText(mContext, "清除绘制", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                if (MENU_OPEN) {
                    //如果打开则置为关闭
                    setVisibility(GONE);
                    MENU_OPEN = false;
                } else {
                    setVisibility(VISIBLE);
                    MENU_OPEN = true;
                }

                break;


        }
    }
}
