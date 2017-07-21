package com.etrade.edgepanel.edgedisplay;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.etrade.edgepanel.EdgeActions;
import com.etrade.edgepanel.R;
import com.etrade.edgepanel.data.Stock;
import com.etrade.edgepanel.data.WatchListManager;

public class StockListService extends RemoteViewsService {
    private WatchListManager watchListManager = WatchListManager.getInstance();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent arg0) {
        return new StockFactory(this.getApplicationContext(), watchListManager);
    }

    private class StockFactory implements RemoteViewsService.RemoteViewsFactory {
        private WatchListManager watchListManager;
        private Context context;
        private final String TAG = StockFactory.class.getSimpleName();

        public StockFactory(Context context, WatchListManager watchListManager) {
            this.context = context;
            this.watchListManager = watchListManager;
        }

        @Override
        public int getCount() {
            return watchListManager.getActiveWatchList().size();
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews stockLayout = new RemoteViews(getPackageName(), R.layout.list_entry);
            Stock stock = watchListManager.getActiveWatchList().getStock(position);

            String change = "";
            String percentage = "(";

            // Set background color to green, red, or gray
            int color = 0;
            if (stock.getPercent_change() > 0.00) {
                color = R.drawable.positive_gradient;
                change += "+";
                percentage += "+";
            } else if (stock.getPercent_change() < 0.00) {
                color = R.drawable.negative_gradient;
            } else {
                color = R.drawable.neutral_gradient;
            }

            stockLayout.setInt(R.id.stock, "setBackgroundResource", color);
            stockLayout.setInt(R.id.stock_border, "setBackgroundResource", color);

            // Set TextView to appropriate stock text
            stockLayout.setTextViewText(R.id.stock_ticker, stock.getTicker());
            stockLayout.setTextViewText(R.id.stock_name, stock.getName());
            stockLayout.setTextViewText(R.id.stock_price, Double.toString(stock.getValue()));
            change += String.format("%.2f", stock.getDollar_change());
            stockLayout.setTextViewText(R.id.stock_change, change);
            percentage += String.format("%.2f", stock.getPercent_change());
            percentage += "%)";
            stockLayout.setTextViewText(R.id.stock_perc, percentage);

            // Set individual item onClick events using fillInIntents
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(EdgeActions.SELECT_STOCK.toString(), position);  //specify which item was clicked
            stockLayout.setOnClickFillInIntent(R.id.stock, fillInIntent);

            // Set border around active stock if reordering
            if (watchListManager.isReorderingStocks) {
                if (position == watchListManager.getActiveWatchList().getActiveStock()) {
                    stockLayout.setInt(R.id.stock_border, "setBackgroundResource", R.color.selected_border);
                }
            }
            return stockLayout;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
        }

    }
}
