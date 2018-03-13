package de.droidgames.familiycalendar;


import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;


/**
 * Created by bandy on 29.01.2018.
 */

public class MyGLSurfaceView extends GLSurfaceView {


    private final MyGLRenderer mRenderer;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    private RectF mCurrentViewport =
            new RectF(-200, -200, 200, 200);
    private Rect mContentRect;
    private ScaleGestureDetector mScaleGestureDetector;


    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8,8,8,8,16,0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);

        mRenderer = new MyGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    public void incSDepth() {
        mRenderer.incSDepth();
    }

    public void decSDepth() {
        mRenderer.decSDepth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {

            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1;
                }

                mRenderer.setxAngle(
                        mRenderer.getxAngle() +
                                ((dx) * TOUCH_SCALE_FACTOR));
                mRenderer.setyAngle(
                        mRenderer.getyAngle() +
                                ((dy) * TOUCH_SCALE_FACTOR));


                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }


//    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener
//            = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//        /**
//         * This is the active focal point in terms of the viewport. Could be a local
//         * variable but kept here to minimize per-frame allocations.
//         */
//        private PointF viewportFocus = new PointF();
//        private float lastSpanX;
//        private float lastSpanY;
//
//        // Detects that new pointers are going down.
//        @Override
//        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
//            lastSpanX = ScaleGestureDetectorCompat.getCurrentSpanX(scaleGestureDetector);
//            lastSpanY = ScaleGestureDetectorCompat.getCurrentSpanY(scaleGestureDetector);
//            return true;
//        }
//
//        @Override
//        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
//
//            float spanX = ScaleGestureDetectorCompat.
//                    getCurrentSpanX(scaleGestureDetector);
//            float spanY = ScaleGestureDetectorCompat.
//                    getCurrentSpanY(scaleGestureDetector);
//
//            float newWidth = lastSpanX / spanX * mCurrentViewport.width();
//            float newHeight = lastSpanY / spanY * mCurrentViewport.height();
//
//            float focusX = scaleGestureDetector.getFocusX();
//            float focusY = scaleGestureDetector.getFocusY();
//            // Makes sure that the chart point is within the chart region.
//            // See the sample for the implementation of hitTest().
//            hitTest(scaleGestureDetector.getFocusX(),
//                    scaleGestureDetector.getFocusY(),
//                    viewportFocus);
//
//            mCurrentViewport.set(
//                    viewportFocus.x
//                            - newWidth * (focusX - mContentRect.left)
//                            / mContentRect.width(),
//                    viewportFocus.y
//                            - newHeight * (mContentRect.bottom - focusY)
//                            / mContentRect.height(),
//                    0,
//                    0);
//            mCurrentViewport.right = mCurrentViewport.left + newWidth;
//            mCurrentViewport.bottom = mCurrentViewport.top + newHeight;
//
//            // Invalidates the View to update the display.
//            ViewCompat.postInvalidateOnAnimation(InteractiveLineGraphView.this);
//
//            lastSpanX = spanX;
//            lastSpanY = spanY;
//            return true;
//        }
//    };

}