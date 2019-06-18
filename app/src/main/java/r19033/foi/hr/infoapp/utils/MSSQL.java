package r19033.foi.hr.infoapp.utils;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MSSQL {

  private static MSSQL instance;

  public static MSSQL getInstance() {
    if (instance == null) {
      instance = new MSSQL();
      return instance;
    } else {
      return instance;
    }
  }

  private MSSQL() {

  }

  private Connection conn = null;
  private Statement stmt = null;

  private static final String URL_SERVER = "31.147.204.119:1433";
  private static final String USER = "19033_User";
  private static final String PWD = "km[i17LW";
  private static final String DB_NAME = "19033_DB";
  private static final String CONNECTION_STRING = "jdbc:jtds:sqlserver://"+ URL_SERVER + ";instance=PISERVER;databaseName=" + DB_NAME + ";encrypt=false;";
  private static final String DRIVER_JTDS = "net.sourceforge.jtds.jdbc.Driver";


  private void getConnection() throws Exception {
    if (conn == null || conn.isClosed()) {
            /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);*/

      Class.forName(DRIVER_JTDS).newInstance();
      Log.d("SQL", "Connect to: " + CONNECTION_STRING);
      conn = DriverManager.getConnection(CONNECTION_STRING, USER, PWD);
    }
  }


  public int upit_zatraziPrijavu(String username, String password) {

    try {
      getConnection();

      if (conn != null) {
        if (!conn.isClosed()) {
          conn.setAutoCommit(false);
          stmt = conn.createStatement();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    String query = "SELECT * FROM dbo.administrator where dbo.administrator.korisnicko_ime='" + username + "';";

    try {
      if (!conn.isClosed() && conn != null) {
        ResultSet rs;

        rs = stmt.executeQuery(query);

        if (rs.next()) {

          String uname = rs.getString("korisnicko_ime");
          String pwd = rs.getString("lozinka");

          if (!conn.isClosed()) {
            conn.close();
          }


          if (uname.equals(username)) {
            if (pwd.equals(password)) {
              return Constants.PRIJAVA_USPJESNA;
            } else {
              return Constants.KRIVA_LOZINKA;
            }
          }

        }
        else {
          return Constants.KORISNIK_NE_POSTOJI;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return Constants.UNKNOWN_ERROR;

  }

}
