package de.droidgames.familiycalendar;



import android.opengl.Matrix;
import javax.vecmath.Vector3f;


/**
 * Created by bandy on 25.01.2018.
 */



public class SpiralParametrization {

 private float[] factors;


    TimeGrid mTimeGrid;

    public SpiralParametrization(TimeGrid Tg ) {

        mTimeGrid = Tg;

    }



    public Vector3f[] GetPoints(int Depth, int numberCirclePoints) {




        float p[][] = new float[2][];
        p[0] = new float[3];
        p[1] = new float[3];
        float pi = (float)Math.PI;

        float[][] tps = mTimeGrid.getmTimePoints();
        Vector3f[] ret = new  Vector3f[(tps[0].length-1)* numberCirclePoints];

            for (int j = 1; j < tps[Depth-1].length; j++) {
                float tmin = tps[Depth-1][j-1];
                float tmax = tps[Depth-1][j];

                for (int k = 0; k < numberCirclePoints; k++) {

                    float x = ( tmin + (tmax-tmin)*(float)k/(float)numberCirclePoints);

                    p[0][0] = (float) (Math.atan(x) * 2.0 / pi);
                    p[0][1] = 0f;
                    p[0][2] = 0f;
                    p[1][0] = (float) (2f / ((float) pi * (1f + x * x)));
                    p[1][1] = 0f;
                    p[1][2] = 0f;
                    float[] ls = new float[3];
                    ls[0] = 0f;
                    ls[1] = 0f;
                    ls[2] = p[1][0] / 2f;

                    SpiralPoint[] spp = new SpiralPoint[Depth+1];
                    spp[0] = new SpiralPoint(null,  new Vector3f(p[0]), new Vector3f(ls),  new Vector3f(p[1])  );

                    for (int l = 1; l < Depth+1; l++) {
                        int  leftIndex =mTimeGrid.GetLeftAddress(x, l );
                        float angle  = 360 * (x-tps[l-1][leftIndex])/(tps[l-1][leftIndex+1]-tps[l-1][leftIndex]);
                        spp[l] = spp[l-1].GetChild(angle);
                    }

                    ret[numberCirclePoints*(j-1) + k] = spp[Depth].getmCoordinates();

                }
        }



        return ret;
    }


}
