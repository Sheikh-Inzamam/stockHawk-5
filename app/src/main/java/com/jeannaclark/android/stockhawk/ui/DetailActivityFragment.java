package com.jeannaclark.android.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jeannaclark.android.stockhawk.R;
import com.jeannaclark.android.stockhawk.Utility;
import com.jeannaclark.android.stockhawk.data.StockDBContract;
import com.jeannaclark.android.stockhawk.model.DetailRecyclerViewAdapter;
import com.jeannaclark.android.stockhawk.model.Stock;


import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private ArrayList<Object> mStockDetailList = new ArrayList<>();
    private ShareActionProvider mShareActionProvider;
    static final String DETAIL_URI = "URI";
    private String stockInfo;
    private String mSymbol;
    private DetailRecyclerViewAdapter mStockAdapter;
    private Uri mUri;

    public static final String[] STOCK_COLUMNS = {
            StockDBContract.SYMBOL,
            StockDBContract.PERCENT_CHANGE,
            StockDBContract.CHANGE,
            StockDBContract.BIDPRICE,
            StockDBContract.ISUP,
            StockDBContract.NAME,
            StockDBContract.EXCHANGE
    };

    public static final int COL_SYMBOL = 0;
    public static final int COL_PERCENT_CHANGE = 1;
    public static final int COL_CHANGE = 2;
    public static final int COL_BIDPRICE = 3;
    public static final int COL_ISUP = 4;
    public static final int COL_NAME = 5;
    public static final int COL_EXCHANGE = 6;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        RecyclerView recyclerViewStock = (RecyclerView) rootView.findViewById(R.id.detail_recycler_view);

        mStockAdapter= new DetailRecyclerViewAdapter(mStockDetailList);
        recyclerViewStock.setAdapter(mStockAdapter);

        recyclerViewStock.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareStockIntent());
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (stockInfo != null) {
            mShareActionProvider.setShareIntent(createShareStockIntent());
        } else {
            Log.d(LOG_TAG, "Share action provider is null?");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (null != mUri) {
            updateStocks();
        }
        super.onActivityCreated(savedInstanceState);
    }

    private Intent createShareStockIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, stockInfo);
        return shareIntent;
    }

    public void updateStocks() {
        Log.v("in update stocks: ", mUri.toString());

        mSymbol = Utility.getStockSymbolFromUri(mUri);

        Cursor stockCursor = getActivity().getContentResolver().query(
                mUri,
                DetailActivityFragment.STOCK_COLUMNS,
                StockDBContract.SYMBOL + " = ? AND " + StockDBContract.ISCURRENT + " = ?",
                new String[]{ mSymbol , "1"},
                null);

        if (stockCursor.moveToFirst()) {
            Stock stock = new Stock();

            stock.setSymbol(stockCursor.getString(COL_SYMBOL));
            stock.setStockExchange(stockCursor.getString(COL_EXCHANGE));
            stock.setPercentChange(stockCursor.getString(COL_PERCENT_CHANGE));
            stock.setAmountChange(stockCursor.getString(COL_CHANGE));
            stock.setName(stockCursor.getString(COL_NAME));
            stock.setBidPrice(stockCursor.getString(COL_BIDPRICE));
            stock.setIsUp(stockCursor.getString(COL_ISUP));

            stockInfo = stock.getName() + ": bid-price = " + stock.getBidPrice();

            getActivity().setTitle(stock.getName());

            mStockDetailList.add(stock);
        } else {
            Log.v(LOG_TAG, "stockCursor returned null");
        }
        stockCursor.close();
        mStockAdapter.notifyDataSetChanged();
    }
}
