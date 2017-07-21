package com.etrade.edgepanel.edgedisplay;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.etrade.edgepanel.EdgeActions;
import com.etrade.edgepanel.R;
import com.etrade.edgepanel.data.WatchListManager;

import java.util.HashMap;

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
        setSettingsMenu(context);
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
        menuView.setOnClickPendingIntent(R.id.settings_button,
            getPendingSelfIntent(context, EdgeActions.TOGGLE_SETTINGS));


        // Add current date
        menuView.setTextViewText(R.id.update_date, WidgetProvider.getDate());
        // Set available watch lists in menu
        for (int i = 0; i < watchListManager.size(); i++) {
            RemoteViews watchListEntry = new RemoteViews(context.getPackageName(), R.layout.watch_list_entry);
            String text = watchListManager.getWatchListArray()[i].getName();
            watchListEntry.setTextViewText(R.id.watch_list_button, text);
            menuView.addView(R.id.lists, watchListEntry);

            if(watchListManager.isReorderingWls) {
                watchListEntry.setOnClickPendingIntent(R.id.watch_list_button, getPendingSelfIntent(context, EdgeActions.SELECT_WL.toString() + ":" + Integer.toString(i)));
                if (watchListManager.getClicked() == i) {
                    watchListEntry.setInt(R.id.watch_list_button, "setBackgroundResource", R.drawable.wl_border);
                    watchListManager.setActive(i);
                }
            } else {
                watchListEntry.setOnClickPendingIntent(R.id.watch_list_button, getPendingSelfIntent(context,
                        EdgeActions.SET_ACTIVE_WATCH_LIST.toString() + ":" + Integer.toString(i)));
            }
        }
    }

    /**
     * Display a settings view when the settings display is enabled
     *
     * @param context
     */
    private void setSettingsMenu(Context context) {
        if (!displaySettings) {
            cancelSettings(context);
            return;
        }
        // else
        menuView.setViewVisibility(R.id.settings_background, View.VISIBLE);

        menuView.setOnClickPendingIntent(R.id.reorder_stocks, getPendingSelfIntent(context, EdgeActions.REORDER_STOCKS));
        menuView.setOnClickPendingIntent(R.id.reorder_watch_lists, getPendingSelfIntent(context, EdgeActions.REORDER_WLS));

        // Set background to close settings menu upon click

        menuView.setOnClickPendingIntent(R.id.settings_background, getPendingSelfIntent(context, EdgeActions.TOGGLE_SETTINGS));
        toggleArrowButtons(context, ((watchListManager.getActiveWatchList().getActiveStock() >= 0) || (watchListManager.getClicked()) >= 0));
    }

    /**
     * Toggles the display of arrow buttons when a stock is selected/de-selected
     *
     * @param context
     * @param showArrows
     */
    private void toggleArrowButtons(Context context, boolean showArrows) {
        if (showArrows) {
            menuView.setViewVisibility(R.id.reorder_buttons, View.INVISIBLE);
            menuView.setViewVisibility(R.id.arrow_buttons, View.VISIBLE);
            // Add button functionality
            if(watchListManager.isReorderingStocks) {
                menuView.setOnClickPendingIntent(R.id.upBtn, getPendingSelfIntent(context, EdgeActions.SWAP_STOCK_UP));
                menuView.setOnClickPendingIntent(R.id.downBtn, getPendingSelfIntent(context, EdgeActions.SWAP_STOCK_DOWN));
            } else if (watchListManager.isReorderingWls) {
                menuView.setOnClickPendingIntent(R.id.upBtn, getPendingSelfIntent(context, EdgeActions.SWAP_WL_UP));
                menuView.setOnClickPendingIntent(R.id.downBtn, getPendingSelfIntent(context, EdgeActions.SWAP_WL_DOWN));
            }
        } else {
            menuView.setViewVisibility(R.id.reorder_buttons, View.VISIBLE);
            menuView.setViewVisibility(R.id.arrow_buttons, View.INVISIBLE);
        }
    }

    /**
     * Toggles whether or not the settings menu should be displayed
     */
    public void toggleDisplaySettings() {

        displaySettings = !displaySettings;
    }

    /**
     * Clears the settings window and resets visibilities back to normal
     *
     * @param context
     */
    private void cancelSettings(Context context) {
        RemoteViews menuView = new RemoteViews(context.getPackageName(), R.layout.menu_window);
        toggleArrowButtons(context, false);
        menuView.setViewVisibility(R.id.settings_background, View.INVISIBLE);
        // Disable reordering ability
        watchListManager.isReorderingStocks = watchListManager.isReorderingWls = false;
        watchListManager.getActiveWatchList().clearActiveStock();
        watchListManager.clearClicked();
    }

}
