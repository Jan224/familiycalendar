package de.droidgames.familiycalendar;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by bandy on 29.01.2018.
 */

class MyGLRenderer implements GLSurfaceView.Renderer {

    private int m_sdep;
    private int dep;
    private int depMin;
    private Spiral[] mSpiral;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] xRotationMatrix = new float[16];
    private float[] yRotationMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private volatile float xAngle;

    public float getxAngle() {
        return xAngle;
    }

    public void setxAngle(float angle) {
        xAngle = angle;
    }

    private volatile float yAngle;

    public float getyAngle() {
        return yAngle;
    }

    public void setyAngle(float angle) {
        yAngle = angle;
    }

    public void incSDepth() {
        m_sdep ++;
        //initSpiral();
    }
    public void decSDepth() {
        m_sdep --;
        //initSpiral();
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        //GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        dep = 4;
        m_sdep = 1;
        depMin = 1;
        mSpiral = new Spiral[dep - depMin + 1];
        initSpiral();
    }

    private void initSpiral() {
        float color[] = {1f, 1f, 1f, 1.0f};
        for (int i = 0; i < dep - depMin + 1; i++) {
            mSpiral[i] = new Spiral(color, m_sdep, i + depMin);
        }
    }

    public void onDrawFrame(GL10 unused) {

        float[] scratch = new float[16];

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, -3, -3, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        long time = SystemClock.uptimeMillis() % 4000L;

        Matrix.setRotateM(xRotationMatrix, 0, xAngle, -1.0f, 0, 0f);

        Matrix.setRotateM(yRotationMatrix, 0, yAngle, 0, -1.0f, 0);
        Matrix.multiplyMM(mRotationMatrix, 0, xRotationMatrix, 0, yRotationMatrix, 0);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        for (Spiral aMSpiral : mSpiral) {
            aMSpiral.draw(scratch);
        }
    }


    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);


    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}