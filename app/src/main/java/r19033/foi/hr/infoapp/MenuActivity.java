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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import r19033.foi.hr.infoapp.adapters.NarudzbaAdapter;
import r19033.foi.hr.infoapp.callbacks.ListUpdateCallback;
import r19033.foi.hr.infoapp.models.Narudzba;
import r19033.foi.hr.infoapp.utils.Constants;
import r19033.foi.hr.infoapp.utils.LoadProgress;
import r19033.foi.hr.infoapp.utils.MSSQL;

public class MenuActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 99;

  private LinearLayout llOrderList;
  private LinearLayout llSortedOrder;
  private LinearLayout llListOrders;
  private LinearLayout llListUnfinishedOrders;

  private ListView mOrderListView;
  private ListView mUnfinishedOrdersListView;
  private ListView mSortedOrdersListView;

  private Button btnSearch;
  private Button btnClear;

  private CheckBox cbUnfinishedOrders;
  private CheckBox cbSortedUnfinishedOrders;

  private ListUpdateCallback callback;

  private Spinner spKrilo;
  private Spinner spKat;

  private ArrayList<Narudzba> mOrderList = new ArrayList<>();
  private ArrayList<Narudzba> mUnfinishedOrderList = new ArrayList<>();
  private ArrayList<Narudzba> mSortedOrdersList = new ArrayList<>();

  private NarudzbaAdapter adbUnfinishedNarudzba;
  private NarudzbaAdapter adbNarudzba;
  private NarudzbaAdapter adbOrderedNarudzba;

  //private int searchType = Constants.TRAZI_PO_KRILU;
  private String searchValueKrilo = "JUG";
  private String searchValueKat = "1";

  private final String[] string_sortBy = {"Lokacija: kat", "Lokacija: krilo"};
  private final String[] string_kat = {"KAT 1", "KAT 2", "KAT 3"};
  private final String[] string_krilo = {"JUG", "ISTOK"};
  private LoadProgress progress;

  private boolean showOnlyUnfinishedSearchOrders = false;

  ArrayAdapter<String> spinnerAdapterResultKat;
  ArrayAdapter<String> spinnerAdapterSort;
  ArrayAdapter<String> spinnerAdapterResultKrilo;


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
    new GetAllOrders().execute(callback);
    progress.showDialog();
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == REQUEST_CODE) {
      if (resultCode == 123) {
        progress.showDialog();
        new GetAllOrders().execute(callback);

      }
    }
  }

  private void initialize() {
    progress = new LoadProgress(MenuActivity.this);
    llSortedOrder = findViewById(R.id.llNewOrder);
    llOrderList = findViewById(R.id.llViewOrders);

    llListOrders = findViewById(R.id.listOrders);
    llListUnfinishedOrders = findViewById(R.id.listUnfinishedOrders);

    llListOrders.setVisibility(View.VISIBLE);
    llListUnfinishedOrders.setVisibility(View.GONE);

    mOrderListView = findViewById(R.id.lvOrders);
    mUnfinishedOrdersListView = findViewById(R.id.lvUnfinishedOrders);

    mSortedOrdersListView = findViewById(R.id.lvSortedOrders);

    cbUnfinishedOrders = findViewById(R.id.cbUnfinishedOrders);
    cbUnfinishedOrders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        showOrderByStatus(b);
      }
    });

    cbSortedUnfinishedOrders = findViewById(R.id.cbSortedUnfinishedOrders);
    cbSortedUnfinishedOrders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        showOnlyUnfinishedSearchOrders = b;
        applyLocalSearch();
      }
    });

    // adapteri
    adbNarudzba = new NarudzbaAdapter(this, mOrderList);
    adbOrderedNarudzba = new NarudzbaAdapter(this, mSortedOrdersList);
    adbUnfinishedNarudzba = new NarudzbaAdapter(this, mUnfinishedOrderList);

    // ListView's
    mOrderListView.setAdapter(adbNarudzba);
    mUnfinishedOrdersListView.setAdapter(adbUnfinishedNarudzba);
    mSortedOrdersListView.setAdapter(adbOrderedNarudzba);

    // spinner
    spKrilo = findViewById(R.id.spinnerKrilo);
    spKat = findViewById(R.id.spinnerKat);

    spinnerAdapterResultKat = new ArrayAdapter<String>
            (this, R.layout.custom_spinner_item, string_kat);

    spinnerAdapterSort = new ArrayAdapter<String>
            (this, R.layout.custom_spinner_item, string_sortBy);

    spinnerAdapterResultKrilo = new ArrayAdapter<String>
            (this, R.layout.custom_spinner_item, string_krilo);

    spinnerAdapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spKrilo.setAdapter(spinnerAdapterResultKrilo);

    spinnerAdapterResultKat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spKat.setAdapter(spinnerAdapterResultKat);

    spinnerAdapterResultKat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


    spKrilo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedItem = spKrilo.getSelectedItem().toString();
        setKrilo(selectedItem);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

    spKat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedItem = spKat.getSelectedItem().toString();
        setKat(selectedItem);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });


    btnSearch = findViewById(R.id.btnSortSearch);
    btnSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        applyLocalSearch();
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


    mOrderListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Narudzba narudzba = mOrderList.get(position);
        startDetailsActivity(narudzba);
      }
    });

    mUnfinishedOrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Narudzba narudzba = mUnfinishedOrderList.get(position);
        startDetailsActivity(narudzba);
      }
    });

    mSortedOrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Narudzba narudzba = mSortedOrdersList.get(position);
        startDetailsActivity(narudzba);

      }
    });

    listUpdateCallback();
  }

  private void listUpdateCallback() {
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
            applyLocalSearch();
          } else {
            Toast.makeText(MenuActivity.this, "Lista narudzbi je prazna!", Toast.LENGTH_SHORT).show();
          }
        } else {
          Toast.makeText(MenuActivity.this, "Lista narudzbi je prazna!", Toast.LENGTH_SHORT).show();
        }

        showOrderByStatus(cbUnfinishedOrders.isChecked());
      }
    };
  }

  private void setKrilo(String selectedItem) {
    searchValueKrilo = selectedItem;
    /*
    if (selectedItem.contains("kat")) {
      spSortResult.setAdapter(spinnerAdapterResultKat);
      searchType = Constants.TRAZI_PO_KATU;
    } else if (selectedItem.contains("krilo")) {
      spSortResult.setAdapter(spinnerAdapterResultKrilo);
      searchType = Constants.TRAZI_PO_KRILU;
    }
    */
  }


  private void showOrderByStatus(boolean status) {
    if (status) {
      llListOrders.setVisibility(View.GONE);
      llListUnfinishedOrders.setVisibility(View.VISIBLE);
      mUnfinishedOrderList.clear();
      for (Narudzba item : mOrderList) {
        if (!item.isIzvrsena()) {
          mUnfinishedOrderList.add(item);
        }
      }
      adbUnfinishedNarudzba.notifyDataSetChanged();

    } else {
      llListOrders.setVisibility(View.VISIBLE);
      llListUnfinishedOrders.setVisibility(View.GONE);
      adbNarudzba.notifyDataSetChanged();

    }
  }


  private void setKat(String selectedItem) {

    if (selectedItem.contains("1")) {
      searchValueKat = "1";
    } else if (selectedItem.contains("2")) {
      searchValueKat = "2";
    } else if (selectedItem.contains("3")) {
      searchValueKat = "3";
    }

    /*
    if (searchType == Constants.TRAZI_PO_KRILU) {
      searchValue = selectedItem;
    } else if (searchType == Constants.TRAZI_PO_KATU) {
      if (selectedItem.contains("1")) {
        searchValue = "1";
      } else if (selectedItem.contains("2")) {
        searchValue = "2";
      } else if (selectedItem.contains("3")) {
        searchValue = "3";
      }
    }
    */
  }

  private void applyLocalSearch() {

    mSortedOrdersList.clear();
    adbOrderedNarudzba.notifyDataSetChanged();
    Log.d("LocalSearch", "search value krilo: " + searchValueKrilo + "; search value kat: " + searchValueKat);
    for (Narudzba item : mOrderList) {
      //if (searchType == Constants.TRAZI_PO_KATU) {
        Log.d("LocalSearch", "Item: " + String.valueOf(item.getId()) + "; Krilo: " + item.getKrilo() + "; Kat: " + item.getKat());

        if (item.getKat() != null && item.getKrilo() != null) {
          if (item.getKat().equals(searchValueKat) && item.getKrilo().equals(searchValueKrilo)) {
            if (showOnlyUnfinishedSearchOrders) {
              if (!item.isIzvrsena()) {
                mSortedOrdersList.add(item);
              }
            } else {
              mSortedOrdersList.add(item);
            }

          }
        }
      //}
      /*else if (searchType == Constants.TRAZI_PO_KRILU) {

        if (item.getKrilo() != null) {
          if (item.getKrilo().equals(searchValue)) {
            if (showOnlyUnfinishedSearchOrders) {
              if (!item.isIzvrsena()) {
                mSortedOrdersList.add(item);
              }
            } else {
              Log.d("LocalSearch", "Finished order" + "; " + String.valueOf(item.getId()));
              mSortedOrdersList.add(item);
            }
          }
        }
      }*/
    }
    if (mSortedOrdersList.size() > 0) {
      adbOrderedNarudzba.notifyDataSetChanged();
    } else {
      if (llSortedOrder.getVisibility() == View.VISIBLE) {
        Toast.makeText(MenuActivity.this, "Sortirana lista je prazna!", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void startDetailsActivity(Narudzba narudzba) {

    Intent orderDetails = new Intent(MenuActivity.this, OrderDetailsActivity.class);
    orderDetails.putExtra("idNarudzbe", narudzba.getId().toString());
    /*
    orderDetails.putExtra("korisnikIme", narudzba.getKorisnikIme());
    orderDetails.putExtra("korisnikPrezime", narudzba.getKorisnikPrezime());
    orderDetails.putExtra("lokacija", narudzba.getLokacija());
    orderDetails.putExtra("kat", narudzba.getKat());
    orderDetails.putExtra("krilo", narudzba.getKrilo());
    orderDetails.putExtra("ukupno", String.valueOf(narudzba.getUkupno()));
    orderDetails.putExtra("nacinPlacanja", narudzba.getNacin_placanja());
    orderDetails.putExtra("napomena", narudzba.getNapomena());
    orderDetails.putExtra("datumKreiranja", narudzba.getDatum_kreiranja());
    */
    if (narudzba.isIzvrsena()) {
      orderDetails.putExtra("izvrsena", "Narudžba je izvršena!");
    } else {
      orderDetails.putExtra("izvrsena", "Narudžba nije izvršena!");
    }

    startActivityForResult(orderDetails, REQUEST_CODE);
  }

  private void showLayoutOrderList() {

    if (llOrderList.getVisibility() != View.VISIBLE) {
      llOrderList.setVisibility(View.VISIBLE);
      llSortedOrder.setVisibility(View.GONE);
    }

    progress.showDialog();
    new GetAllOrders().execute(callback);
  }

  private void showLayoutSortedOrders() {
    if (llSortedOrder.getVisibility() != View.VISIBLE) {
      llSortedOrder.setVisibility(View.VISIBLE);
      llOrderList.setVisibility(View.GONE);
    }


  }

  @SuppressLint("StaticFieldLeak")
  class GetAllOrders extends AsyncTask<ListUpdateCallback, Void, ArrayList<Narudzba>> {

    ListUpdateCallback callback;

    @Override
    protected ArrayList<Narudzba> doInBackground(ListUpdateCallback... listUpdateCallbacks) {
      callback = listUpdateCallbacks[0];
      return MSSQL.getInstance().upit_pregledSvihNarudzbi();
    }

    @Override
    protected void onPostExecute(ArrayList<Narudzba> narudzbas) {
      callback.onUpdate(narudzbas);
    }
  }

}
