package com.jeannaclark.android.stockhawk.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeannaclark on 8/10/16.
 */
public class Stock implements Parcelable {

    String name;
    String symbol;
    String stockExchange;
    String bidPrice;
    String amountChange;
    String percentChange;
    String isUp;

    public Stock() {
    }

    public Stock(String name, String symbol, String stockExchange, String bidPrice, String amountChange, String percentChange, String isUp) {
        this.name = name;
        this.symbol = symbol;
        this.stockExchange = stockExchange;
        this.bidPrice = bidPrice;
        this.amountChange = amountChange;
        this.percentChange = percentChange;
        this.isUp = isUp;
    }

    public Stock(Parcel in) {
        name = in.readString();
        symbol = in.readString();
        stockExchange = in.readString();
        bidPrice = in.readString();
        amountChange = in.readString();
        percentChange = in.readString();
        isUp = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getAmountChange() {
        return amountChange;
    }

    public void setAmountChange(String amountChange) {
        this.amountChange = amountChange;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public String getIsUp() {
        return isUp;
    }

    public void setIsUp(String isUp) {
        this.isUp = isUp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(symbol);
        parcel.writeString(stockExchange);
        parcel.writeString(bidPrice);
        parcel.writeString(amountChange);
        parcel.writeString(percentChange);
        parcel.writeString(isUp);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Stock createFromParcel(Parcel parcel) {
            return new Stock(parcel);
        }

        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };
}
