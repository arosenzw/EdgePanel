package com.etrade.edgepanel;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.etrade.edgepanel.edgedisplay.WidgetProvider;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;

public class EdgeConfigure extends Activity {

    /**
     * Allows the main app activity update the edge panel
     *
     * @param context
     */
    public void refreshEdgePanel(Context context) {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(EdgeActions.REFRESH.toString());
        SlookCocktailManager mgr = SlookCocktailManager.getInstance(context);
        int[] cocktailIds = mgr.getCocktailIds(new ComponentName(context, WidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, cocktailIds);
        sendBroadcast(intent);
    }

}
