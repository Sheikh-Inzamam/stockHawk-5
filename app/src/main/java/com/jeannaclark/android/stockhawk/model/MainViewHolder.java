package com.jeannaclark.android.stockhawk.model;

import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeannaclark.android.stockhawk.R;
import com.jeannaclark.android.stockhawk.touch_helper.ItemTouchHelperViewHolder;

/**
 * Created by jeannaclark on 8/10/16.
 */
public class MainViewHolder extends RecyclerView.ViewHolder
        implements ItemTouchHelperViewHolder, View.OnClickListener {

    private TextView symbol;
    private TextView bidPrice;
    private TextView change;
    private TextView name;
    private ImageView trendArrow;
    private LinearLayout linearLayout;

    public MainViewHolder(View itemView) {
        super(itemView);

        symbol = (TextView) itemView.findViewById(R.id.stock_symbol);
        bidPrice = (TextView) itemView.findViewById(R.id.bid_price);
        change = (TextView) itemView.findViewById(R.id.change);
        name = (TextView) itemView.findViewById(R.id.stock_name);
        trendArrow = (ImageView) itemView.findViewById(R.id.trend_arrow);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.change_arrow_linear);
    }

    public TextView getSymbol() {
        return symbol;
    }

    public void setSymbol(TextView symbol) {
        this.symbol = symbol;
    }

    public TextView getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(TextView bidPrice) {
        this.bidPrice = bidPrice;
    }

    public TextView getChange() {
        return change;
    }

    public void setChange(TextView change) {
        this.change = change;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public ImageView getTrendArrow() {
        return trendArrow;
    }

    public void setTrendArrow(ImageView trendArrow) {
        this.trendArrow = trendArrow;
    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }

    @Override
    public void onClick(View v) {
        //TODO fill in onClick for MainViewHolder
    }
}
