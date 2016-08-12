package com.jeannaclark.android.stockhawk.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.db.chart.view.LineChartView;
import com.jeannaclark.android.stockhawk.R;

/**
 * Created by jeannaclark on 8/10/16.
 */
public class DetailChartViewHolder extends RecyclerView.ViewHolder {

    LineChartView lineChartView;

    public DetailChartViewHolder(View itemView) {
        super(itemView);

        lineChartView = (LineChartView) itemView.findViewById(R.id.stock_line_chart);
    }

    public LineChartView getLineChartView() {
        return lineChartView;
    }

    public void setLineChartView(LineChartView lineChartView) {
        this.lineChartView = lineChartView;
    }
}
