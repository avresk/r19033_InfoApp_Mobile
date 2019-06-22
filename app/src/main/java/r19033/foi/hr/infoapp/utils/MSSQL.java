package r19033.foi.hr.infoapp.utils;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import r19033.foi.hr.infoapp.models.Narudzba;

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


  public ArrayList<Narudzba> upit_pregledSvihNarudzbi() {

    ArrayList<Narudzba> lista = new ArrayList<Narudzba>();
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

    String query = "select dbo.narudzba.narudzba_id as narudzba, " +
            "dbo.korisnik.ime as imeKorisnika, " +
            "dbo.korisnik.prezime as prezimeKorisnika, " +
            "dbo.narudzba.datum_kreiranja as datumKreiranja, " +
            "dbo.narudzba.nacin_placanja as nacinPlacanja, " +
            "dbo.narudzba.napomena as napomena, " +
            "zavrsena, lokacija from narudzba inner join " +
            "dbo.korisnik on narudzba.korisnik_id=dbo.korisnik.korisnik_id;";

    try {
      if (!conn.isClosed() && conn != null) {
        ResultSet rs;

        rs = stmt.executeQuery(query);
        while (rs.next()) {
          Narudzba item = new Narudzba();
          item.setId((long) rs.getInt("narudzba"));
          item.setKorisnikIme(rs.getString("imeKorisnika"));
          item.setKorisnikPrezime(rs.getString("prezimeKorisnika"));
          item.setLokacija(rs.getString("lokacija"));
          item.setDatum_kreiranja(rs.getString("datumKreiranja"));
          item.setNacin_placanja(rs.getString("nacinPlacanja"));
          item.setNapomena(rs.getString("napomena"));
          int zavrsena = rs.getInt("zavrsena");
          if (zavrsena == 0) {
            item.setIzvrsena(false);
          } else {
            item.setIzvrsena(true);
          }
          Log.d("SQL", item.getId() + ";" + item.getKorisnikIme() + ";" + item.getLokacija());

          lista.add(item);

        }
        stmt.close();
        conn.close();
        return lista;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public ArrayList<Narudzba> upit_narudbaByID(Long id) {

    ArrayList<Narudzba> lista = new ArrayList<Narudzba>();
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

    String query = "select dbo.narudzba.narudzba_id as narudzba, " +
            "dbo.korisnik.ime as imeKorisnika, " +
            "dbo.korisnik.prezime as prezimeKorisnika, " +
            "dbo.narudzba.datum_kreiranja as datumKreiranja, " +
            "dbo.narudzba.nacin_placanja as nacinPlacanja, " +
            "dbo.narudzba.napomena as napomena, " +
            "zavrsena, lokacija from narudzba inner join " +
            "dbo.korisnik on narudzba.korisnik_id=dbo.korisnik.korisnik_id " +
            "WHERE dbo.narudzba.korisnik_id=" + id + ";";

    try {
      if (!conn.isClosed() && conn != null) {
        ResultSet rs;

        rs = stmt.executeQuery(query);
        while (rs.next()) {
          Narudzba item = new Narudzba();
          item.setId((long) rs.getInt("narudzba"));
          item.setKorisnikIme(rs.getString("imeKorisnika"));
          item.setKorisnikPrezime(rs.getString("prezimeKorisnika"));
          item.setLokacija(rs.getString("lokacija"));
          item.setDatum_kreiranja(rs.getString("datumKreiranja"));
          item.setNacin_placanja(rs.getString("nacinPlacanja"));
          item.setNapomena(rs.getString("napomena"));
          int zavrsena = rs.getInt("zavrsena");
          if (zavrsena == 0) {
            item.setIzvrsena(false);
          } else {
            item.setIzvrsena(true);
          }
          Log.d("SQL", item.getId() + ";" + item.getKorisnikIme() + ";" + item.getLokacija());

          lista.add(item);

        }
        stmt.close();
        conn.close();
        return lista;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
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

          stmt.close();
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

  public boolean upit_zavrsiNarudzbu(Long id) {
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

    String query = "UPDATE dbo.narudzba SET dbo.narudzba.zavrsena=1 WHERE dbo.narudzba.narudzba_id=" + id + ";";

    try {
      if (!conn.isClosed() && conn != null) {

        stmt.executeUpdate(query);
        stmt.close();
        conn.commit();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
