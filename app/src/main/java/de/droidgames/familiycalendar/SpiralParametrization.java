package de.droidgames.familiycalendar;



import javax.vecmath.Vector3f;


/**
 * Created by bandy on 25.01.2018.
 */


class SpiralParametrization {

 private float[] factors;


    private TimeGrid mTimeGrid;

    public SpiralParametrization(TimeGrid Tg ) {

        mTimeGrid = Tg;

    }



    public Vector3f[] GetPoints(int Depth, int numberCirclePoints) {




        float p[][] = new float[2][];
        p[0] = new float[3];
        p[1] = new float[3];
        float pi = (float)Math.PI;
        float[][] tps = mTimeGrid.getmTimePoints();
        float scale = (tps[0][tps[0].length-1]- tps[0][0])/tps[0].length;


        Vector3f[] ret = new  Vector3f[(tps[Depth-1].length-1)* numberCirclePoints];

            for (int j = 1; j < tps[Depth-1].length; j++) {
                float tmin = tps[Depth-1][j-1];
                float tmax = tps[Depth-1][j];

                for (int k = 0; k < numberCirclePoints; k++) {

                    float t = ( tmin + (tmax-tmin)*(float)k/(float)numberCirclePoints);
                    float x = t/scale;
                    p[0][0] = (float) (Math.atan(x) * 2.0 / pi);
                    p[0][1] = 0f;
                    p[0][2] = 0f;
                    p[1][0] = 2f / (pi * (1f + x * x));
                    p[1][1] = 0f;
                    p[1][2] = 0f;
                    float[] ls = new float[3];
                    ls[0] = 0f;
                    ls[1] = 0f;
                    ls[2] = p[1][0] / 1f;

                    SpiralPoint[] spp = new SpiralPoint[Depth+1];
                    spp[0] = new SpiralPoint(null,  new Vector3f(p[0]), new Vector3f(ls),  new Vector3f(p[1])  );

                    for (int l = 1; l < Depth+1; l++) {
                        int  leftIndex =mTimeGrid.GetLeftAddress(t,l );
                        float angle  = 360 * (t-tps[l-1][leftIndex])/(tps[l-1][leftIndex+1]-tps[l-1][leftIndex]);


                        spp[l] = spp[l-1].GetChild(angle,  mTimeGrid.getmFactors()[mTimeGrid.mStartDepth+l]);
                    }

                    //(tps[l-1][leftIndex+1]-tps[l-1][leftIndex])
                    ret[numberCirclePoints*(j-1) + k] = spp[Depth].getmCoordinates();

                }
        }



        return ret;
    }

    //public Vector3f[] GetTimeIntervalTriangleVertexes(int Depth, float tmin, float tmax) {
    //}



    }
