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

    public static final String DB_NANE = "dish.db";
    public static final int DB_VERSION = 2;

    public static final String DB_TABLE_DISHES_NAME = "dishes_table";
    public static final String DB_TABLE_DISHES_COL_ID = "_id";
    public static final String DB_TABLE_DISHES_COL_NAME = "name";
    public static final String DB_TABLE_DISHES_COL_DISHTYPE = "dishtype";
    public static final String DB_TABLE_DISHES_COL_PRICE = "price";
    public static final String DB_TABLE_DISHES_COL_VEGETARIAN = "vegetarian";

    public static final String DB_TABLE_INGREDIENTS_NAME = "ingredients_table";
    public static final String DB_TABLE_INGREDIENTS_COL_ID = "_id";
    public static final String DB_TABLE_INGREDIENTS_COL_NAME = "name";
    public static final String DB_TABLE_INGREDIENTS_COL_DISH_ID = "dishid";

    public static final String SQL_CREATE_TABLE_DISHES = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +        // id
                    "%s TEXT NOT NULL," +                            // name
                    "%s TEXT," +                                      // dish type
                    "%s INTEGER NOT NULL," +                         // price
                    "%s TEXT NOT NULL" +                             // vegetarian
            ")",
            DB_TABLE_DISHES_NAME,
            DB_TABLE_DISHES_COL_ID,
            DB_TABLE_DISHES_COL_NAME,
            DB_TABLE_DISHES_COL_DISHTYPE,
            DB_TABLE_DISHES_COL_PRICE,
            DB_TABLE_DISHES_COL_VEGETARIAN);

    public static final String SQL_CREATE_TABLE_INGREDIENTS = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +       // id
                    "%s TEXT NOT NULL," +                           // name
                    "%s INTEGER NOT NULL" +         // id references dishes->id
            ")",
            DB_TABLE_INGREDIENTS_NAME,
            DB_TABLE_INGREDIENTS_COL_ID,
            DB_TABLE_INGREDIENTS_COL_NAME,
            DB_TABLE_INGREDIENTS_COL_DISH_ID);


    public DishDbHelper(Context context) {
        super(context, DB_NANE, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }


/*
    public static final String DB_NAME = "dish.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_DISHES = "dishes_table";
    public static final String TABLE_INGREDIENTS = "ingredients_table";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DISHTYPE = "dishtype";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_VEGETARIAN = "vegetarian";
    public static final String COLUMN_INGREDIENTS = "ingredients";

    public static final String SQL_CREATE_DISHES = "CREATE TABLE " + TABLE_DISHES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_DISHTYPE + "TEXT, " + COLUMN_PRICE + " INTEGER NOT NULL, "
            + COLUMN_VEGETARIAN + "TEXT NOT NULL);";

    public static final String SQL_CREATE_INGREDIENTS = "CREATE TABLE " + TABLE_INGREDIENTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT NOT NULL, "
            + "PRIMARY KEY (" + COLUMN_ID + ")," + " FOREIGN KEY (" + COLUMN_ID + ") REFERENCES "
            + TABLE_DISHES + " (" + COLUMN_ID + "));";

*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate beim DbHelper jetzt aufgerufen.");

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_DISHES + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_DISHES);
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_INGREDIENTS + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_INGREDIENTS);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
