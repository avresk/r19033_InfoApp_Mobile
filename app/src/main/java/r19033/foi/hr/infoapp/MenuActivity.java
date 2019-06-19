package r19033.foi.hr.infoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import r19033.foi.hr.infoapp.adapters.NarudzbaAdapter;
import r19033.foi.hr.infoapp.callbacks.ListUpdateCallback;
import r19033.foi.hr.infoapp.callbacks.SearchByID;
import r19033.foi.hr.infoapp.models.Narudzba;
import r19033.foi.hr.infoapp.utils.LoadProgress;
import r19033.foi.hr.infoapp.utils.MSSQL;

public class MenuActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 99;

  private LinearLayout llOrderList;
  private LinearLayout llSortedOrder;
  private ListView mOrderListView;
  private ListView mSortedOrdersListView;
  private Button btnSearch;
  private Button btnClear;

  private ListUpdateCallback callback;
  private SearchByID callbackByID;

  private Spinner spSortBy;
  private Spinner spSortResult;

  private ArrayList<Narudzba> mOrderList = new ArrayList<>();
  private ArrayList<Narudzba> mSortedOrdersList = new ArrayList<>();
  private NarudzbaAdapter adbNarudzba;
  private NarudzbaAdapter adbOrderedNarudzba;

  private final String[] string_sortBy = {"Ime korisnika", "Lokacija"};
  private final String[] string_dummy = {"test test", "Ivo Ivic"};
  private LoadProgress progress;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
          = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_order_list:
          showLayoutOrderList();
          return true;
        case R.id.navigation_new_order:
          showLayoutSortedOrders();
          return true;

      }
      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);

    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    initialize();
    new GetDataAsync().execute(callback);
    progress.showDialog();
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == REQUEST_CODE) {
      if (resultCode == 123) {
        progress.showDialog();
        new GetDataAsync().execute(callback);
      }
    }
  }

  private void initialize() {
    progress = new LoadProgress(MenuActivity.this);
    llSortedOrder = findViewById(R.id.llNewOrder);
    llOrderList = findViewById(R.id.llViewOrders);

    mOrderListView = findViewById(R.id.lvOrders);
    mSortedOrdersListView = findViewById(R.id.lvSortedOrders);

    adbNarudzba = new NarudzbaAdapter(this, mOrderList);
    adbOrderedNarudzba = new NarudzbaAdapter(this, mSortedOrdersList);

    mOrderListView.setAdapter(adbNarudzba);
    mSortedOrdersListView.setAdapter(adbOrderedNarudzba);

    spSortBy = findViewById(R.id.spinnerSortBy);
    spSortResult = findViewById(R.id.spinnerSortResult);

    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
            (this, android.R.layout.simple_spinner_item, string_sortBy); //selected item will look like a spinner set from XML
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spSortBy.setAdapter(spinnerArrayAdapter);

    ArrayAdapter<String> spinnerArrayAdapterResult = new ArrayAdapter<String>
            (this, android.R.layout.simple_spinner_item, string_dummy); //selected item will look like a spinner set from XML
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spSortResult.setAdapter(spinnerArrayAdapterResult);


    btnSearch = findViewById(R.id.btnSortSearch);
    btnSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        progress.showDialog();
        new GetDataAsyncByID().execute(callbackByID);
      }
    });

    btnClear = findViewById(R.id.btnSortClear);
    btnClear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mSortedOrdersList.clear();
        adbOrderedNarudzba.notifyDataSetChanged();
      }
    });

    callback = new ListUpdateCallback() {
      @Override
      public void onUpdate(ArrayList<Narudzba> result) {
        //Log.d("debug", String.valueOf(result.size()));
        progress.dissmissDialog();
        mOrderList.clear();
        if (result != null) {
          mOrderList.addAll(result);
          if (!mOrderList.isEmpty()) {
            adbNarudzba.notifyDataSetChanged();
          } else {
            Toast.makeText(MenuActivity.this, "Lista narudzbi je prazna!", Toast.LENGTH_SHORT).show();
          }
        } else {
          Toast.makeText(MenuActivity.this, "Lista narudzbi je prazna!", Toast.LENGTH_SHORT).show();
        }

      }
    };

    callbackByID = new SearchByID() {
      @Override
      public void onUpdate(ArrayList<Narudzba> result) {
        progress.dissmissDialog();
        mSortedOrdersList.clear();
        if (result != null) {
          mSortedOrdersList.addAll(result);
          if (!mSortedOrdersList.isEmpty()) {
            adbOrderedNarudzba.notifyDataSetChanged();
          } else {
            Toast.makeText(MenuActivity.this, "Lista narudzbi je prazna!", Toast.LENGTH_SHORT).show();
          }
        } else {
          Toast.makeText(MenuActivity.this, "Lista narudzbi je prazna!", Toast.LENGTH_SHORT).show();
        }
      }
    };

    //order list view
  }

  private void showLayoutOrderList() {

    if (llOrderList.getVisibility() != View.VISIBLE) {
      llOrderList.setVisibility(View.VISIBLE);
      llSortedOrder.setVisibility(View.GONE);
    }

    progress.showDialog();
    new GetDataAsync().execute(callback);
  }

  private void showLayoutSortedOrders() {
    if (llSortedOrder.getVisibility() != View.VISIBLE) {
      llSortedOrder.setVisibility(View.VISIBLE);
      llOrderList.setVisibility(View.GONE);
    }


  }

  @SuppressLint("StaticFieldLeak")
  class GetDataAsync extends AsyncTask<ListUpdateCallback, Void, ArrayList<Narudzba>> {

    ListUpdateCallback callback;

    @Override
    protected ArrayList<Narudzba> doInBackground(ListUpdateCallback... listUpdateCallbacks) {
      callback = listUpdateCallbacks[0];
      // napraviti return prema commandi
      return MSSQL.getInstance().upit_pregledSvihNarudzbi();
    }

    @Override
    protected void onPostExecute(ArrayList<Narudzba> narudzbas) {
      callback.onUpdate(narudzbas);
    }
  }

  @SuppressLint("StaticFieldLeak")
  class GetDataAsyncByID extends AsyncTask<SearchByID, Void, ArrayList<Narudzba>> {

    SearchByID callback;

    @Override
    protected ArrayList<Narudzba> doInBackground(SearchByID... listUpdateCallbacks) {
      callback = listUpdateCallbacks[0];
      // napraviti return prema commandi
      return MSSQL.getInstance().upit_narudbaByID(1L);
    }

    @Override
    protected void onPostExecute(ArrayList<Narudzba> narudzbas) {
      callback.onUpdate(narudzbas);
    }
  }
}
