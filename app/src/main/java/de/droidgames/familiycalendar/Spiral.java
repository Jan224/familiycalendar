package de.droidgames.familiycalendar;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.vecmath.Vector3f;

import static java.nio.ByteBuffer.*;
import static java.nio.ByteOrder.*;

/**
 * Created by bandy on 29.01.2018.
 */

public class Spiral {

    private int mPositionHandle;
    private int mColorHandle;


    private int vertexCount;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex


    private final int mProgram;

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";
    // Use to access and set the view transformation
    private int mMVPMatrixHandle;


    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    private FloatBuffer vertexBuffer;
    //private ShortBuffer drawListBuffer;


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    float spiralCoords[];

    // Set color with red, green, blue and alpha (opacity) values
    float color[];// = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    private float[] GetFloatFromVecs(Vector3f[] Vecs) {
        int n = Vecs.length * 3;
        float[] ret = new float[n];

        for (int i = 0; i < Vecs.length; i++) {
            ret[3 * i] = Vecs[i].getX();
            ret[3 * i + 1] = Vecs[i].getY();
            ret[3 * i + 2] = Vecs[i].getZ();
        }
        return ret;
    }
    private float[] AppendFloat(float[] f1, float[] f2) {
        int n = 0;
        if (f1 != null) n = f1.length;
        int m = 0;
        if (f2 != null) m = f2.length;
        float[] ret = new float[n+m];


        for (int i = 0; i <n; i++) {
            ret[i] = f1[i];
        }
        for (int i = 0; i <m; i++) {
            ret[i+n] = f2[i];
        }
        return ret;
    }


    public Spiral(float[] Color, int sdep, int dep  ) {



        int dim = 2;
        color = Color;


        int numberPoints = 10;
        int numberCirclePoints = 12;
        //int dep = 4;
        //int sdep = 6;
        TimeGrid tg = new TimeGrid(sdep, dep,  numberPoints);
        SpiralParametrization st = new SpiralParametrization(tg);


      //  for (int i = 1; i< dep+1;i++) {
            Vector3f[] newVecs = st.GetPoints(dep, numberCirclePoints);
//            float[] newFloats = GetFloatFromVecs(newVecs);
//            float[] bu = AppendFloat(spiralCoords, newFloats);
//            spiralCoords = bu;
            spiralCoords =  GetFloatFromVecs(newVecs);
            vertexCount = spiralCoords.length / COORDS_PER_VERTEX;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = allocateDirect(
                spiralCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();


        // add the coordinates to the FloatBuffer
        vertexBuffer.put(spiralCoords);






//        vertexCount = spiralCoords.length / COORDS_PER_VERTEX;
//        // initialize vertex byte buffer for shape coordinates
//        ByteBuffer bb = allocateDirect(
//                spiralCoords.length * 4);
//        // use the device hardware's native byte order
//        bb.order(nativeOrder());
//
//        // create a floating point buffer from the ByteBuffer
//        vertexBuffer = bb.asFloatBuffer();
//
//
//        // add the coordinates to the FloatBuffer
//        vertexBuffer.put(spiralCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);

    }


    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);


        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertexCount);


        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

}
