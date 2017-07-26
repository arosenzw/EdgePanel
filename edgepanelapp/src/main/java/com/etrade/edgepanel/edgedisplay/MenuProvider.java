package com.etrade.edgepanel.edgedisplay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;

import com.etrade.edgepanel.EdgeActions;
import com.etrade.edgepanel.R;
import com.etrade.edgepanel.data.WatchListManager;

import java.util.HashMap;

import static android.graphics.Color.rgb;
import static com.etrade.edgepanel.edgedisplay.WidgetProvider.getPendingSelfIntent;

/**
 * Created by dpowell1 on 7/20/17.
 */

public class MenuProvider {
    private static RemoteViews menuView;
    private static WatchListManager watchListManager;
    public static boolean displaySettings = false;

    /**
     * Updates the left-hand side menu panel containing watch lists and buttons.
     *
     * @param context
     */
    public void updateMenuPanel(Context context, RemoteViews menuView, WatchListManager watchListManager) {
        this.menuView = menuView;
        this.watchListManager = watchListManager;
        setMainMenu(context);
    }

    /**
     * Display the normal menu window with the list of watch lists and settings button.
     *
     * @param context
     */
    private void setMainMenu(Context context) {
        menuView.removeAllViews(R.id.lists);     // clear menu before redrawing
        // Set button functionalities
        menuView.setOnClickPendingIntent(R.id.refresh_button, 
            getPendingSelfIntent(context, EdgeActions.REFRESH));
        // Add current date
        menuView.setTextViewText(R.id.update_date, WidgetProvider.getDate());
        // Set available watch lists in menu
        for (int i = 0; i < watchListManager.size(); i++) {
            RemoteViews watchListEntry = new RemoteViews(context.getPackageName(), R.layout.watch_list_entry);
            String text = watchListManager.getWatchListArray()[i].getName();
            watchListEntry.setTextViewText(R.id.watch_list_button, text);
            if(i == watchListManager.getActive()) {
                int color = rgb(114, 188, 212);
                watchListEntry.setTextColor(R.id.watch_list_button, color);
            }
            watchListEntry.setOnClickPendingIntent(R.id.watch_list_button, getPendingSelfIntent(context,
                        EdgeActions.SET_ACTIVE_WATCH_LIST.toString() + ":" + Integer.toString(i)));
            menuView.addView(R.id.lists, watchListEntry);
        }
    }

}
