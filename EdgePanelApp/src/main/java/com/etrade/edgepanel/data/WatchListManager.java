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
        Stock s1 = new Stock("FB");
        Stock s2 = new Stock("MSFT");
        Stock s4 = new Stock("GOOGL");
        Stock s12 = new Stock("SNAP");
        Stock s3 = new Stock("NFLX");

        Stock s = new Stock("aapl");
        Stock s6 = new Stock("TSLA");
        Stock s11 = new Stock("BABA");
        Stock s17 = new Stock("AMZN");

        Stock s5 = new Stock("CSCO");
        Stock s7 = new Stock("BAC");
        Stock s8 = new Stock("GE");
        Stock s9 = new Stock("F");
        Stock s10 = new Stock("TWTR");
        Stock s13 = new Stock("GPRO");
        Stock s14 = new Stock("DIS");
        Stock s15 = new Stock("SBUX");
        Stock s16 = new Stock("FIT");

        Stock[] stocks1 = {s, s1, s2, s3, s4, s12, s13};
        Stock[] stocks2 = {s, s6, s11, s17};
        Stock[] stocks3 = {s5, s7, s8, s9, s10};
        Stock[] stocks4 = {s14, s15, s16};

        WatchList w = new WatchList(stocks1, "Watch List 1");
        WatchList w2 = new WatchList(stocks2, "Watch List 2");
        WatchList w3 = new WatchList(stocks3, "Watch List 3");
        WatchList w4 = new WatchList(stocks4, "Watch List 4");

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