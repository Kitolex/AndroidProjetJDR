package ca.uqac.projetjdr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ca.uqac.projetjdr.jdr.Attribut;
import ca.uqac.projetjdr.jdr.FichePersonnage;
import ca.uqac.projetjdr.jdr.exception.ValeurImpossibleException;

/**
 * Created by leopo on 24/03/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DIM";
    private static final int DATABASE_VERSION = 13;
    private static final String DATABASE_NAME = "JDR";

    private static final String TABLE_FICHE = "fiches";
    private static final String TABLE_ATTRIBUT = "attributs";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // Attribut fiche
    private static final String KEY_ATTR_NAME = "name";

    // Attribut column
    private static final String KEY_FICHE_NAME = "name";
    private static final String KEY_VALUE = "value";
    private static final String KEY_ID_PARENT = "parent";
    private static final String KEY_ID_FICHE = "personnage";

    // Table Create Statements
    // Attribut table create statement
    private static final String CREATE_TABLE_ATTRIBUT = "CREATE TABLE "
            + TABLE_ATTRIBUT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ATTR_NAME
            + " TEXT," + KEY_VALUE + " TEXT," + KEY_ID_FICHE + " INTEGER,"  + KEY_ID_PARENT + " INTEGER,"+ KEY_CREATED_AT
            + " DATETIME" + ")";

    // Fiche table create statement
    private static final String CREATE_TABLE_FICHE = "CREATE TABLE "
            + TABLE_FICHE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FICHE_NAME
            + " TEXT," + KEY_CREATED_AT
            + " DATETIME" + ")";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FICHE);
        db.execSQL(CREATE_TABLE_ATTRIBUT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FICHE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTRIBUT);
        onCreate(db);
    }


    // FICHE PERSONNAGE-----------------------------------------------------------------------------

    /*
     * Creating a fiche
     */
    public long createFiche(FichePersonnage fiche) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FICHE_NAME, fiche.getNomPersonnage());
        values.put(KEY_CREATED_AT, getDateTime());

        long fiche_id = db.insert(TABLE_FICHE, null, values);

        for (Attribut attr: fiche.getListeAttributs() ) {
            createAttribut(attr, ""+fiche_id, "");
        }
        return fiche_id;
    }

    /*
    * get single fiche
    */
    public FichePersonnage getFiche(long fiche_id) {
        FichePersonnage f = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FICHE
                + " WHERE " + KEY_ID + " = " + fiche_id;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();

        try {
            List<Attribut> attrs = null;
            f = new FichePersonnage(c.getString(c.getColumnIndex(KEY_FICHE_NAME)));
            f.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            f.listeAttributs = getAllAttributsFromFiche(f);

        }catch(ValeurImpossibleException e){
            e.printStackTrace();
        }
        c.close();

        return f;
    }

    /*
    * getting all fiches
    * */
    public List<FichePersonnage> getAllFiches() {

        List<FichePersonnage> fiches = new ArrayList<FichePersonnage>();
        String selectQuery = "SELECT  * FROM " + TABLE_FICHE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                try {
                    FichePersonnage f = new FichePersonnage(c.getString(c.getColumnIndex(KEY_FICHE_NAME)));
                    f.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                    f.listeAttributs = getAllAttributsFromFiche(f);

                    fiches.add(f);
                }catch(ValeurImpossibleException e){
                    e.printStackTrace();
                }
            } while (c.moveToNext());
        }

        c.close();
        return fiches;
    }

    /*
     * Updating a fiche
     */
    public int updateFiche(FichePersonnage fiche) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FICHE_NAME, fiche.getNomPersonnage());

        return db.update(TABLE_FICHE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(fiche.getId()) });
    }

    /*
    * Deleting a fiche
    */
    public void deleteFiche(long fiche_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FICHE, KEY_ID + " = ?",
                new String[] { String.valueOf(fiche_id) });
    }

    // ATTRIBUT-------------------------------------------------------------------------------------

    /*
     * Creating an attribut
     */
    public long createAttribut(Attribut attribut, String ficheId, String idParent) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ATTR_NAME, attribut.getNom());
        values.put(KEY_VALUE, attribut.getValeur());
        values.put(KEY_CREATED_AT, getDateTime());
        if(!ficheId.equals(""))
            values.put(KEY_ID_FICHE, ficheId);
        if(!idParent.equals(""))
            values.put(KEY_ID_PARENT, idParent);

        long attribut_id = db.insert(TABLE_ATTRIBUT, null, values);

        for (Attribut a: attribut.getListeSousAttributs()  ) {
            createAttribut(a, "", ""+attribut_id);
        }

        return attribut_id;
    }


    /*
    * get single attribut
    */
    public Attribut getAttribut(long attr_id) {
        Attribut a = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FICHE
                + " WHERE " + KEY_ID + " = " + attr_id;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        try {
            a = new Attribut(c.getString(c.getColumnIndex(KEY_ATTR_NAME)), c.getString(c.getColumnIndex(KEY_VALUE)));
            a.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            a.setListeSousAttributs(getAllSousAttribut(a));
        }catch (ValeurImpossibleException e){
            e.printStackTrace();
        }
        c.close();
        return a;
    }


    /*
   * getting all attributs from a fiche
   * */
    public List<Attribut> getAllAttributsFromFiche(FichePersonnage fiche) {

        List<Attribut> attrs = new ArrayList<Attribut>();
        String selectQuery = "SELECT  * FROM " + TABLE_ATTRIBUT;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                if(c.getInt(c.getColumnIndex(KEY_ID_FICHE)) == fiche.getId()){
                    try {
                        Attribut a = new Attribut(c.getString(c.getColumnIndex(KEY_ATTR_NAME)), c.getString(c.getColumnIndex(KEY_VALUE)));
                        a.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                        a.setListeSousAttributs(getAllSousAttribut(a));

                        attrs.add(a);
                    }catch(ValeurImpossibleException e){
                        e.printStackTrace();
                    }
                }
            } while (c.moveToNext());
        }

        c.close();
        return attrs;
    }

    /*
    * Get all attribut which have the parameter as a parent
     */
    public List<Attribut> getAllSousAttribut(Attribut attr){
        List<Attribut> attrs = new ArrayList<Attribut>();
        String selectQuery = "SELECT  * FROM " + TABLE_ATTRIBUT;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                if(c.getInt(c.getColumnIndex(KEY_ID_PARENT)) == attr.getId()){
                    try {
                        Attribut a = new Attribut(c.getString(c.getColumnIndex(KEY_ATTR_NAME)), c.getString(c.getColumnIndex(KEY_VALUE)));
                        a.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                        a.setListeSousAttributs(getAllSousAttribut(a));

                        attrs.add(a);
                    }catch(ValeurImpossibleException e){
                        e.printStackTrace();
                    }
                }
            } while (c.moveToNext());
        }

        c.close();
        return attrs;
    }

    /*
    * Updating an attribut
    */
    public int updateAttribut(Attribut attr) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ATTR_NAME, attr.getNom());
        values.put(KEY_VALUE, attr.getValeur());

        return db.update(TABLE_ATTRIBUT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(attr.getId()) });
    }

    /*
    * Deleting an attribut
    */
    public void deleteAttribut(long attr_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        Attribut attr = getAttribut(attr_id);

        for (Attribut a:  attr.getListeSousAttributs()) {
            deleteAttribut(a.getId());
        }

        db.delete(TABLE_ATTRIBUT, KEY_ID + " = ?",
                new String[] { String.valueOf(attr_id) });
    }



    // closing database
    public void closeDB() {

        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }





    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
