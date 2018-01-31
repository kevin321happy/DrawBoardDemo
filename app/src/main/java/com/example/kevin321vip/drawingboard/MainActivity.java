package com.example.kevin321vip.drawingboard;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.kevin321vip.drawingboard.view.DrawingBoardView;
import com.example.kevin321vip.drawingboard.view.PatternType;
import com.example.kevin321vip.drawingboard.view.PopUpView;

public class MainActivity extends Activity implements PopUpView.OnChildMenuClickListener {

    private DrawingBoardView mDraw_view;
    private PopUpView mPop_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDraw_view = (DrawingBoardView) findViewById(R.id.draw_view);
        mPop_view = (PopUpView) findViewById(R.id.pop_view);
        mPop_view.setOnChildMenuClickListener(this);
        //设置控制的弹窗
        mDraw_view.setControlledPopover(mPop_view);

    }
    @Override
    public void onChildClick(int currentChild) {
        switch (currentChild) {
            case 0:
                mDraw_view.setVisibility(View.VISIBLE);
                mDraw_view.setPatternType(PatternType.CURVE);
                Toast.makeText(this, "开启画笔", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                mDraw_view.setVisibility(View.VISIBLE);
                mDraw_view.setPatternType(PatternType.RECTANGLE);
                Toast.makeText(this, "绘制矩形", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                mDraw_view.setVisibility(View.VISIBLE);
                mDraw_view.setPatternType(PatternType.ROUND);
                Toast.makeText(this, "绘制圆形", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                mDraw_view.repeal();
                break;
            case 4:
                mDraw_view.clearCanvas();
                Toast.makeText(this, "清除绘制", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
