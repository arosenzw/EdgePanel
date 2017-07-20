package com.etrade.edgepanel.edgedisplay;

import com.etrade.edgepanel.R;
import com.etrade.edgepanel.data.WatchList;
import com.etrade.edgepanel.data.WatchListManager;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailProvider;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class EdgeProvider extends SlookCocktailProvider {
    private static final String ACTION_HEADER = "com.etrade.edgepanel.action.";
    private static final String REFRESH = ACTION_HEADER+"REFRESH";
    private static final String SET_ACTIVE_WATCH_LIST = ACTION_HEADER+"SET_ACTIVE_WATCH_LIST";
    private static final String TOGGLE_SETTINGS = ACTION_HEADER+"TOGGLE_SETTINGS";
    private static final String REORDER_STOCKS = ACTION_HEADER+"REORDER_STOCKS";
    private static final String REORDER_WLS = ACTION_HEADER+"REORDER_WLS";
    private static final String SELECT_STOCK = ACTION_HEADER+"SELECT_STOCK";
    private static final String SWAP_STOCK_UP = ACTION_HEADER+"SWAP_STOCK_UP";
    private static final String SWAP_STOCK_DOWN = ACTION_HEADER+"SWAP_STOCK_DOWN";
    private static WatchListManager watchListManager = WatchListManager.getInstance();
    private static RemoteViews edgeView;
    private static RemoteViews menuView;
    public static boolean displaySettings = false;

    @Override
    public void onUpdate(Context context, SlookCocktailManager cocktailManager, int[] cocktailIds) {
        updateEdge(context);
    }

    /**
     * Updates the edge screen with views to display and functionality for buttons
     *
     * @param context
     */
    private void updateEdge(Context context) {
        SlookCocktailManager mgr = SlookCocktailManager.getInstance(context);
        int[] cocktailIds = mgr.getCocktailIds(new ComponentName(context, EdgeProvider.class));
        // Right-hand side "panel view" window layout
        edgeView = new RemoteViews(context.getPackageName(), R.layout.stock_list_layout);
        // Left-hand side "help view" window layout
        menuView = new RemoteViews(context.getPackageName(), R.layout.menu_window);

        updateMenuPanel(context);
        updateEdgePanel(context);

        // Update all widget items, including both the edge content and menu content
        if (cocktailIds != null) {
            for (int id : cocktailIds) {
                mgr.updateCocktail(id, edgeView, menuView);
            }
        }
    }

    /**
     * Updates the left-hand side menu panel containing watch lists and buttons.
     *
     * @param context
     */
    private void updateMenuPanel(Context context) {
        setMainMenu(context);
        setSettingsMenu(context);
    }

    /**
     * Display the normal menu window with the list of watch lists and settings button.
     *
     * @param context
     */
    private void setMainMenu(Context context) {
        // Set button functionalities
        menuView.setOnClickPendingIntent(R.id.refresh_button, getPendingSelfIntent(context, REFRESH));
        menuView.setOnClickPendingIntent(R.id.settings_button, getPendingSelfIntent(context, TOGGLE_SETTINGS));

        // Add current date
        menuView.setTextViewText(R.id.update_date, getDate());

        // Set available watch lists in menu
        for (int i = 0; i < watchListManager.size(); i++) {
            RemoteViews watchListEntry = new RemoteViews(context.getPackageName(), R.layout.watch_list_entry);
            String text = "Watch List " + Integer.toString(i + 1);
            watchListEntry.setTextViewText(R.id.watch_list_button, text);
            menuView.addView(R.id.lists, watchListEntry);
            watchListEntry.setOnClickPendingIntent(
                    R.id.watch_list_button,
                    getPendingSelfIntent(context, SET_ACTIVE_WATCH_LIST + ":" + Integer.toString(i))
            );
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

        menuView.setOnClickPendingIntent(R.id.reorder_stocks, getPendingSelfIntent(context, REORDER_STOCKS));
        menuView.setOnClickPendingIntent(R.id.reorder_watch_lists, getPendingSelfIntent(context, REORDER_WLS));
        // Set background to close settings menu upon click
        menuView.setOnClickPendingIntent(R.id.settings_background, getPendingSelfIntent(context, TOGGLE_SETTINGS));
        toggleArrowButtons(context, (watchListManager.getActiveWatchList().getActiveStock() >= 0));
    }

    private void toggleArrowButtons(Context context, boolean showArrows) {
        if (showArrows) {
            menuView.setViewVisibility(R.id.reorder_buttons, View.INVISIBLE);
            menuView.setViewVisibility(R.id.arrow_buttons, View.VISIBLE);
            // Add button functionality
            menuView.setOnClickPendingIntent(R.id.upBtn, getPendingSelfIntent(context, SWAP_STOCK_UP));
            menuView.setOnClickPendingIntent(R.id.downBtn, getPendingSelfIntent(context, SWAP_STOCK_DOWN));
        } else {
            menuView.setViewVisibility(R.id.reorder_buttons, View.VISIBLE);
            menuView.setViewVisibility(R.id.arrow_buttons, View.INVISIBLE);
        }
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
    }

    /**
     * Updates the right-hand side edge panel containing stock information of a given watch list.
     *
     * @param context
     */
    private void updateEdgePanel(Context context) {
        // ListView
        Intent lvIntent = new Intent(context, StockListService.class);
        edgeView.setRemoteAdapter(R.id.stock_list, lvIntent);
    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("M/d h:mm a");
        String strDate = "Updated ";
        strDate += sdf.format(calendar.getTime());
        return strDate;
    }

    public static PendingIntent getPendingSelfIntent(Context context, String action) {
        return getPendingSelfIntent(context, action, null);
    }

    /**
     * Gets a {@code PendingIntent} object that is designed to target this class (self)
     *
     * @param context
     * @param action
     *          Action to be executed
     * @param extras
     *          Extra key-values to be added to the intent
     * @return
     */
    public static PendingIntent getPendingSelfIntent(Context context, String action, HashMap<String, String> extras) {
        Intent intent = new Intent(context, EdgeProvider.class);
        intent.setAction(action);
        if (extras != null) {
            for (String key : extras.keySet()) {
                intent.putExtra(key, extras.get(key));
            }
        }
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    /**
     * Filter broadcasts for a specific button's functionality
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("onReceive: ", intent.getAction());

        String action = intent.getAction();
        if (action.equals(REFRESH)) {
        } else if (action.contains(SET_ACTIVE_WATCH_LIST)) {   //action = SET_ACTIVE + : + buttonNum
            // Get correct button
            int newActiveWatchList = Integer.parseInt(action.split(":")[1]);
            watchListManager.setActive(newActiveWatchList);
        } else if (action.equals(TOGGLE_SETTINGS)) {
            displaySettings = !displaySettings;
        } else if (action.equals(REORDER_STOCKS)) {
            watchListManager.isReorderingStocks = !watchListManager.isReorderingStocks;
            watchListManager.isReorderingWls = false;
        } else if (action.equals(REORDER_WLS)) {
            watchListManager.isReorderingWls = !watchListManager.isReorderingWls;
            watchListManager.isReorderingStocks = false;
            watchListManager.getActiveWatchList().clearActiveStock();
        } else if (action.contains(SELECT_STOCK)) {
            if (watchListManager.isReorderingStocks) {
                int stockNum = Integer.parseInt(action.split(":")[1]);
                WatchList watchList = watchListManager.getActiveWatchList();
                if (stockNum != watchList.getActiveStock()) {
                    watchList.setActiveStock(stockNum);
                } else {
                    watchList.clearActiveStock();
                }
            }
        } else if (action.equals(SWAP_STOCK_UP) || action.equals(SWAP_STOCK_DOWN)) {
            WatchList wl = watchListManager.getActiveWatchList();
            wl.swap(wl.getActiveStock(), (action.equals(SWAP_STOCK_UP) ? -1 : 1));
        }
        updateEdge(context);
    }

    private void displayTextPopup(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}