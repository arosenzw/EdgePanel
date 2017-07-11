package com.etrade.edgepanel.data;

import java.util.ArrayList;

/**
 * Created by dpowell1 on 7/10/17.
 */

public class WatchListManager {
    private Stock testStock1;
    private Stock testStock2;
    private ArrayList<Stock> stocks;

    public WatchListManager() {
        testStock1 = new Stock("Apple", 5);
        testStock2 = new Stock("Microsoft", 10);
        stocks = new ArrayList<>();
        stocks.add(testStock1);
        stocks.add(testStock2);
    }

    public Stock[] getStocksAsArray() {
        return (Stock[]) stocks.toArray(new Stock[stocks.size()]);
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

}
