package com.etrade.edgepanel.edgedisplay;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.etrade.edgepanel.EdgeActions;
import com.etrade.edgepanel.R;

/**
 * Created by dpowell1 on 7/21/17.
 */

public class EdgeProvider {

    /**
     * Updates the right-hand side edge panel containing stock information of a given watch list.
     *
     * @param context
     */
    public void updateEdgePanel(Context context, RemoteViews edgeView) {
        // Set up ListView
        Intent populateIntent = new Intent(context, StockListService.class);
        edgeView.setRemoteAdapter(R.id.stock_list, populateIntent);
        // Set up general pending intent template that will capture all touches.
        // StockListService.StockFactory will set individual fill-in-intents that
        // specify what should be done on a per-list-item basis upon receipt of a
        // touch from the general intent below
        edgeView.setPendingIntentTemplate(
                R.id.stock_list,
                WidgetProvider.getPendingSelfIntent(context, EdgeActions.SELECT_STOCK));
    }

}
