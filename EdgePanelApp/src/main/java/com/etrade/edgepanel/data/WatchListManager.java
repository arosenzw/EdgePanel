package com.etrade.edgepanel.data;

/**
 * Created by dpowell1 on 7/10/17.
 */

public class WatchListManager {
    private WatchList[] watch_lists;
    private int active;

    private WatchListManager(WatchList[] watch_lists) {
        this.watch_lists = watch_lists;
        this.active = 0; //show first watch list in array
    }

    public WatchList getActiveWatchList() {
        return watch_lists[active];
    }

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

        Stock[] stocks1 = {s, s1, s2, s, s1, s2, s, s1, s2, s, s1, s2};
        Stock[] stocks2 = {s3, s4, s5, s6};

        WatchList w = new WatchList(stocks1);
        WatchList w2 = new WatchList(stocks2);

        WatchList[] lists = {w, w2};

        return new WatchListManager(lists);
    }

    private static class WatchListManagerSingleton {
        private static final WatchListManager instance = WatchListManager.getTestWatchListManager();
    }

}