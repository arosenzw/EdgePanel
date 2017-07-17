package com.etrade.edgepanel.data;

import java.util.ArrayList;

/**
 * Created by arosenzw on 7/13/17.
 */

public class WatchList {
    private Stock[] stocks;

    /*public Stock[] getStocksAsArray() {
        return (Stock[]) stocks.toArray(new Stock[stocks.size()]);
    }*/

    public WatchList(Stock[] s) { stocks = s; }

    public Stock[] getStocks() {
        return stocks;
    }

    /*public void deleteStock() {
        stocks.remove(0); //keep microsoft
    }*/

    /*public void insert_stock(Stock s) {
        stocks.add(s);
    }*/

}
