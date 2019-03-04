package cn.gxh.view.goo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

public class GooView extends View {

    private Paint paint;
    //固定圆圆心
    private PointF mStickCenter = new PointF(200, 200);
    //固定圆半径
    private float mStickRadius = 10;

    //拖动圆圆心
    private PointF mDragCenter = new PointF(200, 100);
    //拖动圆半径
    private float mDragRadius = 15;
    //最大范围
    private float mMaxRange=80;
    //是否超出最大范围
    private boolean mOutOfRange=false;
    //是否消失
    private boolean mDisppear=false;

    //控制点
    private PointF mCtrlPoint=new PointF(200,350);

    private PointF[] mStickPoints = new PointF[]{
            new PointF(300, 300),//A
            new PointF(300, 400) //D
    };

    private PointF[] mDragPoints = new PointF[]{
            new PointF(100, 300),//B
            new PointF(100, 400) //C
    };


    public GooView(Context context) {
        super(context);
        init();
    }

    public GooView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GooView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);//去锯齿
    }


    @Override
    protected void onDraw(Canvas canvas) {

        float distance=GeometryUtil.getDistanceBetween2Points(mDragCenter,mStickCenter);
        float start=mStickRadius;
        float end=mStickRadius*0.3f;
        if(distance>mMaxRange){
            distance=mMaxRange;
        }
        float percent=distance/mMaxRange;
        float tempRadius=start+(end-start)*percent;


        //两个圆心组成直线的斜率
        float dy=mDragCenter.y-mStickCenter.y;
        float dx=mDragCenter.x-mStickCenter.x;

        Double lineK=null;
        if(dx!=0){
            lineK=(double)(dy/dx);
        }

        //获取A和D的坐标  参数三是与AD直线垂直的直线的斜率,也就是两个圆心组织的直线的斜率
        mStickPoints=GeometryUtil.getIntersectionPoints(mStickCenter,tempRadius,lineK);

        //BC
        mDragPoints=GeometryUtil.getIntersectionPoints(mDragCenter,mDragRadius,lineK);

        //控制点为圆心连线的中点
        mCtrlPoint=GeometryUtil.getMiddlePoint(mDragCenter,mDragCenter);

        if(!mDisppear){
            //2.绘制拖动圆
            canvas.drawCircle(mDragCenter.x, mDragCenter.y, mDragRadius, paint);

            if(!mOutOfRange){
                //1.绘制固定圆
                canvas.drawCircle(mStickCenter.x, mStickCenter.y, tempRadius, paint);

                //3.绘制连接部分
                Path path = new Path();
                path.moveTo(mStickPoints[0].x, mStickPoints[0].y);
                //A-->B曲线    前两个参数是控制点，后两个是B
                path.quadTo(mCtrlPoint.x, mCtrlPoint.y, mDragPoints[0].x, mDragPoints[0].y);
                //B-->C直线
                path.lineTo(mDragPoints[1].x, mDragPoints[1].y);
                //C-->D曲线
                path.quadTo(mCtrlPoint.x, mCtrlPoint.y, mStickPoints[1].x,mStickPoints[1].y);

                path.close();//会把起点和终点连接起来
                canvas.drawPath(path, paint);
            }
        }


        //最大范围
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mStickCenter.x,mStickCenter.y,mMaxRange,paint);
        paint.setStyle(Paint.Style.FILL);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mOutOfRange=false;
                mDisppear=false;
            case MotionEvent.ACTION_MOVE:

                //更新拖动圆圆心坐标
                mDragCenter.set(event.getX(),event.getY());
                invalidate();

                float distance=GeometryUtil.getDistanceBetween2Points(mDragCenter,mStickCenter);
                if(distance>mMaxRange){
                    mOutOfRange = true;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:

                float distance2=GeometryUtil.getDistanceBetween2Points(mDragCenter,mStickCenter);
                if(distance2>mMaxRange){
                    //最大范围外松开则消失
                    mDisppear = true;
                    invalidate();
                }else {//没有超出最大范围松开
                    if(mOutOfRange){
                        //断开后又放回最大范围内松开
                        mDragCenter.set(mStickCenter.x,mStickCenter.y);
                        invalidate();

                    }else {
                        //没有超出最大范围松开弹回去
                        final PointF star=new PointF(mDragCenter.x,mDragCenter.y);
                        final PointF end=new PointF(mStickCenter.x,mStickCenter.y);

                        final ValueAnimator animator=ValueAnimator.ofInt(1);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                float percent=animator.getAnimatedFraction();
                                //中间点
                                PointF point=GeometryUtil.getPointByPercent(star,end,percent);
                                mDragCenter.set(point.x,point.y);
                                invalidate();
                            }
                        });
                        animator.setDuration(300);
                        animator.setInterpolator(new OvershootInterpolator(4));
                        animator.start();

                    }
                }
                break;
        }

        //按下事件要返回true,否则接下来的Move事件接收不到
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            return true;
        }

        return super.onTouchEvent(event);
    }
}
