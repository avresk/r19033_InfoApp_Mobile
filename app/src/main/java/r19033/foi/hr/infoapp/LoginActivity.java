package r19033.foi.hr.infoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import r19033.foi.hr.infoapp.callbacks.LoginRequestCallback;
import r19033.foi.hr.infoapp.utils.Constants;
import r19033.foi.hr.infoapp.utils.LoadProgress;
import r19033.foi.hr.infoapp.utils.MSSQL;


public class LoginActivity extends AppCompatActivity {

  private Handler mHandler = new Handler();

  private EditText etUsername;
  private EditText etPassword;
  private Button btnLogin;
  private CheckBox cbRememberMe;

  private LoginRequestCallback callback;
  protected String mUsername = "";
  protected String mPassword = "";

  private LoadProgress progress;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    progress = new LoadProgress(LoginActivity.this);
    initialize();
  }

  private void initialize() {

    callback = new LoginRequestCallback() {
      @Override
      public void onRequest(int result) {
        progress.dissmissDialog();
        handleCallback(result);
      }
    };

    etUsername = findViewById(R.id.etUsername);
    etPassword = findViewById(R.id.etPassword);
    cbRememberMe = findViewById(R.id.cbRememberMe);

    readPrefs();
    btnLogin = findViewById(R.id.btnLogin);
    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mUsername = etUsername.getText().toString();
        mPassword = etPassword.getText().toString();

        if (mUsername.trim().length() > 0 && mPassword.trim().length() > 0) {

          new GetDataAsync().execute(callback);
          progress.showDialog();

        } else {
          Toast.makeText(LoginActivity.this, "Username i/ili password ne mogu biti prazni!", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private void handleCallback(int result) {
    switch (result) {
      case Constants.KORISNIK_NE_POSTOJI : {
        Toast.makeText(LoginActivity.this, "Korisnik ne postoji", Toast.LENGTH_SHORT).show();
        break;
      }
      case Constants.KRIVA_LOZINKA : {
        Toast.makeText(LoginActivity.this, "Lozinka se ne podudara s korisničkim imenom", Toast.LENGTH_SHORT).show();
        break;
      }
      case Constants.PRIJAVA_USPJESNA: {
        Toast.makeText(LoginActivity.this, "Prijava uspješna", Toast.LENGTH_SHORT).show();
        savePrefs();
        mHandler.postDelayed(startActivityDelayed, 2000);
        break;
      }
      default : {
        Toast.makeText(LoginActivity.this, "Greška baze", Toast.LENGTH_SHORT).show();
        break;
      }
    }
  }

  private Runnable startActivityDelayed = new Runnable() {
    @Override
    public void run() {
      LoginSuccessful();
    }
  };

  private void LoginSuccessful() {
    Intent menuActivity = new Intent(LoginActivity.this, MenuActivity.class);
    startActivity(menuActivity);
  }

  private void readPrefs() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

    if (prefs.contains(Constants.PREFS_USERNAME)) {
      mUsername = prefs.getString(Constants.PREFS_USERNAME, "");
      etUsername.setText(mUsername, TextView.BufferType.EDITABLE);
    }

    if (prefs.contains(Constants.PREFS_PASSWORD)) {
      mPassword = prefs.getString(Constants.PREFS_PASSWORD, "");
      etPassword.setText(mPassword);
    }

    if (prefs.contains(Constants.PREFS_CB)) {
      String checked = prefs.getString(Constants.PREFS_CB, "");
      if (checked.equals("checked")) {
        cbRememberMe.setChecked(true);
      }
      else {
        cbRememberMe.setChecked(false);
      }
    }
  }

  private void savePrefs() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
    SharedPreferences.Editor editor = prefs.edit();

    if (cbRememberMe.isChecked()) {
      editor.putString(Constants.PREFS_USERNAME, mUsername);
      editor.putString(Constants.PREFS_PASSWORD, mPassword);
      editor.putString(Constants.PREFS_CB, "checked");
      editor.apply();
    }
    else {
      editor.putString(Constants.PREFS_USERNAME, "");
      editor.putString(Constants.PREFS_PASSWORD, "");
      editor.putString(Constants.PREFS_CB, "unchecked");
      editor.commit();
    }
  }


  @SuppressLint("StaticFieldLeak")
  private class GetDataAsync extends AsyncTask<LoginRequestCallback, Void, Integer>
  {

    LoginRequestCallback callback;

    @Override
    protected Integer doInBackground(LoginRequestCallback... loginRequestCallbacks) {
      callback = loginRequestCallbacks[0];
      return MSSQL.getInstance().upit_zatraziPrijavu(mUsername, mPassword);
    }

    @Override
    protected void onPostExecute(Integer s) {
      callback.onRequest(s);
    }
  }
}