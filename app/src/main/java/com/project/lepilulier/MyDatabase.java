package com.project.lepilulier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MyDatabase {
    public static final String DATABASE_NAME = "MyDB";
    public static final String DATABASE_TABLE1 = "ordonnances";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE1_MID = "mid";
    public static final String TABLE1_MEDICAMENT = "med_name";
    public static final String TABLE1_DATE = "date";
    public static final String TABLE1_MATIN = "matin";
    public static final String TABLE1_MATIN_TEMPS = "matin_heure";
    public static final String TABLE1_MATIN_POSOLOGIE = "matin_posologie";
    public static final String TABLE1_MATIN_PRIS = "matin_pris";
    public static final String TABLE1_MIDI = "midi";
    public static final String TABLE1_MIDI_TEMPS = "midi_heure";
    public static final String TABLE1_MIDI_POSOLOGIE = "midi_posologie";
    public static final String TABLE1_MIDI_PRIS = "midi_pris";
    public static final String TABLE1_SOIR = "soir";
    public static final String TABLE1_SOIR_TEMPS = "soir_heure";
    public static final String TABLE1_SOIR_POSOLOGIE = "soir_posologie";
    public static final String TABLE1_SOIR_PRIS = "soir_pris";

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE1 + " (" +
                    TABLE1_MID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TABLE1_MEDICAMENT + " TEXT, " +
                    TABLE1_DATE + " INTEGER, " +
                    TABLE1_MATIN + " TEXT, " +
                    TABLE1_MATIN_TEMPS + " TEXT, " +
                    TABLE1_MATIN_POSOLOGIE + " TEXT, " +
                    TABLE1_MATIN_PRIS + " TEXT, " +
                    TABLE1_MIDI + " TEXT, " +
                    TABLE1_MIDI_TEMPS + " TEXT, " +
                    TABLE1_MIDI_POSOLOGIE + " TEXT, " +
                    TABLE1_MIDI_PRIS + " TEXT, " +
                    TABLE1_SOIR + " TEXT, " +
                    TABLE1_SOIR_TEMPS + " TEXT, " +
                    TABLE1_SOIR_POSOLOGIE + " TEXT, " +
                    TABLE1_SOIR_PRIS + " TEXT);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public MyDatabase(Context c) {
        ourContext = c;
    }

    public MyDatabase open() throws Exception {

        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public void saveOrdonnance(String medicament, int date, String matin, String matin_heure, String matin_posologie, String matin_pris,
                                 String midi, String midi_heure, String midi_posologie, String midi_pris,
                                 String soir, String soir_heure, String soir_posologie, String soir_pris) {
        ContentValues cv = new ContentValues();
        cv.put(TABLE1_MEDICAMENT, medicament);
        cv.put(TABLE1_DATE, date);
        cv.put(TABLE1_MATIN, matin);
        cv.put(TABLE1_MATIN_TEMPS, matin_heure);
        cv.put(TABLE1_MATIN_POSOLOGIE, matin_posologie);
        cv.put(TABLE1_MATIN_PRIS, matin_pris);
        cv.put(TABLE1_MIDI, midi);
        cv.put(TABLE1_MIDI_TEMPS, midi_heure);
        cv.put(TABLE1_MIDI_POSOLOGIE, midi_posologie);
        cv.put(TABLE1_MIDI_PRIS, midi_pris);
        cv.put(TABLE1_SOIR, soir);
        cv.put(TABLE1_SOIR_TEMPS, soir_heure);
        cv.put(TABLE1_SOIR_POSOLOGIE, soir_posologie);
        cv.put(TABLE1_SOIR_PRIS, soir_pris);

        ourDatabase.insert(DATABASE_TABLE1, null, cv);
    }

    public ArrayList<HashMap<String, String>> getJournalDeBord(String date) throws Exception {
        String[] columns = new String[]{TABLE1_MEDICAMENT, TABLE1_DATE,
                TABLE1_MATIN_TEMPS, TABLE1_MATIN_POSOLOGIE, TABLE1_MATIN_PRIS, TABLE1_MATIN,
                TABLE1_MIDI_TEMPS, TABLE1_MIDI_POSOLOGIE, TABLE1_MIDI_PRIS, TABLE1_MIDI,
                TABLE1_SOIR_TEMPS, TABLE1_SOIR_POSOLOGIE, TABLE1_SOIR_PRIS, TABLE1_SOIR};

        ArrayList<HashMap<String, String>> entrées = new ArrayList<>();

        Cursor c = ourDatabase.query(DATABASE_TABLE1, columns, "( " + TABLE1_MATIN_PRIS + " = ? OR " +
                TABLE1_MIDI_PRIS + " = ? OR " + TABLE1_SOIR_PRIS + " = ? ) AND ( " +
                TABLE1_DATE + " < ? )", new String[]{"0", "0", "0", date}, null, null, null);
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                //for matin
                if (c.getString(4).equals("0") && c.getString(5).equals("1")) {
                    HashMap<String, String> entrée = new HashMap<>();
                    entrée.put("medicament", c.getString(0));
                    entrée.put("date", c.getString(1));
                    entrée.put("heure", c.getString(2));
                    entrée.put("posologie", c.getString(3));

                    entrées.add(entrée);
                }

                //for midi
                if (c.getString(8).equals("0") && c.getString(9).equals("1")) {
                    HashMap<String, String> entrée = new HashMap<>();
                    entrée.put("medicament", c.getString(0));
                    entrée.put("date", c.getString(1));
                    entrée.put("heure", c.getString(6));
                    entrée.put("posologie", c.getString(7));

                    entrées.add(entrée);
                }

                //for soir
                if (c.getString(12).equals("0") && c.getString(13).equals("1")) {
                    HashMap<String, String> entrée = new HashMap<>();
                    entrée.put("medicament", c.getString(0));
                    entrée.put("date", c.getString(1));
                    entrée.put("heure", c.getString(10));
                    entrée.put("posologie", c.getString(11));

                    entrées.add(entrée);
                }
            }
        }
        c.close();

        return entrées;
    }

    public ArrayList<HashMap<String, String>> getMatin(String date) throws Exception {
        String[] columns = new String[]{TABLE1_MEDICAMENT, TABLE1_MATIN_TEMPS, TABLE1_MATIN_POSOLOGIE};

        ArrayList<HashMap<String, String>> entrées = new ArrayList<>();

        Cursor c = ourDatabase.query(DATABASE_TABLE1, columns, TABLE1_MATIN + " = ? AND " +
                TABLE1_DATE + " = ? ", new String[]{"1", date}, null, null, null);
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                HashMap<String, String> entrée = new HashMap<>();
                entrée.put("medicament", c.getString(0));
                entrée.put("heure", c.getString(1));
                entrée.put("posologie", c.getString(2));

                entrées.add(entrée);
            }
        }
        c.close();

        return entrées;
    }

    public ArrayList<HashMap<String, String>> getMidi(String date) throws Exception {
        String[] columns = new String[]{TABLE1_MEDICAMENT, TABLE1_MIDI_TEMPS, TABLE1_MIDI_POSOLOGIE};

        ArrayList<HashMap<String, String>> entrées = new ArrayList<>();

        Cursor c = ourDatabase.query(DATABASE_TABLE1, columns, TABLE1_MIDI + " = ? AND " +
                TABLE1_DATE + " = ? ", new String[]{"1", date}, null, null, null);
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                HashMap<String, String> entrée = new HashMap<>();
                entrée.put("medicament", c.getString(0));
                entrée.put("heure", c.getString(1));
                entrée.put("posologie", c.getString(2));

                entrées.add(entrée);
            }
        }
        c.close();

        return entrées;
    }

    public ArrayList<HashMap<String, String>> getSoir(String date) throws Exception {
        String[] columns = new String[]{TABLE1_MEDICAMENT, TABLE1_SOIR_TEMPS, TABLE1_SOIR_POSOLOGIE};

        ArrayList<HashMap<String, String>> entrées = new ArrayList<>();

        Cursor c = ourDatabase.query(DATABASE_TABLE1, columns, TABLE1_SOIR + " = ? AND " +
                TABLE1_DATE + " = ? ", new String[]{"1", date}, null, null, null);
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                HashMap<String, String> entrée = new HashMap<>();
                entrée.put("medicament", c.getString(0));
                entrée.put("heure", c.getString(1));
                entrée.put("posologie", c.getString(2));

                entrées.add(entrée);
            }
        }
        c.close();

        return entrées;
    }

    public ArrayList<String> getAllDatesEvents() throws Exception {
        String[] columns = new String[]{TABLE1_DATE};

        ArrayList<String> entrées = new ArrayList<>();

        Cursor c = ourDatabase.query(true, DATABASE_TABLE1, columns, null, null, null, null, null, null);
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                entrées.add(c.getString(0));
            }
        }
        c.close();

        return entrées;
    }

    public ArrayList<HashMap<String, String>> getToday(int date) throws Exception {
        String[] columns = new String[]{TABLE1_MEDICAMENT,
                TABLE1_MATIN_TEMPS, TABLE1_MATIN_POSOLOGIE, TABLE1_MATIN_PRIS, TABLE1_MATIN,
                TABLE1_MIDI_TEMPS, TABLE1_MIDI_POSOLOGIE, TABLE1_MIDI_PRIS, TABLE1_MIDI,
                TABLE1_SOIR_TEMPS, TABLE1_SOIR_POSOLOGIE, TABLE1_SOIR_PRIS, TABLE1_SOIR,
                TABLE1_MID};

        ArrayList<HashMap<String, String>> entrées = new ArrayList<>();

        Cursor c = ourDatabase.query(DATABASE_TABLE1, columns, TABLE1_DATE + " = ?",
                new String[]{"" + date}, null, null, null);
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                //for matin
                if (c.getString(4).equals("1")) {
                    HashMap<String, String> entrée = new HashMap<>();
                    entrée.put("which", "1");
                    entrée.put("medicament", c.getString(0));
                    entrée.put("heure", c.getString(1));
                    entrée.put("posologie", c.getString(2));
                    entrée.put("pris", c.getString(3));
                    entrée.put("mid", c.getString(13));

                    entrées.add(entrée);
                }

                //for midi
                if (c.getString(8).equals("1")) {
                    HashMap<String, String> entrée = new HashMap<>();
                    entrée.put("which", "2");
                    entrée.put("medicament", c.getString(0));
                    entrée.put("heure", c.getString(5));
                    entrée.put("posologie", c.getString(6));
                    entrée.put("pris", c.getString(7));
                    entrée.put("mid", c.getString(13));

                    entrées.add(entrée);
                }

                //for soir
                if (c.getString(12).equals("1")) {
                    HashMap<String, String> entrée = new HashMap<>();
                    entrée.put("which", "3");
                    entrée.put("medicament", c.getString(0));
                    entrée.put("heure", c.getString(9));
                    entrée.put("posologie", c.getString(10));
                    entrée.put("pris", c.getString(11));
                    entrée.put("mid", c.getString(13));

                    entrées.add(entrée);
                }
            }
        }
        c.close();

        return entrées;
    }

    public void takeThisMedicament(String mid, String which, String value) throws Exception {
        ContentValues cv = new ContentValues();

        if(which.equals("1")) {
            cv.put(TABLE1_MATIN_PRIS, value);
        } else if(which.equals("2")) {
            cv.put(TABLE1_MIDI_PRIS, value);
        } else if(which.equals("3")) {
            cv.put(TABLE1_SOIR_PRIS, value);
        }

        ourDatabase.update(DATABASE_TABLE1, cv, TABLE1_MID + " = ?", new String[]{mid});
    }
}
