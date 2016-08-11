package com.jeannaclark.android.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.jeannaclark.android.stockhawk.R;
import com.jeannaclark.android.stockhawk.data.StockDBContract;
import com.jeannaclark.android.stockhawk.data.StockContentProvider;
import com.jeannaclark.android.stockhawk.model.DividerItemDecoration;
import com.jeannaclark.android.stockhawk.model.MainRecyclerViewAdapter;
import com.jeannaclark.android.stockhawk.model.RecyclerViewItemClickListener;
import com.jeannaclark.android.stockhawk.service.StockIntentService;
import com.jeannaclark.android.stockhawk.service.StockTaskService;
import com.jeannaclark.android.stockhawk.touch_helper.SimpleItemTouchHelperCallback;

/**
 * Created by jeannaclark on 08/10/16.
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MainRecyclerViewAdapter mCursorAdapter;
    private static final int CURSOR_LOADER_ID = 0;
    private Intent mServiceIntent;
    private ItemTouchHelper mItemTouchHelper;
    private static final String SELECTED_KEY = "selected_position";
    private static final String EXTRA_MESSAGE = "Stock data: ";
    private Context mContext;
    private Cursor mCursor;
    private Uri mUri;
    static boolean isConnected;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mPosition = -1;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCursorAdapter = new MainRecyclerViewAdapter(getActivity(), null);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mContext = getContext();
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View v, int position) {
                        Cursor cursor = mCursor;
                        cursor.moveToPosition(position);
                        String symbol = cursor.getString(cursor.getColumnIndex("symbol"));

                        mUri = StockContentProvider.Quotes.withSymbol(symbol);

                        Intent intent = new Intent(getActivity(), DetailActivity.class)
                                .setData(mUri);
                        startActivity(intent);
                    }
                }));

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        recyclerView.setAdapter(mCursorAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        recyclerView.addItemDecoration(itemDecoration);

        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // The intent service is for executing immediate pulls from the Yahoo API
        // GCMTaskService can only schedule tasks, they cannot execute immediately
        mServiceIntent = new Intent(mContext, StockIntentService.class);

        if (savedInstanceState == null) {
            // Run the initialize task service so that some stocks appear upon an empty database
            mServiceIntent.putExtra("tag", "init");
            if (isConnected){
                getActivity().startService(mServiceIntent);
            } else{
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
            }
        }

        com.melnykov.fab.FloatingActionButton fab = (com.melnykov.fab.FloatingActionButton) rootView
                .findViewById(R.id.fab);
        fab.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (isConnected){
                    new MaterialDialog.Builder(mContext).title(R.string.symbol_search)
                            .content(R.string.content_test)
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                                @Override public void onInput(MaterialDialog dialog, CharSequence input) {
                                    // On FAB click, receive user input. Make sure the stock doesn't
                                    // already exist in the DB and proceed accordingly
                                    Cursor c = getActivity().getContentResolver()
                                            .query(StockContentProvider.Quotes.CONTENT_URI,
                                            new String[] { StockDBContract.SYMBOL }, StockDBContract
                                                            .SYMBOL + "= ? COLLATE NOCASE",
                                            new String[] { input.toString() }, null);
                                    if (c.getCount() != 0) {
                                        Toast toast =
                                                Toast.makeText(getActivity(), "This stock is already " +
                                                        "saved!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                        toast.show();
                                        return;
                                    } else {
                                        // Add the stock to DB
                                        mServiceIntent.putExtra("tag", "add");
                                        mServiceIntent.putExtra("symbol", input.toString());
                                        getActivity().startService(mServiceIntent);
                                    }
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(getActivity(), "No internet connection",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        updateStocks();

        //TODO: setup swipe to delete delay & Undo option

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout)
                rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.material_blue_500,
                R.color.material_green_700, R.color.material_red_700);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshStocks();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != -1) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        // This narrows the return to only the stocks that are most current.
        return new CursorLoader(getContext(), StockContentProvider.Quotes.CONTENT_URI,
                new String[]{ StockDBContract._ID, StockDBContract.SYMBOL, StockDBContract.BIDPRICE,
                        StockDBContract.PERCENT_CHANGE, StockDBContract.CHANGE, StockDBContract.ISUP,
                        StockDBContract.EXCHANGE, StockDBContract.NAME},
                StockDBContract.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        mCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mCursorAdapter.swapCursor(null);
    }

    public void updateStocks() {
        if (isConnected){
            long period = 3600L;
            long flex = 10L;
            String periodicTag = "periodic";

            // create a periodic task to pull stocks once every hour after the app has been opened.
            // This is so Widget data stays up to date.
            PeriodicTask periodicTask = new PeriodicTask.Builder()
                    .setService(StockTaskService.class)
                    .setPeriod(period)
                    .setFlex(flex)
                    .setTag(periodicTag)
                    .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                    .setRequiresCharging(false)
                    .build();
            // Schedule task with tag "periodic." This ensure that only the stocks present in the DB
            // are updated.
            GcmNetworkManager.getInstance(getActivity()).schedule(periodicTask);
        }
    }

    public void refreshStocks() {

        //TODO: instant setup YQL refresh

        updateStocks();
        Toast.makeText(getContext(), "Data refreshed", Toast.LENGTH_LONG).show();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}