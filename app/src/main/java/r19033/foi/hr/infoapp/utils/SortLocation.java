package r19033.foi.hr.infoapp.utils;

import java.util.HashMap;

public class SortLocation {

    private static SortLocation instance;

    public static SortLocation getInstance() {
        if (instance == null) {
            instance = new SortLocation();
            setKrilo();
            setKat();
            return instance;
        } else {
            return instance;
        }
    }

    private SortLocation() {

    }

    private static final String KAT_1 = "1";
    private static final String KAT_2 = "2";
    private static final String KAT_3 = "3";

    private static final String KRILO_ISTOK = "ISTOK";
    private static final String KRILO_JUG = "JUG";

    private static HashMap<String, String> krilo = new HashMap<>();
    private static HashMap<String, String> kat = new HashMap<>();

    private static void setKrilo() {
        krilo.put("51", KRILO_ISTOK);
        krilo.put("52", KRILO_ISTOK);
        krilo.put("53", KRILO_ISTOK);
        krilo.put("72", KRILO_ISTOK);
        krilo.put("73", KRILO_ISTOK);
        krilo.put("74", KRILO_ISTOK);
        krilo.put("75", KRILO_ISTOK);
        krilo.put("108", KRILO_ISTOK);
        krilo.put("109", KRILO_ISTOK);
        krilo.put("110", KRILO_ISTOK);
        krilo.put("111", KRILO_ISTOK);
        krilo.put("113", KRILO_ISTOK);
        krilo.put("115", KRILO_ISTOK);

        krilo.put("63", KRILO_JUG);
        krilo.put("161", KRILO_JUG);
        krilo.put("160", KRILO_JUG);
        krilo.put("60", KRILO_JUG);
        krilo.put("153", KRILO_JUG);
        krilo.put("157", KRILO_JUG);
        krilo.put("158", KRILO_JUG);
        krilo.put("155", KRILO_JUG);
        krilo.put("61", KRILO_JUG);
        krilo.put("156", KRILO_JUG);
        krilo.put("154", KRILO_JUG);
        krilo.put("159", KRILO_JUG);
        krilo.put("164", KRILO_JUG);
        krilo.put("163", KRILO_JUG);
        krilo.put("162", KRILO_JUG);
        krilo.put("58", KRILO_JUG);
        krilo.put("57", KRILO_JUG);
        krilo.put("151", KRILO_JUG);
        krilo.put("78", KRILO_JUG);
        krilo.put("81", KRILO_JUG);
        krilo.put("79", KRILO_JUG);
        krilo.put("82", KRILO_JUG);
        krilo.put("96", KRILO_JUG);
        krilo.put("107", KRILO_JUG);
        krilo.put("99", KRILO_JUG);
        krilo.put("93", KRILO_JUG);
        krilo.put("91", KRILO_JUG);
        krilo.put("106", KRILO_JUG);
        krilo.put("95", KRILO_JUG);
        krilo.put("94", KRILO_JUG);
        krilo.put("98", KRILO_JUG);
        krilo.put("90", KRILO_JUG);
        krilo.put("101", KRILO_JUG);
        krilo.put("105", KRILO_JUG);
    }

    private static void setKat() {

        //ISTOK
        kat.put("51", KAT_1);
        kat.put("52", KAT_1);
        kat.put("53", KAT_1);

        kat.put("72", KAT_2);
        kat.put("73", KAT_2);
        kat.put("74", KAT_2);
        kat.put("75", KAT_2);

        kat.put("108", KAT_3);
        kat.put("109", KAT_3);
        kat.put("110", KAT_3);
        kat.put("111", KAT_3);
        kat.put("113", KAT_3);
        kat.put("115", KAT_3);


        //JUG
        kat.put("63", KAT_1);
        kat.put("161", KAT_1);
        kat.put("160", KAT_1);
        kat.put("60", KAT_1);
        kat.put("153", KAT_1);
        kat.put("157", KAT_1);
        kat.put("158", KAT_1);
        kat.put("155", KAT_1);
        kat.put("61", KAT_1);
        kat.put("156", KAT_1);
        kat.put("154", KAT_1);
        kat.put("159", KAT_1);
        kat.put("164", KAT_1);
        kat.put("163", KAT_1);
        kat.put("162", KAT_1);
        kat.put("58", KAT_1);
        kat.put("57", KAT_1);
        kat.put("151", KAT_1);

        kat.put("78", KAT_2);
        kat.put("81", KAT_2);
        kat.put("79", KAT_2);
        kat.put("82", KAT_2);

        kat.put("96", KAT_3);
        kat.put("107", KAT_3);
        kat.put("99", KAT_3);
        kat.put("93", KAT_3);
        kat.put("91", KAT_3);
        kat.put("106", KAT_3);
        kat.put("95", KAT_3);
        kat.put("94", KAT_3);
        kat.put("98", KAT_3);
        kat.put("90", KAT_3);
        kat.put("101", KAT_3);
        kat.put("105", KAT_3);

    }

    public String[] returnLocation(String location) {

        String kat = this.kat.get(location);
        String krilo = this.krilo.get(location);
        String[] toReturn = new String[2];

        toReturn[0] = kat;
        toReturn[1] = krilo;
        return toReturn;
    }
}
