/**
 * Created by arosenzw on 7/25/17.
 */

import com.etrade.edgepanel.data.Stock;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class StockTests {

    @Test
    public void testConstructor() {
        Stock s = new Stock("AAPL");
        assertTrue(s.getTicker() == "AAPL");
        Stock s1 = new Stock("FB");
        assertTrue(s1.getTicker() == "FB");
        assertFalse(s.getTicker() == s1.getTicker());
        Stock s2 = new Stock("AAPL");
        assertTrue(s.getTicker() == s2.getTicker());
    }
}
