package com.etrade.edgepanel.data;

/**
 * Created by arosenzw on 7/13/17.
 */

public class WatchList {
    private Stock[] stocks;
    private String name;
    private int activeStock;    // stock selected for reordering

    public WatchList(Stock[] s, String name_in) {
        stocks = s;
        name = name_in;
    }

    public String getName() { return name; }

    public Stock[] getStocks() {
        return stocks;
    }

    public int size() {
        return stocks.length;
    }

    public int getActiveStock() {
        return activeStock;
    }

    public void setActiveStock(int i) {
        activeStock = i;
    }

    public void clearActiveStock() {
        activeStock = -1;
    }

    public Stock getStock(int pos) {
        updateStock(pos);
        return stocks[pos];
    }

    private void updateStock(int pos) {
        stocks[pos].update();
    }

    private void updateStocks() {
        for (Stock s : stocks) {
            s.update();
        }
    }
}
