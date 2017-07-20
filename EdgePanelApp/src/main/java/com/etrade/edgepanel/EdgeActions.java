package com.etrade.edgepanel;

/**
 * Created by dpowell1 on 7/20/17.
 */

public enum EdgeActions {
    REFRESH("REFRESH"),
    SET_ACTIVE_WATCH_LIST("SET_ACTIVE_WATCH_LIST"),
    TOGGLE_SETTINGS("TOGGLE_SETTINGS"),
    REORDER_STOCKS("REORDER_STOCKS"),
    REORDER_WLS("REORDER_WLS"),
    SELECT_STOCK("SELECT_STOCK"),
    SWAP_STOCK_UP("SWAP_STOCK_UP"),
    SWAP_STOCK_DOWN("SWAP_STOCK_DOWN");

    private String action;
    private String prefix = "com.etrade.edgepanel.action.";

    EdgeActions(String action) {
        this.action = prefix+action;
    }

    @Override
    public String toString() {
        return this.action;
    }
}
