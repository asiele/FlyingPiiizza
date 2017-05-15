package annes.flyingpiiizza.dishesdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Johanna on 28.04.2017.
 */

public class DishDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DishDbHelper.class.getSimpleName();


    public static final String DB_NAME = "dish.database";
    public static final int DB_VERSION = 2;
    public static final String TABLE_DISHES = "dishes_table";
    public static final String TABLE_INGREDIENTS = "ingredients_table";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_INGREDIENTS = "ingredients";

    public static final String SQL_CREATE_DISHES = "CREATE TABLE " + TABLE_DISHES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_PRICE + " INTEGER NOT NULL), "
            + "PRIMARY KEY (" + COLUMN_ID + ");";

    public static final String SQL_CREATE_INGREDIENTS = "CREATE TABLE " + TABLE_INGREDIENTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT NOT NULL, "
            + "PRIMARY KEY (" + COLUMN_ID + ")," + " FOREIGN KEY (" + COLUMN_ID + ") REFERENCES "
            + TABLE_DISHES + " (" + COLUMN_ID + "));";


    public DishDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_DISHES + " angelegt.");
            db.execSQL(SQL_CREATE_DISHES);
            db.execSQL(SQL_CREATE_INGREDIENTS);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
