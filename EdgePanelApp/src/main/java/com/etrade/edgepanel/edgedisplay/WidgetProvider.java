package com.etrade.edgepanel.edgedisplay;

import com.etrade.edgepanel.EdgeActions;
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
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class WidgetProvider extends SlookCocktailProvider {
    private static WatchListManager watchListManager = WatchListManager.getInstance();
    private static RemoteViews edgeView;
    private static RemoteViews menuView;
    private static MenuProvider menuProvider = new MenuProvider();
    public static final int EDGE_PANEL_LAYOUT = R.layout.stock_list_layout;
    public static final int MENU_PANEL_LAYOUT = R.layout.menu_window;

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
        int[] cocktailIds = mgr.getCocktailIds(new ComponentName(context, WidgetProvider.class));
        // Right-hand side "panel view" window layout
        edgeView = new RemoteViews(context.getPackageName(), EDGE_PANEL_LAYOUT);
        // Left-hand side "help view" window layout
        menuView = new RemoteViews(context.getPackageName(), MENU_PANEL_LAYOUT);

        menuProvider.updateMenuPanel(context, menuView, watchListManager);
        updateEdgePanel(context);

        // Update all widget items, including both the edge content and menu content
        if (cocktailIds != null) {
            for (int id : cocktailIds) {
                mgr.updateCocktail(id, edgeView, menuView);
            }
        }
    }

    /**
     * Updates the right-hand side edge panel containing stock information of a given watch list.
     *
     * @param context
     */
    private void updateEdgePanel(Context context) {
        // Set up ListView
        Intent populateIntent = new Intent(context, StockListService.class);
        edgeView.setRemoteAdapter(R.id.stock_list, populateIntent);
        // Set up general pending intent template that will capture all touches.
        // StockListService.StockFactory will set individual fill-in-intents that
        // specify what should be done on a per-list-item basis upon receipt of a
        // touch from the general intent below
        edgeView.setPendingIntentTemplate(R.id.stock_list, getPendingSelfIntent(context, EdgeActions.SELECT_STOCK));
    }

    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("M/d h:mm a");
        String strDate = "Updated ";
        strDate += sdf.format(calendar.getTime());
        return strDate;
    }

    public static PendingIntent getPendingSelfIntent(Context context, EdgeActions action) {
        return getPendingSelfIntent(context, action.toString(), null);
    }

    public static PendingIntent getPendingSelfIntent(Context context, String action) {
        return getPendingSelfIntent(context, action, null);
    }

    /**
     * Gets a {@code PendingIntent} object that is designed to target this class (self).
     * This class is responsible for handling all intents and actions.
     *
     * @param context
     * @param action
     *          Action to be executed
     * @param extras
     *          Extra key-values to be added to the intent
     * @return
     */
    public static PendingIntent getPendingSelfIntent(Context context, String action, HashMap<String, String> extras) {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(action);
        if (extras != null) {
            for (String key : extras.keySet()) {
                intent.putExtra(key, extras.get(key));
            }
        }
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Filter broadcasts for a specific action and handle appropriately
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("onReceive: ", intent.getAction());

        String action = intent.getAction();
        if (action.equals(EdgeActions.REFRESH.toString())) {
        } else if (action.contains(EdgeActions.SET_ACTIVE_WATCH_LIST.toString())) {
            // Get correct button
            int newActiveWatchList = Integer.parseInt(action.split(":")[1]);
            watchListManager.setActive(newActiveWatchList);
        } else if (action.equals(EdgeActions.TOGGLE_SETTINGS.toString())) {
            menuProvider.toggleDisplaySettings();
        } else if (action.equals(EdgeActions.REORDER_STOCKS.toString())) {
            watchListManager.isReorderingStocks = !watchListManager.isReorderingStocks;
            watchListManager.isReorderingWls = false;
        } else if (action.equals(EdgeActions.REORDER_WLS.toString())) {
            watchListManager.isReorderingWls = !watchListManager.isReorderingWls;
            watchListManager.isReorderingStocks = false;
            watchListManager.getActiveWatchList().clearActiveStock();
        } else if (action.contains(EdgeActions.SELECT_STOCK.toString())) {
            if (watchListManager.isReorderingStocks) {
                int stockNum = intent.getIntExtra(EdgeActions.SELECT_STOCK.toString(), 0);
                WatchList watchList = watchListManager.getActiveWatchList();
                if (stockNum != watchList.getActiveStock()) {
                    watchList.setActiveStock(stockNum);
                } else {
                    watchList.clearActiveStock();
                }
            }
        } else if (action.equals(EdgeActions.SWAP_STOCK_UP.toString()) || action.equals(EdgeActions.SWAP_STOCK_DOWN.toString())) {
            WatchList wl = watchListManager.getActiveWatchList();
            wl.swap(wl.getActiveStock(), (action.equals(EdgeActions.SWAP_STOCK_UP.toString()) ? -1 : 1));
        }
        updateEdge(context);
    }

    public static void displayTextPopup(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}