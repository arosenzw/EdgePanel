package com.etrade.edgepanel.data;

import java.util.ArrayList;

/**
 * Created by arosenzw on 7/13/17.
 */

public class WatchList {
    private Stock[] stocks;

    public WatchList(Stock[] s) {
        stocks = s;
    }

    public Stock[] getStocks() {
        return stocks;
    }

    /**
     * Swaps a stock with a neighboring stock
     *
     * @param stock
     *          Stock index to be swapped
     * @param direction
     *          Direction to swap stock; either positive (down) or negative (up).
     */
    public void swap(int stock, int direction) {
        if (stock == 0 && direction < 0) {
            return;
        } else if (stock == stocks.length-1 && direction > 0) {
            return;
        }

        if (direction > 0) {
            direction = 1;
        } else if (direction < 0) {
            direction = -1;
        } else {
            return;
        }

        Stock s = stocks[stock];
        stocks[stock] = stocks[stock+direction];
        stocks[stock+direction] = s;
    }

    public void moveStock(int oldPos, int newPos) {
        if (oldPos >= stocks.length || oldPos < 0 || newPos >= stocks.length || newPos < 0) {
            throw new ArrayIndexOutOfBoundsException("Exceeded watch list bounds");
        }

        if (oldPos == newPos) {
            return;
        } else if (oldPos < newPos) {
            Stock moved = stocks[oldPos];
            for (int i = oldPos; i < newPos; i++) {
                stocks[i] = stocks[i+1];
            }
            stocks[newPos] = moved;
        } else {
            Stock moved = stocks[oldPos];
            for (int i = oldPos; i > newPos; i--) {
                stocks[i] = stocks[i-1];
            }
            stocks[newPos] = moved;
        }
    }

}
