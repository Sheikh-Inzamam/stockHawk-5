package com.jeannaclark.android.stockhawk.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeannaclark on 8/11/16.
 */
public class Chart implements Parcelable {

    //TODO: setup chart model using YQL historical data parsed
    String symbol; // "Symbol"
    String date; // "Date"
    Double highQuote; // "High"

    public Chart() {
    }

    public Chart(String symbol, String date, Double highQuote) {
        this.symbol = symbol;
        this.date = date;
        this.highQuote = highQuote;
    }

    public Chart(Parcel in) {
        symbol = in.readString();
        date = in.readString();
        highQuote = in.readDouble();
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getHighQuote() {
        return highQuote;
    }

    public void setHighQuote(Double highQuote) {
        this.highQuote = highQuote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(symbol);
        parcel.writeString(date);
        parcel.writeDouble(highQuote);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Chart createFromParcel(Parcel parcel) {
            return new Chart(parcel);
        }

        public Chart[] newArray(int size) {
            return new Chart[size];
        }
    };
}
