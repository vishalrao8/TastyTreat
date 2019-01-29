package com.unitedcreation.visha.tastytreat.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import static android.util.DisplayMetrics.DENSITY_DEFAULT;

public class DpToPixel {

    /**
     * Utility method to convert dp unit into pixel.
     * @param dp unit to be converted.
     * @param context linked to the resources.
     * @return pixel unit converted from dp via some means of calculation.
     */
    public static int convert (int dp, Context context) {

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / DENSITY_DEFAULT);
    }
}
