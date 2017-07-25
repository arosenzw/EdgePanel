/**
 * Created by arosenzw on 7/25/17.
 */
import com.etrade.edgepanel.data.Stock;
import com.etrade.edgepanel.data.WatchList;
import com.etrade.edgepanel.data.WatchListManager;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)

public class WatchListManagerTests {

    @Test
    public void InitializationTest() {
        Stock s = new Stock("AAPL");
        Stock s1 = new Stock("MSFT");
        Stock s2 = new Stock("AMZN");
        Stock s3 = new Stock("FB");
        Stock[] stocks0 = {s, s1};
        Stock[] stocks1 = {s2, s3};

        WatchList w = new WatchList(stocks0, "WatchList one");
        WatchList w1 = new WatchList(stocks1, "WatchList two");
        WatchList[] watchLists = {w, w1};

        WatchListManager watchListManager = new WatchListManager(watchLists);
        assertTrue(watchListManager.size() == 2);
        assertTrue(watchListManager.getWatchListArray() == watchLists);
    }

    @Test
    public void ActiveWatchListTest() {
        Stock s = new Stock("AAPL");
        Stock s1 = new Stock("MSFT");
        Stock s2 = new Stock("AMZN");
        Stock s3 = new Stock("FB");
        Stock[] stocks0 = {s, s1};
        Stock[] stocks1 = {s2};
        Stock[] stocks2 = {s3};

        WatchList w = new WatchList(stocks0, "WatchList one");
        WatchList w1 = new WatchList(stocks1, "WatchList two");
        WatchList w2 = new WatchList(stocks2, "WatchList three");
        WatchList[] watchLists = {w, w1, w2};

        WatchListManager watchListManager = new WatchListManager(watchLists);
        assertTrue(watchListManager.size() == 3);

        watchListManager.setActive(0);
        assertTrue(watchListManager.getActive() == 0);
        assertFalse(watchListManager.getActive() == 1);
        assertTrue(watchListManager.getActiveWatchList() == w);

        watchListManager.setActive(2);
        assertTrue(watchListManager.getActive() == 2);
        assertFalse(watchListManager.getActive() == 1);
        assertTrue(watchListManager.getActiveWatchList() == w2);
    }
}
