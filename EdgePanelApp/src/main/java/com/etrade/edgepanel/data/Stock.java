package com.etrade.edgepanel.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by dpowell1 on 7/10/17.
 */

public class Stock {
    private String ticker;
    private String name;
    private double value;
    private double dollar_change;
    private double percent_change;
    private String color;

    public Stock(String ticker, String name, double value, double dollar, double perc) {
        this.ticker = ticker;
        this.name = name;
        this.value = value;
        this.dollar_change = dollar;
        this.percent_change = perc;
    }

    public String getTicker() {
        return this.ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return this.name; }

    public double getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getDollar_change() { return this.dollar_change; }

    public void setDollar_change(double dollar) { this.dollar_change = dollar; }

    public double getPercent_change() { return this.percent_change; }

    public void setPercent_change(double perc) { this.percent_change = perc; }

    public String getColor() { return this.color; }


    public void setColor() {
        if (percent_change > 0) {
            this.color = "green";
        } else if (percent_change == 0) {
            this.color = "gray";
        } else {
            this.color = "red";
        }
    }


}
