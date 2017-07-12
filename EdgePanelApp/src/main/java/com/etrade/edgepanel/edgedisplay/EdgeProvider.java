package com.etrade.edgepanel.edgedisplay;

import com.etrade.edgepanel.R;
import com.etrade.edgepanel.data.Stock;
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

public class EdgeProvider extends SlookCocktailProvider {
    private static final String DELETE = "com.etrade.edgepanel.action.DELETE_STOCK";
    private static final String ADD = "com.etrade.edgepanel.action.ADD_STOCK";
    private static final WatchListManager watchListManager = new WatchListManager();
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
        RemoteViews rv = new RemoteViews(context.getPackageName(), MAIN_LAYOUT);

        // Add all stocks in the current watch list
        Stock[] stocks = watchListManager.getStocksAsArray();
        for (int i = 0; i < stocks.length; i++) {
            Log.d("Stock update", "New stock added: " + stocks[i].getName());
            //Create new remote view using the specified layout file
            RemoteViews listEntryLayout = new RemoteViews(context.getPackageName(), R.layout.list_entry);
            //Set the text of the TextView inside the above specified layout file
            listEntryLayout.setTextViewText(R.id.stock_text, stocks[i].getName());
            listEntryLayout.setTextViewText(R.id.stock_filler, Integer.toString(i));
            //Add the new remote view to the parent/containing Layout object
            rv.addView(R.id.main_layout, listEntryLayout);
        }

        // Set button functionalities
        rv.setOnClickPendingIntent(R.id.del_button, getPendingSelfIntent(context, DELETE));
        rv.setOnClickPendingIntent(R.id.add_button, getPendingSelfIntent(context, ADD));

        // Standard updating
        if (cocktailIds != null) {
            for (int id : cocktailIds) {
                mgr.updateCocktail(id, rv);
            }
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, EdgeProvider.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("onReceive: ", intent.getAction());

        switch(intent.getAction()) {
            case DELETE:
                Toast.makeText(context, "DELETE", Toast.LENGTH_SHORT).show();
                watchListManager.deleteStock();
                updateEdge(context);
                break;
            case ADD:
                Toast.makeText(context, "ADD", Toast.LENGTH_SHORT).show();
                watchListManager.addStock();
                updateEdge(context);
                break;
            default:
                break;
        }
    }

}
