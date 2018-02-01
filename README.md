# DrawBoardDemo
## 一个画板的控件
[![CDNJS](https://img.shields.io/badge/Gradle-3.0.1-blue.svg)]()   [![Gem](https://img.shields.io/badge/Widget-drawingboard-orange.svg)]()   [![Travis branch](https://img.shields.io/badge/build-passing-brightgreen.svg)]()

### 两个自定义控件,一个画布的View，一个控制绘制操作的弹窗菜单

状态 | 功能
-------- | ---
**支持**|**曲线、矩形、椭圆等随手势的滑动进行绘制**
**支持**|**绘制图形的大小任意,可多种图案同时存在在画板上**
**支持**|**绘制的撤销、清屏操作**
**支持**|**绘制的画笔的颜色粗细的设置**
**支持**|**在此基础上拓展增加其他类型的图案的绘制**
<div align="center">
    <img src="https://github.com/kevin321happy/DrawBoardDemo/blob/master/app/src/main/gif/draw_board.gif" width="400">
  </div>


#### * 主要是通过path的绘制来实现
- 在onTouch的Down事件中将path.moveTo（downx,down,y）,在Move事件中根据设置的图案类型,path设置对应的路径

- 避免在move事件中频繁绘制大量的重复,需要在绘制图案前调用mPath.reset(),（mPath是临时的Path变量）最后绘制出来的是一个图形。

- 由于上面在move中进行了mPath.reset，这样导致了新的问题,同一个图案永远只能在画报中显示一个。通过定义一个Path的集合,在每一次的Up事件中new 一个Path,并调用path.add(mPath)添加mPath,然后添加到集合中,保证每一次的路径都保存下来。然后在onDraw中,绘制完临时的mPath后再遍历绘制path集合中的内容。

- 撤销的实现是通过移除mpathList中的最后一个Path,然后进行重绘。

#### * Touch事件中的处理
```java
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

```

