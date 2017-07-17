package com.etrade.edgepanel.data;

import java.util.ArrayList;

/**
 * Created by dpowell1 on 7/10/17.
 */

public class WatchListManager {
    private WatchList[] watch_lists;

    /*public WatchList[] getListsAsArray() {
        return (WatchList[]) watch_lists.toArray(new WatchList[watch_lists.size()]);
    }*/

    public WatchListManager(WatchList[] w) { watch_lists = w; }

    public WatchList[] getLists() {
        return watch_lists;
    }

   /* public void deleteList() {
        watch_lists.remove(0); //keep microsoft
    }*/

    /*public void insert_list(WatchList w) {
        watch_lists.add(w);
    }*/

}