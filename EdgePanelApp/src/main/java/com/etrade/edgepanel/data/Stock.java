package com.etrade.edgepanel.data;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by dpowell1 on 7/10/17.
 */

public class Stock {
    private String ticker;
    private String name;
    private double dollarValue;
    private double dollarChange;
    private double percentChange;
    private static final String ETRADE_STOCK_URL =
            "https://mobiletrade.uat.etrade.com/app/quote/getmobilequotes/" + "%s.json" +
            "?&EHFLAG=false&requireEarningsDate=true";

    public Stock(String ticker) {
        this.ticker = ticker;
        pullStockInfo(ticker);
    }

    /**
     * Refreshes the stock's information
     */
    public void update() {
        pullStockInfo(getTicker());
    }

    /**
     * Starts a new asynchronous task to pull stock information from the internet
     *
     * @param ticker
     *          Stock's ticker symbol
     */
    private void pullStockInfo(String ticker) {
        String path = String.format(ETRADE_STOCK_URL, ticker);
        new AsyncUrlRequest().execute(path);
    }

    /**
     * Parses the JSON-String response from the URL request.
     * Extracts the ticker, current dollar value, change in
     * dollars, and percentage change
     *
     * @param jsonString
     */
    private void parseResponse(String jsonString) {
        JsonObject jo = new JsonParser().parse(jsonString).getAsJsonObject();
        if (isJsonError(jsonString) == "Data Unavailable" ||
                isJsonError(jsonString) == "QuoteResponse null") {
            // If error in getting stock, try again
            pullStockInfo(getTicker());
            return;
        } else if (isJsonError(jsonString) == "Incorrect symbol") {
            // handle incorrect symbol by removing the stock from watch list
        } else if (isJsonError(jsonString) == "None") {
            JsonObject quote =
                    jo.get("data").getAsJsonObject().get("QuoteResponse").getAsJsonArray().get(0).getAsJsonObject();
            String ticker = quote.get("symbol").getAsString();
            String name = quote.get("symbolDescription").getAsString();
            double dollarValue = quote.get("lastPrice").getAsDouble();
            DecimalFormat df = new DecimalFormat(("#.##"));
            dollarValue = Double.valueOf(df.format(dollarValue));
            double dollarChange = quote.get("change").getAsDouble();
            dollarChange = Double.valueOf(df.format(dollarChange));
            double percChange = quote.get("percentChange").getAsDouble();
            // Round values
            dollarValue = ((double) Math.round(dollarValue * 100)) / 100;
            dollarChange = ((double) Math.round(dollarChange * 100)) / 100;
            percChange = ((double) Math.round(percChange * 100)) / 100;
//        Log.d("Json values:", ticker + ":" + name + ":" + dollarValue + ":" + dollarChange + ":" + percChange);
            setDefaultValues(ticker, name, dollarValue, dollarChange, percChange);
        }
    }

    /**
     * Check if the E-Trade backend was unable to return stock data
     *
     * @param jsonString
     *
     * @return
     */
    public String isJsonError(String jsonString) {
        JsonObject data = new JsonParser().parse(jsonString).getAsJsonObject().get("data").getAsJsonObject();
        // If error message (== "Quote unavailable, try again in a few minutes")
        JsonElement message = data.get("Messages");
        String error = "None";
        if (message != null) {
            JsonObject messageBody = data.get("Messages").getAsJsonArray().get(0).getAsJsonObject();
            if (messageBody.get("type").getAsString().equals("ERROR")) {
                if(messageBody.get("text").getAsString().equals("This symbol is not recognized. " +
                        "Please check the symbol and re-enter.")) {
                    Log.d("Incorrect Symbol", getTicker() + ":" + message.toString());
                    error = "Incorrect Symbol";
                } else {
                    Log.d("Stock data unavailable", getTicker() + ":" + message.toString());
                    error = "Stock data unavailable";
                }
            }
        }
        // If QuoteResponse is empty
        if (data.get("QuoteResponse").getAsJsonArray().size() == 0) {
            Log.e("QuoteResponse null", getTicker());
            error = "QuoteResponse null";
        }
        return error;
    }

    /**
     * Sets the default values of this {@code Stock} object
     *
     * @param ticker
     * @param name
     * @param dollarValue
     * @param dollarChange
     * @param percChange
     */
    private void setDefaultValues(String ticker, String name, double dollarValue,
                                  double dollarChange, double percChange) {
        this.ticker = ticker;
        this.name = name;
        this.dollarChange = dollarChange;
        this.dollarValue = dollarValue;
        this.percentChange = percChange;
    }

    // Getters and setters

    public String getTicker() {
        return this.ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return this.name; }

    public double getDollarValue() {
        return this.dollarValue;
    }

    public void setDollarValue(int value) {
        this.dollarValue = value;
    }

    public double getDollarChange() { return this.dollarChange; }

    public void setDollarChange(double dollar) { this.dollarChange = dollar; }

    public double getPercentChange() { return this.percentChange; }

    public void setPercentChange(double perc) { this.percentChange = perc; }

    /**
     * Asynchronously requests a JSON file that contains all the stock information
     * from the specified URL.
     */
    private class AsyncUrlRequest extends AsyncTask<String, Void, String> {

        /**
         * Does the work this thread is meant to do -- retrieving a JSON
         * String containing updated stock information
         *
         * @param params
         *          Strings fed in from {@code asyncUrlRequest.execute(params)}
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            String urlPath = String.valueOf(params[0]);
            StringBuffer jsonString = new StringBuffer();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                return jsonString.toString();
            } catch (Exception e) {
                Log.e("URL request error", e.getMessage());
            }
            return null;
        }

        /**
         * Upon completion of the {@code doInBackground()} method, this method
         * is called to process the results in the parent class
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            parseResponse(result);
        }

    }

}
