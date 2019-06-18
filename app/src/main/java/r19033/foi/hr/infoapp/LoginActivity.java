package r19033.foi.hr.infoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

  private LoginRequestCallback callback;
  public String username = "";
  public String password = "";

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
    btnLogin = findViewById(R.id.btnLogin);
    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if (username.trim().length() > 0 && password.trim().length() > 0) {

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
        Toast.makeText(LoginActivity.this, "Lozinka se ne podudara s korisnickim imenom", Toast.LENGTH_SHORT).show();
        break;
      }
      case Constants.PRIJAVA_USPJESNA: {
        Toast.makeText(LoginActivity.this, "Prijava uspjesna", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(startActivityDelayed, 2000);
        break;
      }
      default : {
        Toast.makeText(LoginActivity.this, "Greska baze", Toast.LENGTH_SHORT).show();
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
    //TODO start another acitvity
    //Intent menuActivity = new Intent(LoginActivity.this, MenuActivity.class);
    //startActivity(menuActivity);
  }


  @SuppressLint("StaticFieldLeak")
  private class GetDataAsync extends AsyncTask<LoginRequestCallback, Void, Integer>
  {

    LoginRequestCallback callback;

    @Override
    protected Integer doInBackground(LoginRequestCallback... loginRequestCallbacks) {
      callback = loginRequestCallbacks[0];
      return MSSQL.getInstance().upit_zatraziPrijavu(username, password);
    }

    @Override
    protected void onPostExecute(Integer s) {
      callback.onRequest(s);
    }
  }
}