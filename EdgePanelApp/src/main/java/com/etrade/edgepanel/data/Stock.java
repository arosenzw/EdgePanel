package com.etrade.edgepanel.data;

/**
 * Created by dpowell1 on 7/10/17.
 */

public class Stock {
    private String name;
    private int value;

    public Stock(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
