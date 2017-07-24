package com.etrade.edgepanel.edgedisplay;

import com.etrade.edgepanel.EdgeActions;
import com.etrade.edgepanel.R;
import com.etrade.edgepanel.data.WatchList;
import com.etrade.edgepanel.data.WatchListManager;
import com.samsung.android.sdk.look.Slook;
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

import static com.etrade.edgepanel.EdgeActions.REFRESH;


public class WidgetProvider extends SlookCocktailProvider {
    private static WatchListManager watchListManager = WatchListManager.getInstance();
    private static RemoteViews edgeView;
    private static RemoteViews menuView;
    private static EdgeProvider edgeProvider = new EdgeProvider();
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

        // Update content of menu and edge panels
        menuProvider.updateMenuPanel(context, menuView, watchListManager);
        edgeProvider.updateEdgePanel(context, edgeView);
        
        // Redraw menu and edge panels
        if (cocktailIds != null) {
            for (int id : cocktailIds) {
                mgr.updateCocktail(id, edgeView, menuView);
            }
        }
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

        if (action.equals(REFRESH.toString())) {
            updateEdge(context);
        } else if (action.contains(EdgeActions.SET_ACTIVE_WATCH_LIST.toString())) {
            // Get correct button
            int newActiveWatchList = Integer.parseInt(action.split(":")[1]);
            if(watchListManager.getActive() != newActiveWatchList) {
                watchListManager.setActive(newActiveWatchList);
                updateEdge(context);
            }
            // else do not update screen

        } else if (action.equals(EdgeActions.TOGGLE_SETTINGS.toString())) {
            menuProvider.toggleDisplaySettings();
            watchListManager.clearClicked();
            watchListManager.isReorderingWls = watchListManager.isReorderingStocks = false;
            updateEdge(context);
        } else if (action.equals(EdgeActions.REORDER_STOCKS.toString())) {
            watchListManager.isReorderingStocks = !watchListManager.isReorderingStocks;
            watchListManager.isReorderingWls = false;
            updateEdge(context);
        } else if (action.equals(EdgeActions.REORDER_WLS.toString())) {
            watchListManager.isReorderingWls = !watchListManager.isReorderingWls;
            watchListManager.isReorderingStocks = false;
            watchListManager.clearClicked();
            updateEdge(context);
        } else if (action.contains(EdgeActions.SELECT_STOCK.toString())) {
            if (watchListManager.isReorderingStocks) {
                int stockNum = intent.getIntExtra(EdgeActions.SELECT_STOCK.toString(), 0);
                WatchList watchList = watchListManager.getActiveWatchList();
                if (stockNum != watchList.getActiveStock()) {
                    watchList.setActiveStock(stockNum);
                } else {
                    watchList.clearActiveStock();
                }
                updateEdge(context);
            }
        } else if (action.contains(EdgeActions.SELECT_WL.toString())) {
            if(watchListManager.isReorderingWls) {
                int watchList = Integer.parseInt(action.split(":")[1]);
                if(watchList != watchListManager.getClicked()) {
                    watchListManager.setClicked(watchList);
                } else {
                    watchListManager.clearClicked();
                }
                updateEdge(context);
            }
        } else if (action.equals(EdgeActions.SWAP_STOCK_UP.toString()) || action.equals(EdgeActions.SWAP_STOCK_DOWN.toString())) {
            WatchList wl = watchListManager.getActiveWatchList();
            wl.swap(wl.getActiveStock(), (action.equals(EdgeActions.SWAP_STOCK_UP.toString()) ? -1 : 1));
            updateEdge(context);
        } else if(action.equals(EdgeActions.SWAP_WL_UP.toString()) || action.equals(EdgeActions.SWAP_WL_DOWN.toString())) {
            watchListManager.swapWatchList(watchListManager.getClicked(), (action.equals(EdgeActions.SWAP_WL_UP.toString()) ? -1 : 1));
            updateEdge(context);
        }
    }

    public static void displayTextPopup(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}