package r19033.foi.hr.infoapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import r19033.foi.hr.infoapp.callbacks.FinishOrderCallback;
import r19033.foi.hr.infoapp.models.Narudzba;
import r19033.foi.hr.infoapp.utils.LoadProgress;
import r19033.foi.hr.infoapp.utils.MSSQL;

public class OrderDetailsActivity extends AppCompatActivity {

  private TextView tvID;
  private TextView tvIme;
  private TextView tvPrezime;
  private TextView tvLokacija;
  private TextView tvUkupno;
  private TextView tvNacinPlacanja;
  private TextView tvNapomena;
  private TextView tvDatumKreiranja;
  private TextView tvKat;
  private TextView tvKrilo;

  private Button btnZavrsi;
  private FinishOrderCallback callback;

  private LoadProgress progress;

  Narudzba narudzba = new Narudzba();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_details);

    initialize();
    Intent intent = getIntent();
    if (intent != null) {

      if (intent.hasExtra("idNarudzbe"))narudzba.setId(Long.parseLong(intent.getStringExtra("idNarudzbe")));
      if (intent.hasExtra("korisnikIme"))narudzba.setKorisnikIme(intent.getStringExtra("korisnikIme"));
      if (intent.hasExtra("korisnikPrezime"))narudzba.setKorisnikPrezime(intent.getStringExtra("korisnikPrezime"));
      if (intent.hasExtra("lokacija"))narudzba.setLokacija(intent.getStringExtra("lokacija"));
      if (intent.hasExtra("kat"))narudzba.setKat(intent.getStringExtra("kat"));
      if (intent.hasExtra("krilo"))narudzba.setKat(intent.getStringExtra("krilo"));
      if (intent.hasExtra("ukupno"))narudzba.setUkupno(Float.parseFloat(intent.getStringExtra("ukupno")));
      if (intent.hasExtra("nacinPlacanja"))narudzba.setNacin_placanja(intent.getStringExtra("nacinPlacanja"));
      if (intent.hasExtra("napomena"))narudzba.setNapomena(intent.getStringExtra("napomena"));
      if (intent.hasExtra("datumKreiranja"))narudzba.setDatum_kreiranja(intent.getStringExtra("datumKreiranja"));
      if (intent.hasExtra("izvrsena")){
        if (intent.getStringExtra("izvrsena").equals("Narudžba je izvršena!")) {
          narudzba.setIzvrsena(true);
        } else {
          narudzba.setIzvrsena(false);
        }
      }
    }

    loadData();
  }


  private void initialize() {
    tvID = findViewById(R.id.tvOD_ID);
    tvIme = findViewById(R.id.tvOD_korisnikIme);
    tvPrezime = findViewById(R.id.tvOD_korisnikPrezime);
    tvLokacija = findViewById(R.id.tvOD_lokacija);
    tvKrilo = findViewById(R.id.tvOD_krilo);
    tvKat = findViewById(R.id.tvOD_kat);
    tvUkupno = findViewById(R.id.tvOD_ukupno);
    tvNacinPlacanja = findViewById(R.id.tvOD_nacinPlacanja);
    tvNapomena = findViewById(R.id.tvOD_napomena);
    tvDatumKreiranja = findViewById(R.id.tvOD_datumKreiranja);
    btnZavrsi = findViewById(R.id.btnOD_zavrsi);
    btnZavrsi.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        zavrsiDialog();
      }
    });

    callback = new FinishOrderCallback() {
      @Override
      public void isFinished(boolean finished) {
        progress.dissmissDialog();
        if (finished){
          Toast.makeText(OrderDetailsActivity.this, "Narudžba uspješno završena", Toast.LENGTH_SHORT).show();
          setResult(123);
          finish();
        } else {
          Toast.makeText(OrderDetailsActivity.this, "Dogodila se greška, molim ponovite", Toast.LENGTH_SHORT).show();
        }
      }
    };

    progress = new LoadProgress(OrderDetailsActivity.this);
  }

  @SuppressLint("SetTextI18n")
  private void loadData() {
    tvID.setText("ID narudzbe : " + narudzba.getId().toString());
    tvIme.setText("Ime korisnika : " + narudzba.getKorisnikIme());
    tvPrezime.setText("Prezime korisnika : " + narudzba.getKorisnikPrezime());
    tvLokacija.setText("Kabinet korisnika : " + narudzba.getLokacija());
    tvKat.setText("Kat zgrade: " + narudzba.getKat());
    tvKrilo.setText("Krilo zgrade: " + narudzba.getKrilo());
    tvUkupno.setText("Za platiti : " + String.valueOf(narudzba.getUkupno()));
    tvNacinPlacanja.setText("Nacin plačanja : " + narudzba.getNacin_placanja());
    tvNapomena.setText("Napomena : \n" + narudzba.getNapomena());
    tvDatumKreiranja.setText("Kreirano : " + narudzba.getDatum_kreiranja());
    if (narudzba.isIzvrsena()) {
      Toast.makeText(this, "Narudzba je vec izvšena", Toast.LENGTH_SHORT).show();
      btnZavrsi.setEnabled(false);
    }
  }

  @SuppressLint("StaticFieldLeak")
  class FinishOrderAsync extends AsyncTask<FinishOrderCallback, Void, Boolean> {

    FinishOrderCallback callback;

    @Override
    protected Boolean doInBackground(FinishOrderCallback... finishOrderCallbacks) {
      callback = finishOrderCallbacks[0];
      return MSSQL.getInstance().upit_zavrsiNarudzbu(narudzba.getId());
    }

    @Override
    protected void onPostExecute(Boolean finished) {
      callback.isFinished(finished);
    }
  }


  private void zavrsiDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Zavrsi");
    builder.setMessage("Da li ste sigurni da želite završiti ovu narudžbu?");
    builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        progress.showDialog();
        new FinishOrderAsync().execute(callback);

      }
    });

    builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

      }
    });

    AlertDialog alert = builder.create();
    alert.show();
  }


}
