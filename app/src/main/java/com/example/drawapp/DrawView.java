package com.example.drawapp;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class DrawView extends View
{
private Paint mBorderPaint;
    private Paint mBackgroundPaint;
    private Paint mComplexPaint;
    private Path mComplexPath;
    private boolean flag_pen = true;
    private boolean flag_rect = false;
    private boolean flag_gest = false;
    private static final float STROKE_WIDTH = 10f;
    private Figure mFigure ;
    private List<Figure> mFigures = new ArrayList<>(64);
    private Path mPath = new Path();
    private Rect mDrawingBounds;
    private Box mCurrentBox;
    private int mCurrentColor = Color.BLACK;
    private BoxDrawView box = new BoxDrawView(getContext(), null);
    private GestureDetector mDetector;
    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            for (Figure figure : mFigures) {
                for (PointF point : figure.mPoints) {
                    float x = mDrawingBounds.left + (point.x * mDrawingBounds.width());
                    float y = mDrawingBounds.top + (point.y * mDrawingBounds.height());

                    point.x = toRangeX(x - distanceX);
                    point.y = toRangeY(y - distanceY);
                }
            }

            invalidate();
            return true;
        }
    };

    //setters_flags

    public void setFlag_pen(boolean flag_pen)
    {
        this.flag_pen = flag_pen;
    }

    public void setFlag_rect(boolean flag_rect)
    {
        this.flag_rect = flag_rect;
    }

    public void setFlag_gest(boolean flag_gest)
    {
        this.flag_gest = flag_gest;
    }


    //constructors

    public DrawView(Context context)
    {
        super(context);
        initPaints();
    }
    public DrawView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initPaints();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }


    private void initPaints() {
        mBackgroundPaint = new Paint();
        mBorderPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);
        mBorderPaint.setColor(Color.BLACK);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mBorderPaint.setStrokeCap(Paint.Cap.SQUARE);
        mBorderPaint.setStrokeJoin(Paint.Join.BEVEL);
        mDetector = new GestureDetector(getContext(), mGestureListener);
    }

    private float toRangeX(float x) {
        float localX = x - mDrawingBounds.left;
        return localX / mDrawingBounds.width();
    }

    private float toRangeY(float y) {
        float localY = y - mDrawingBounds.top;
        return localY / mDrawingBounds.height();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int size = Math.min(w, h);
        int left = (w - size) / 2;
        int top = (h - size) / 2;

        int oneDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        mDrawingBounds = new Rect(left, top, left + size, top + size);
        mDrawingBounds.inset(oneDp, oneDp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(mDrawingBounds, mBackgroundPaint);
        canvas.drawRect(mDrawingBounds, mBorderPaint);
        canvas.clipRect(mDrawingBounds);
        canvas.translate(mDrawingBounds.left, mDrawingBounds.top);

        for (Figure f : mFigures) {
                if (f.getmPath() != null)
                {f.mPaint.setStyle(Paint.Style.STROKE);
                    f.drawPen(canvas, f);}
                if (f.getmBox() != null)
                {f.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    f.drawRect(canvas, f);}
                if (f.getmPoints()!=null)
                {f.draw(canvas);
                    if (mFigure != null) {
                        mFigure.draw(canvas);
                    }
                }
        }
    }

    public void setmCurrentColor(int mCurrentColor)
    {
        this.mCurrentColor = mCurrentColor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (flag_pen)
        {
            return
                    onTouchPen(event);}
        else if (flag_rect)
        {
            return
                    onTouchRect(event);}
        else if (flag_gest)
            return
                    processPaintingModeEvent(event);
        return true;
    }

    private boolean processPaintingModeEvent(MotionEvent event) {
        PointF currentPoint = null;
        // masked!
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mFigure = new Figure(mCurrentColor);
                currentPoint = mFigure.getPoint(0);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int pointerId = event.getPointerId(event.getActionIndex());
                currentPoint = mFigure.getPoint(pointerId);
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int pId = event.getPointerId(i);
                    mFigure.getPoint(pId).x = toRangeX(event.getX(i));
                    mFigure.getPoint(pId).y = toRangeY(event.getY(i));
                }
                break;
            case MotionEvent.ACTION_UP:
                mFigures.add(mFigure);
                mFigure = null;
                break;
        }

        if (currentPoint != null) {
            currentPoint.x = toRangeX(event.getX(event.getActionIndex()));
            currentPoint.y = toRangeY(event.getY(event.getActionIndex()));
        }
        invalidate();
        return true;
    }

    private boolean onTouchPen(MotionEvent event) {
        mFigure = new Figure(mCurrentColor);
        mFigures.add(mFigure);
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case ACTION_DOWN:
                mPath=new Path();
                mPath.moveTo(eventX, eventY);
                mFigure.setmPath(mPath);
                return true;
            case ACTION_MOVE:
                mPath.lineTo(eventX, eventY);
                invalidate();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private boolean onTouchRect(MotionEvent event) {
        mFigure = new Figure(mCurrentColor);
        mFigures.add(mFigure);
        PointF current = new PointF(event.getX(), event.getY());
        int action = event.getAction();
        switch (action) {
            case ACTION_DOWN:
                mCurrentBox = new Box(current);//левый верхний угол
                mFigure.setmBox(mCurrentBox);
                break;
            case ACTION_MOVE:
                mCurrentBox.setCurrent(current);
                invalidate();//нужно обновить при отрисовке
                break;
            case ACTION_UP:
                break;
            case ACTION_CANCEL:
                mCurrentBox = null;
                break;
        }
        return true;
    }

    public void clear() {
        mPath.reset();
        mFigures.clear();
        invalidate();
    }

 private class Figure extends Drawable {
        private Box mBox=null;
        private Path mPath=null;
        private int mColor;
        private Paint mPaint;
        private List<PointF> mPoints = new ArrayList<>(16);

     protected Figure(int color) {
         mColor = color;
         initPaint();
     }

     private void initPaint() {
         mPaint=new Paint();
         mPaint.setColor(mColor);
         mPaint.setStrokeCap(Paint.Cap.ROUND);
         mPaint.setAntiAlias(true);
         mPaint.setStrokeWidth(STROKE_WIDTH);
         mPaint.setStrokeJoin(Paint.Join.ROUND);
         mComplexPaint = new Paint(mPaint);
         mComplexPaint.setStyle(Paint.Style.FILL);
     }
     public Box getmBox() {
            return mBox;
        }

        public Path getmPath() {
            return mPath;
        }

        public void setmBox(Box mBox) {
            this.mBox = mBox;
        }

        public void setmPath(Path mPath) {
            this.mPath = mPath;
        }

     public List<PointF> getmPoints() {
         return mPoints;
     }

     @Override
        public void draw(@NonNull Canvas canvas) {

            switch (mPoints.size())
            {
                case 1:
                    drawSinglePoint(mPoints.get(0), canvas);
                    break;
                case 2:
                    drawLine(mPoints.get(0), mPoints.get(1), canvas);
                    break;
                default:
                    drawComplexFigure(canvas);
            }

        }
     public PointF getPoint(int index) {
         while (index >= mPoints.size()) {
             mPoints.add(new PointF());
         }

         return mPoints.get(index);
     }
        @Override
        public void setAlpha(int alpha) {

        }
        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }

     private void drawSinglePoint(PointF point, Canvas canvas)
     {
         float size = Math.min(canvas.getWidth(), canvas.getHeight());
         float x = point.x * size;
         float y = point.y * size;
         canvas.drawPoint(x, y, mPaint);
     }

     private void drawLine(PointF one, PointF two, Canvas canvas)
     {
         float size = Math.min(canvas.getWidth(), canvas.getHeight());
         canvas.drawLine(one.x * size, one.y * size, two.x * size, two.y * size, mPaint);
     }

     private void drawComplexFigure(Canvas canvas) {

         if (mComplexPath == null) {
             mComplexPath = new Path();
         }

         mComplexPath.reset();

         float size = Math.min(canvas.getWidth(), canvas.getHeight());

         for (PointF point : mPoints) {
             if (mComplexPath.isEmpty()) {
                 mComplexPath.moveTo(point.x * size, point.y * size);
             } else {
                 mComplexPath.lineTo(point.x * size, point.y * size);
             }
         }

         mComplexPath.close();
         canvas.drawPath(mComplexPath, mComplexPaint);
     }
     private void drawRect(Canvas canvas, Figure f)
     {
         Box box = f.getmBox();
         float left = Math.min(box.getOrigin().x, box.getCurrent().x);
         float right = Math.max(box.getOrigin().x, box.getCurrent().x);
         float top = Math.max(box.getOrigin().y, box.getCurrent().y);
         float bottom = Math.min(box.getOrigin().y, box.getCurrent().y);
         canvas.drawRect(left, top, right, bottom, mPaint);
     }
     private void drawPen(Canvas canvas,Figure f) {
         canvas.drawPath(f.getmPath(), mPaint);
     }
    }
}
