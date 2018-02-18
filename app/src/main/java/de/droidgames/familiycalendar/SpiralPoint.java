package de.droidgames.familiycalendar;

import android.opengl.Matrix;

import javax.vecmath.Vector3f;

/**
 * Created by bandy on 14.02.2018.
 */

public class SpiralPoint {

    private Vector3f mNewStep;
    private Vector3f mVelocity;
    private Vector3f mCoordinates;
    private SpiralPoint mParent;



    public SpiralPoint getmParent() {
        return mParent;
    }

    public Vector3f getmNewStep() {
        return mNewStep;
    }

    public Vector3f getmVelocity() {
        return mVelocity;
    }

    public Vector3f getmCoordinates()
    {
        return mCoordinates;
    }


    public SpiralPoint( SpiralPoint Parent, Vector3f Coordinates, Vector3f NewStep, Vector3f Velocity)
    {
        mParent = Parent;
        mNewStep = NewStep;
        mCoordinates = Coordinates;
        mVelocity = Velocity;
    }

    public SpiralPoint GetChild( float angle, float factor )
    {
        float pi = (float)Math.PI;
        Vector3f axis = new Vector3f(mVelocity);
        float  vel = axis.length();
        axis.normalize();
        float[] rotM = new float[16];
        Matrix.setRotateM(rotM,0, angle,axis.getX(),axis.getY(), axis.getZ());
        float[] nv = new float[4];
        Vector3f newStep = new Vector3f();
        float[] ov = new float[4];
        nv[0] = mNewStep.getX();
        nv[1] = mNewStep.getY();
        nv[2] = mNewStep.getZ();
        float l =  mNewStep.length();
        Matrix.multiplyMV(ov,0,rotM,0,nv,0);
        newStep.set(ov[0],ov[1],ov[2]);
        float scale = 2f * pi /factor;
        if (scale> 1f/2f)
        {
            scale = 1f/2f;
        }

        newStep.scale(scale);
        Vector3f newVelocity = new Vector3f();
        newVelocity.cross(newStep,axis);
        newVelocity.scale(2f * (float)Math.PI);
        Vector3f newPoint = new Vector3f(mCoordinates);
        newPoint.add(newStep);
        return new SpiralPoint(this, newPoint, newStep, newVelocity );

    }

}
