package com.etrade.edgepanel.edgedisplay;

import com.etrade.edgepanel.R;
import com.etrade.edgepanel.data.Stock;
import com.etrade.edgepanel.data.WatchListManager;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailProvider;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

public class CocktailProvider extends SlookCocktailProvider {

    @Override
    public void onUpdate(Context context, SlookCocktailManager cocktailManager, int[] cocktailIds) {
        panelUpdate(context, cocktailManager, cocktailIds);
    }

    public void panelUpdate(Context context, SlookCocktailManager manager, int[] cocktailIds) {
        int layoutId = R.layout.main_view;
        RemoteViews rv = new RemoteViews(context.getPackageName(), layoutId);

        Stock[] stocks = new WatchListManager().getStocksAsArray();
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

        //keep this
        if (cocktailIds != null) {
            for (int id : cocktailIds) {
                manager.updateCocktail(id, rv);
            }
        }
    }

}
