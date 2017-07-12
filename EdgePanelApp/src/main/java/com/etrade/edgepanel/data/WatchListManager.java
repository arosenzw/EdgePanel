package com.etrade.edgepanel.data;

import java.util.ArrayList;

/**
 * Created by dpowell1 on 7/10/17.
 */

public class WatchListManager {
    private ArrayList<Stock> stocks;

    public Stock[] getStocksAsArray() {
        return (Stock[]) stocks.toArray(new Stock[stocks.size()]);
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public void deleteStock() {
        stocks.remove(0); //keep microsoft
    }

    public void insert_stock(Stock s) {
        stocks.add(s);
    }

}