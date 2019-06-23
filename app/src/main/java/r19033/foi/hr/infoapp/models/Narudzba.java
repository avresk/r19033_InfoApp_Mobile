package r19033.foi.hr.infoapp.models;

import java.util.ArrayList;

public class Narudzba {

    private Long id;
    private String korisnikIme;
    private String korisnikPrezime;
    private String lokacija;
    private String krilo;
    private String kat;
    private boolean izvrsena;
    private float ukupno;
    private String nacin_placanja;
    private String napomena;
    private String datum_kreiranja;
    private String datum_zavrsetka;
    private ArrayList<StavkaNarudzbe> stavkeNarudzbe;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public boolean isIzvrsena() {
        return izvrsena;
    }

    public void setIzvrsena(boolean izvrsena) {
        this.izvrsena = izvrsena;
    }

    public float getUkupno() {
        return ukupno;
    }

    public void setUkupno(float ukupno) {
        this.ukupno = ukupno;
    }

    public String getNacin_placanja() {
        return nacin_placanja;
    }

    public void setNacin_placanja(String nacin_placanja) {
        this.nacin_placanja = nacin_placanja;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public String getDatum_kreiranja() {
        return datum_kreiranja;
    }

    public void setDatum_kreiranja(String datum_kreiranja) {
        this.datum_kreiranja = datum_kreiranja;
    }

    public String getDatum_zavrsetka() {
        return datum_zavrsetka;
    }

    public void setDatum_zavrsetka(String datum_zavrsetka) {
        this.datum_zavrsetka = datum_zavrsetka;
    }

    public String getKorisnikIme() {
        return korisnikIme;
    }

    public void setKorisnikIme(String korisnikIme) {
        this.korisnikIme = korisnikIme;
    }

    public String getKorisnikPrezime() {
        return korisnikPrezime;
    }

    public void setKorisnikPrezime(String korisnikPrezime) {
        this.korisnikPrezime = korisnikPrezime;
    }

    public String getKrilo() {
        return krilo;
    }

    public void setKrilo(String krilo) {
        this.krilo = krilo;
    }

    public String getKat() {
        return kat;
    }

    public void setKat(String kat) {
        this.kat = kat;
    }

    public ArrayList<StavkaNarudzbe> getStavkeNarudzbe() {
        return stavkeNarudzbe;
    }

    public void setStavkeNarudzbe(ArrayList<StavkaNarudzbe> stavkeNarudzbe) {
        this.stavkeNarudzbe = stavkeNarudzbe;
    }
}
