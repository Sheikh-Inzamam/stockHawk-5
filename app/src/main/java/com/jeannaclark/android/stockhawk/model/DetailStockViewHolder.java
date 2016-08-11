package com.jeannaclark.android.stockhawk.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannaclark.android.stockhawk.R;

/**
 * Created by jeannaclark on 8/10/16.
 */
public class DetailStockViewHolder extends RecyclerView.ViewHolder {

    private TextView stockName;
    private TextView bidPrice;
    private TextView stockSymbol;
    private ImageView trendArrow;
    private TextView change;


    public DetailStockViewHolder(View itemView) {
        super(itemView);

        stockName = (TextView) itemView.findViewById(R.id.detail_stock_name);
        bidPrice = (TextView) itemView.findViewById(R.id.detail_bid_price);
        stockSymbol = (TextView) itemView.findViewById(R.id.detail_stock_symbol);
        trendArrow = (ImageView) itemView.findViewById(R.id.detail_trend_arrow);
        change = (TextView) itemView.findViewById(R.id.detail_change);
    }

    public TextView getStockName() {
        return stockName;
    }

    public void setStockName(TextView stockName) {
        this.stockName = stockName;
    }

    public TextView getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(TextView bidPrice) {
        this.bidPrice = bidPrice;
    }

    public TextView getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(TextView stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public ImageView getTrendArrow() {
        return trendArrow;
    }

    public void setTrendArrow(ImageView trendArrow) {
        this.trendArrow = trendArrow;
    }

    public TextView getChange() {
        return change;
    }

    public void setChange(TextView change) {
        this.change = change;
    }
}
