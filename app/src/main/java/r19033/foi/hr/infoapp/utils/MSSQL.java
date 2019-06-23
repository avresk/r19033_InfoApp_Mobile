package r19033.foi.hr.infoapp.utils;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import r19033.foi.hr.infoapp.models.Narudzba;
import r19033.foi.hr.infoapp.models.StavkaNarudzbe;

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

      Class.forName(DRIVER_JTDS).newInstance();
      Log.d("SQL", "Connect to: " + CONNECTION_STRING);
      conn = DriverManager.getConnection(CONNECTION_STRING, USER, PWD);
    }
  }

  public Narudzba upit_pregledSvihStavkiNaruzbe(long id) {
    ArrayList<Narudzba> lista = new ArrayList<Narudzba>();

    ArrayList<StavkaNarudzbe> stavkeNarudzbe = new ArrayList<>();
    boolean initialLoadDone = false;
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
            "stavka_narudzbe.kolicina as kolicina, " +
            "dbo.korisnik.ime as imeKorisnika, " +
            "dbo.korisnik.prezime as prezimeKorisnika, " +
            "dbo.narudzba.datum_kreiranja as datumKreiranja, " +
            "dbo.narudzba.nacin_placanja as nacinPlacanja, " +
            "dbo.narudzba.napomena as napomena, " +
            "dbo.narudzba.ukupno as ukupno, " +
            "artikl.naziv as nazivArtikla, " +
            "stavka_narudzbe.kolicina as kolicina, " +
            "zavrsena, lokacija from stavka_narudzbe inner join " +
            "dbo.narudzba on narudzba.narudzba_id=stavka_narudzbe.narudzba " +
            "LEFT OUTER JOIN dbo.korisnik on narudzba.korisnik_id=dbo.korisnik.korisnik_id " +
            "INNER JOIN artikl on stavka_narudzbe.artikl_id=artikl.artikl_id where " +
            "narudzba_id =" + id + ";";
    Narudzba item = new Narudzba();
    try {
      if (!conn.isClosed() && conn != null) {
        ResultSet rs;

        rs = stmt.executeQuery(query);
        while (rs.next()) {

          if (!initialLoadDone) {
            item.setId((long) rs.getInt("narudzba"));
            item.setKorisnikIme(rs.getString("imeKorisnika"));
            item.setKorisnikPrezime(rs.getString("prezimeKorisnika"));
            item.setLokacija(rs.getString("lokacija"));
            String[] loc = SortLocation.getInstance().returnLocation(item.getLokacija());
            item.setKat(loc[0]);
            item.setKrilo(loc[1]);
            Log.d("SQLtest", rs.getString("nazivArtikla"));
            item.setUkupno(rs.getFloat("ukupno"));
            item.setDatum_kreiranja(rs.getString("datumKreiranja"));
            item.setNacin_placanja(rs.getString("nacinPlacanja"));
            item.setNapomena(rs.getString("napomena"));
            int zavrsena = rs.getInt("zavrsena");
            if (zavrsena == 0) {
              item.setIzvrsena(false);
            } else {
              item.setIzvrsena(true);
            }
          }
          StavkaNarudzbe stavka = new StavkaNarudzbe();
          stavka.setNaziv(rs.getString("nazivArtikla"));
          stavka.setKolicina(rs.getFloat("kolicina"));
          stavkeNarudzbe.add(stavka);
          //Log.d("SQL", item.getId() + ";" + item.getKorisnikIme() + ";" + item.getLokacija());



        }
        item.setStavkeNarudzbe(stavkeNarudzbe);
        lista.add(item);
        Log.d("SQLtest", "Items size: " + String.valueOf(lista.size()) + "; Stavke size: " + String.valueOf(stavkeNarudzbe.size()));
        stmt.close();
        //conn.close();
        return item;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
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
          String[] loc = SortLocation.getInstance().returnLocation(item.getLokacija());
          item.setKat(loc[0]);
          item.setKrilo(loc[1]);
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
        //conn.close();
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
        //conn.close();
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
            //conn.close();
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

    String query = "UPDATE dbo.narudzba SET dbo.narudzba.zavrsena=1,dbo.narudzba.datum_zavrsetka=getdate() WHERE dbo.narudzba.narudzba_id=" + id + ";";

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
