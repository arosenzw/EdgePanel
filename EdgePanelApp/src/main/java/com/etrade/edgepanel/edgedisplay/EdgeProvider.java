package com.etrade.edgepanel.edgedisplay;

import com.etrade.edgepanel.R;
import com.etrade.edgepanel.data.Stock;
import com.etrade.edgepanel.data.WatchList;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailProvider;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EdgeProvider extends SlookCocktailProvider {
    private static final String POPUP = "com.etrade.edgepanel.action.POPUP";
    private static final String REFRESH = "com.etrade.edgepanel.action.REFRESH";
    //private static final WatchListManager watchListManager = new WatchListManager();
    private static final int MAIN_LAYOUT = R.layout.main_view;


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
        RemoteViews edgeView = new RemoteViews(context.getPackageName(), MAIN_LAYOUT);
        // Set left-hand side "help view" window layout
        RemoteViews menuView = new RemoteViews(context.getPackageName(), R.layout.menu_window);


        // Set button functionalities
        menuView.setOnClickPendingIntent(R.id.refresh_button, getPendingSelfIntent(context, REFRESH));
        menuView.setTextViewText(R.id.update_date, getDate());

        Stock s = new Stock("AAPL", "Apple", 151.99, 2.56, 1.07);
        Stock s1 = new Stock("FB", "Facebook", 151.99, -2.56, -1.07);
        Stock s2 = new Stock("MSFT", "Microsoft", 151.99, 2.56, 1.07);
        Stock s3 = new Stock("NFLX", "Netflix", 151.99, 0.00, 0.00);
        Stock s4 = new Stock("GOOGL", "Alphabet", 151.99, 5.00, 2.00);
        Stock s5 = new Stock("CSCO", "Cisco", 100.00, -2.00, -5.00);
        Stock s6 = new Stock("TSLA", "Tesla", 500.00, 3.15, 2.05);


        Stock[] stocks1 = {s, s1, s2};
        Stock[] stocks2 = {s3, s4, s5, s6};

        WatchList w = new WatchList(stocks1);
        WatchList w2 = new WatchList(stocks2);

        WatchList[] lists = {w, w2};

        //WatchListManager manager = new WatchListManager(lists);

        for(int j = 0; j < lists.length; ++j) {
            // Add this List to the Menu View
            RemoteViews watchListEntry = new RemoteViews(context.getPackageName(), R.layout.watch_list_entry);
            String listNumber = "Watch List " + Integer.toString(j + 1);
            watchListEntry.setTextViewText(R.id.watch_list_button, listNumber);
            menuView.addView(R.id.lists, watchListEntry);
            // Get the stocks in this list
            Stock[] stocks = lists[j].getStocks();

            // Add stocks to Remote View
            for (int i = 0; i < stocks.length; i++) {
                Log.d("Stock update", "New stock added: " + stocks[i].getTicker());
                //Create new remote view using the specified layout file
                RemoteViews listEntryLayout = new RemoteViews(context.getPackageName(), R.layout.list_entry);
                String change = "";
                String percentage = "(";

                // Set background color to green, red, or gray

                int color = 0;
                if (stocks[i].getPercent_change() > 0.00) {
                    color = android.R.color.holo_green_light;
                    change += "+";
                    percentage += "+";
                } else if (stocks[i].getPercent_change() < 0.00) {
                    color = android.R.color.holo_red_light;
                } else {
                    color = android.R.color.darker_gray;
                }

                listEntryLayout.setInt(R.id.stock_ticker, "setBackgroundResource", color);
                listEntryLayout.setInt(R.id.stock_name, "setBackgroundResource", color);
                listEntryLayout.setInt(R.id.stock_price, "setBackgroundResource", color);
                listEntryLayout.setInt(R.id.stock_change, "setBackgroundResource", color);
                listEntryLayout.setInt(R.id.stock_perc, "setBackgroundResource", color);

                // Set TextView to appropriate stock text
                listEntryLayout.setTextViewText(R.id.stock_ticker, stocks[i].getTicker());
                listEntryLayout.setTextViewText(R.id.stock_name, stocks[i].getName());
                listEntryLayout.setTextViewText(R.id.stock_price, Double.toString(stocks[i].getValue()));
                change += String.format("%.2f", stocks[i].getDollar_change());
                listEntryLayout.setTextViewText(R.id.stock_change, change);
                percentage += String.format("%.2f", stocks[i].getPercent_change());
                percentage += "%)";
                listEntryLayout.setTextViewText(R.id.stock_perc, percentage);

                //Add the new remote view to the parent/containing Layout object
                edgeView.addView(R.id.main_layout, listEntryLayout);

            }
        }


        // Update all widget items, including both the edge content and menu content
        if (cocktailIds != null) {
            for (int id : cocktailIds) {
                mgr.updateCocktail(id, edgeView, menuView);
            }
        }
    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("M/d h:mm a");
        String strDate = "Updated ";
        strDate += sdf.format(calendar.getTime());
        return strDate;
    }

    /**
     * Gets a {@code PendingIntent} object that is designed to target this class (self)
     *
     * @param context
     * @param action
     * @return
     */
    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, EdgeProvider.class);
        intent.setAction(action);
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

        switch(intent.getAction()) {
            case POPUP:
                Toast.makeText(context, "POPUP", Toast.LENGTH_SHORT).show();
                updateEdge(context);
                break;
            case REFRESH:
                Toast.makeText(context, "Refreshing...", Toast.LENGTH_SHORT).show();
                updateEdge(context);
                break;
            default:
                break;
        }
    }
}