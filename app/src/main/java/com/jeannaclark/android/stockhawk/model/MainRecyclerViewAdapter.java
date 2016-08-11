package com.jeannaclark.android.stockhawk.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannaclark.android.stockhawk.R;
import com.jeannaclark.android.stockhawk.data.StockContentProvider;
import com.jeannaclark.android.stockhawk.data.StockDBContract;
import com.jeannaclark.android.stockhawk.touch_helper.ItemTouchHelperAdapter;

/**
 * Created by sam_chordas on 10/6/15.
 *  Credit to skyfishjy gist:
 *    https://gist.github.com/skyfishjy/443b7448f59be978bc59
 * for the code structure
 */
public class MainRecyclerViewAdapter extends CursorRecyclerViewAdapter<MainViewHolder>
    implements ItemTouchHelperAdapter {

  private static Context mContext;
  private boolean isPercent;

  public MainRecyclerViewAdapter(Context context, Cursor cursor){
    super(context, cursor);
    mContext = context;
  }

  @Override
  public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.activity_main_list_item, parent, false);
    MainViewHolder vh = new MainViewHolder(itemView);
    return vh;
  }

  @Override
  public void onBindViewHolder(final MainViewHolder viewHolder, final Cursor cursor){

    String percentChange = cursor.getString(cursor.getColumnIndex("percent_change"));
    String amountChange = cursor.getString(cursor.getColumnIndex("change"));
    String bidPrice = cursor.getString(cursor.getColumnIndex("bid_price"));
    String stockExchange = cursor.getString(cursor.getColumnIndex("exchange"));

    String symbol = cursor.getString(cursor.getColumnIndex("symbol"));

    viewHolder.getBidPrice().setText(mContext.getString(R.string.xliff_grid_layout_bid_price, bidPrice));
    viewHolder.getChange().setText(mContext.getString(R.string.xliff_grid_layout_percent_change, amountChange, percentChange));
    viewHolder.getSymbol().setText(mContext.getString(R.string.xliff_grid_layout_bid_symbol_exchange, symbol, stockExchange));
    viewHolder.getName().setText(cursor.getString(cursor.getColumnIndex("name")));

    int sdk = Build.VERSION.SDK_INT;
    if (cursor.getInt(cursor.getColumnIndex("is_up")) == 1){
      if (sdk < Build.VERSION_CODES.JELLY_BEAN){
        viewHolder.getTrendArrow().setImageDrawable(
                mContext.getResources().getDrawable(R.drawable.vector_drawable_ic_arrow_upward_white___px));
        viewHolder.getLinearLayout().setBackgroundDrawable(
                mContext.getResources().getDrawable(R.drawable.percent_change_pill_green));
      }else {
        viewHolder.getTrendArrow().setImageDrawable(
                mContext.getResources().getDrawable(R.drawable.vector_drawable_ic_arrow_upward_white___px));
        viewHolder.getLinearLayout().setBackground(
                mContext.getResources().getDrawable(R.drawable.percent_change_pill_green));
      }
    } else{
      if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
        viewHolder.getTrendArrow().setImageDrawable(
                mContext.getResources().getDrawable(R.drawable.vector_drawable_ic_arrow_downward_white___px));
        viewHolder.getLinearLayout().setBackgroundDrawable(
                mContext.getResources().getDrawable(R.drawable.percent_change_pill_red));
      } else{
        viewHolder.getTrendArrow().setImageDrawable(
                mContext.getResources().getDrawable(R.drawable.vector_drawable_ic_arrow_downward_white___px));
        viewHolder.getLinearLayout().setBackground(
                mContext.getResources().getDrawable(R.drawable.percent_change_pill_red));
      }
    }
  }

  @Override public void onItemDismiss(int position) {
    Cursor c = getCursor();
    c.moveToPosition(position);
    String symbol = c.getString(c.getColumnIndex(StockDBContract.SYMBOL));
    mContext.getContentResolver().delete(StockContentProvider.Quotes.withSymbol(symbol), null, null);
    notifyItemRemoved(position);
  }

  @Override public int getItemCount() {
    return super.getItemCount();
  }
}
