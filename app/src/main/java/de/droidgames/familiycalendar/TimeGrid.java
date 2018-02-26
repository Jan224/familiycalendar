package de.droidgames.familiycalendar;

/**
 * Created by bandy on 16.02.2018.
 */

class TimeGrid {

    private float[][] mTimePoints;
    private int[] mFactors;
    int mStartDepth;
    //private int[][][] mPointsDepth;

    public float[][] getmTimePoints() {
        return mTimePoints;
    }

    public int[] getmFactors() {
        return mFactors;
    }

    public int getmStartDepth() {
        return mStartDepth;
    }

    public TimeGrid(int  StartDepth, int  Depth, int numberPoints) {
        mFactors = new int[9];
        mFactors[0] = 1000;
        mFactors[1] = 1000;
        mFactors[2] = 10;
        mFactors[3] = 10;
        mFactors[4] = 10;
        mFactors[5] = 12;
        mFactors[6] = 4;
        mFactors[7] = 7;
        mFactors[8] = 24;
        mStartDepth = StartDepth;

        mTimePoints = new float[Depth][];

        float stepsize= 100f;
        for (int i = StartDepth; i < StartDepth+ Depth; i++) {
            stepsize *= 1f/(float)mFactors[i];
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
