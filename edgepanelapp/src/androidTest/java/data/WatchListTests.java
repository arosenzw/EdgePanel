package data;

/**
 * Created by arosenzw on 7/25/17.
 */

import com.etrade.edgepanel.data.Stock;
import com.etrade.edgepanel.data.WatchList;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class WatchListTests {

    @Test
    public void testWatchLists_0() {
        Stock s = new Stock("AAPL");
        Stock s1 = new Stock("FB");
        Stock s2 = new Stock("AMZN");
        Stock[] stocks = {s, s1, s2};

        WatchList w = new WatchList(stocks, "WatchList one");
        assertTrue(w.size() == 3);
        assertTrue(w.getStock(0) == s);
        assertFalse(w.getStock(1) == s2);
        assertTrue(w.getStocks() == stocks);
    }

    @Test
    public void testWatchLists_1() {
        Stock s = new Stock("AAPL");
        Stock s1 = new Stock("AAPL");
        Stock s2 = new Stock("MSFT");
        Stock s3 = new Stock("SNAP");
        Stock s4 = new Stock("BABA");
        Stock[] stocks = {s, s1, s2, s3, s4};

        WatchList w = new WatchList(stocks, "WatchList two");

        assertTrue(w.size() == 5);
        assertFalse(w.size() == 4);
        assertTrue(w.getStock(0) == s);
        assertTrue(w.getStock(1) == s1);
        assertTrue(w.getStocks() == stocks);
    }

}
