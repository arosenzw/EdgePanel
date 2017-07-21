package com.etrade.edgepanel.data;

/**
 * Created by dpowell1 on 7/10/17.
 */

public class WatchListManager {
    private WatchList[] watch_lists;
    private int active;
    private int clicked; // for reordering
    public static boolean isReorderingStocks = false;
    public static boolean isReorderingWls = false;

    private WatchListManager(WatchList[] watch_lists) {
        this.watch_lists = watch_lists;
        this.active = 0; //show first watch list in array
    }

    public WatchList[] getWatchListArray() { return watch_lists; }

    public WatchList getActiveWatchList() {
        return watch_lists[active];
    }

    public int getActive() { return active; }

    public int getClicked() { return clicked; }

    public void setClicked(int watchList) { clicked = watchList; }

    public void clearClicked() { clicked = -1; }

    public int size() {
        return this.watch_lists.length;
    }

    public void setActive(int i) {
        active = i;
    }

    public static WatchListManager getInstance() {
        return WatchListManagerSingleton.instance;
    }

    private static WatchListManager getTestWatchListManager() {
        // Sample stocks
        Stock s = new Stock("AAPL", "Apple", 151.99, 2.56, 1.07);
        Stock s1 = new Stock("FB", "Facebook", 151.99, -2.56, -1.07);
        Stock s2 = new Stock("MSFT", "Microsoft", 151.99, 2.56, 1.07);
        Stock s3 = new Stock("NFLX", "Netflix", 151.99, 0.00, 0.00);
        Stock s4 = new Stock("GOOGL", "Alphabet", 151.99, 5.00, 2.00);
        Stock s5 = new Stock("CSCO", "Cisco", 100.00, -2.00, -5.00);
        Stock s6 = new Stock("TSLA", "Tesla", 500.00, 3.15, 2.05);
        Stock s7 = new Stock("BAC", "Bank of America", 23.92, -0.14, -0.58);
        Stock s8 = new Stock("GE", "General Electric", 26.79, -0.15, -0.56);
        Stock s9 = new Stock("F", "Ford Motor", 11.71, 0.03, 0.21);
        Stock s10 = new Stock("TWTR", "Twitter", 20.60, 0.48, 2.39);
        Stock s11 = new Stock("BABA", "Alibaba", 152.63, -0.52, -0.34);
        Stock s12 = new Stock("SNAP", "Snapchat", 14.79, -0.18, -1.20);
        Stock s13 = new Stock("GPRO", "GoPro", 8.10, 0.00, 0.00);
        Stock s14 = new Stock("DIS", "Disney", 107.56, 0.73, 0.68);
        Stock s15 = new Stock("SBUX", "Starbucks", 58.08, 0.01, 0.02);
        Stock s16 = new Stock("FIT", "Fitbit", 5.81, 0.0402, 0.70);




        Stock[] stocks1 = {s, s1, s2};
        Stock[] stocks2 = {s3, s4, s5, s6, s14, s15, s16};
        Stock[] stocks3 = {s4, s5, s6, s7, s8, s9, s10};
        Stock[] stocks4 = {s11, s12, s13};

        WatchList w = new WatchList(stocks1, "Energy");
        WatchList w2 = new WatchList(stocks2, "BioTech");
        WatchList w3 = new WatchList(stocks3, "Long Term");
        WatchList w4 = new WatchList(stocks4, "Options");

        WatchList[] lists = {w, w2, w3, w4};

        return new WatchListManager(lists);
    }
    public void swapWatchList(int watchList, int direction) {
        if (watchList == 0 && direction < 0) {
            return;
        } else if (watchList == watch_lists.length-1 && direction > 0) {
            return;
        }

        if (direction > 0) {
            direction = 1;
        } else if (direction < 0) {
            direction = -1;
        } else {
            return;
        }

        WatchList w = watch_lists[watchList];
        watch_lists[watchList] = watch_lists[watchList + direction];
        watch_lists[watchList + direction] = w;
        setClicked(watchList+direction);
    }

    private static class WatchListManagerSingleton {
        private static final WatchListManager instance = WatchListManager.getTestWatchListManager();
    }

}