package de.droidgames.familiycalendar;

/**
 * Created by bandy on 16.02.2018.
 */

public class TimeGrid {

    private float[][] mTimePoints;
    int[] factors;
    //private int[][][] mPointsDepth;

    public float[][] getmTimePoints() {
        return mTimePoints;
    }



    public TimeGrid(int  StartDepth, int  Depth, int numberPoints) {
        factors = new int[9];
        factors[0] = 1000;
        factors[1] = 1000;
        factors[2] = 10;
        factors[3] = 10;
        factors[4] = 10;
        factors[5] = 12;
        factors[6] = 4;
        factors[7] = 7;
        factors[8] = 24;

        mTimePoints = new float[Depth][];

        float stepsize= 100f;
        for (int i = StartDepth; i < StartDepth+ Depth; i++) {
            stepsize *= 1f/(float)factors[i];
            mTimePoints[i-StartDepth] = new float[numberPoints];
            for (int j = 0; j < numberPoints; j++) {

                mTimePoints[i-StartDepth][j] = stepsize* ((float)j-(float)numberPoints/2f);
            }
        }

    }


        public int GetLeftAddress(float t, int Depth){
           // int[] ret = new int[3];
            for (int i = 0; i < mTimePoints[Depth-1].length;i++)
                 {
                     if(mTimePoints[Depth-1][i] >t)
                     {
                         return i-1;
                     }



                 }

            return  mTimePoints[Depth-1].length;

        }








}
