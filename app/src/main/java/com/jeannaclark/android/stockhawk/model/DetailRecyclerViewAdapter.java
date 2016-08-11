package com.jeannaclark.android.stockhawk.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannaclark.android.stockhawk.R;

import java.util.List;

/**
 * Created by jeannaclark on 8/10/16.
 */
public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;

    private final int STOCK = 0;
    private final int CHART = 1;
    private Context mContext;

    public DetailRecyclerViewAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Stock) {
            return STOCK;
        } else {
            return CHART;
        }

        //TODO: change to else if (instanceof Chart) & else {return -1}
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        mContext = viewGroup.getContext();

        switch (viewType) {
            case CHART:
                View chartView = inflater.inflate(R.layout.activity_detail_chart_item, viewGroup, false);
                viewHolder = new DetailChartViewHolder(chartView);
                break;
            default:
                View stockView = inflater.inflate(R.layout.activity_detail_stock_item, viewGroup, false);
                viewHolder = new DetailStockViewHolder(stockView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case STOCK:
                DetailStockViewHolder stockViewHolder = (DetailStockViewHolder) viewHolder;
                configureStockViewHolder(stockViewHolder, position);
                break;
            case CHART:
                DetailChartViewHolder chartViewHolder = (DetailChartViewHolder) viewHolder;
                configureChartViewHolder(chartViewHolder, position);
                break;
        }
    }

    private void configureStockViewHolder(DetailStockViewHolder stockViewHolder, int position) {
        Stock stock = (Stock) items.get(position);

        if (stock != null) {
            if (Integer.parseInt(stock.getIsUp()) == 1) {
                stockViewHolder.getTrendArrow().setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.vector_drawable_ic_arrow_upward_white___px));
            }
            else if (Integer.parseInt(stock.getIsUp()) == 0) {
                stockViewHolder.getTrendArrow().setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.vector_drawable_ic_arrow_downward_white___px));
            }

            stockViewHolder.getChange().setText(mContext.getString(R.string.xliff_grid_layout_percent_change,
                    stock.getAmountChange(), stock.getPercentChange()));
            stockViewHolder.getStockSymbol().setText(mContext.getString(R.string.xliff_grid_layout_exchange_symbol,
                    stock.getStockExchange(), stock.getSymbol()));
            stockViewHolder.getBidPrice().setText(stock.getBidPrice());
            stockViewHolder.getStockName().setText(stock.getName());
        }
    }

    private void configureChartViewHolder(DetailChartViewHolder chartViewHolder, int position) {

        //TODO: insert chart view data

    }
}
