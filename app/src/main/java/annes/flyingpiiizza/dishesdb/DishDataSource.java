package annes.flyingpiiizza.dishesdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import annes.flyingpiiizza.Dish;

/**
 * Created by Johanna on 28.04.2017.
 */

public class DishDataSource {

    private static final String LOG_TAG = DishDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private DishDbHelper dbHelper;

    private String[] columns = {
            DishDbHelper.COLUMN_ID,
            DishDbHelper.COLUMN_NAME,
            DishDbHelper.COLUMN_DESCRIPTION,
            DishDbHelper.COLUMN_PRICE,
    };


    public DishDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new DishDbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public Dish createDish(String name, String description, int price) {
        assert(name != null && price != 0);
        ContentValues values = new ContentValues();
        values.put(DishDbHelper.COLUMN_NAME, name);
        values.put(DishDbHelper.COLUMN_DESCRIPTION, description);
        values.put(DishDbHelper.COLUMN_PRICE, price);
        Log.d(LOG_TAG, "values put");
        long insertId = (database.insert(DishDbHelper.TABLE_DISHES, null, values));
        Log.d(LOG_TAG, "inserted");
        Cursor cursor = database.query(DishDbHelper.TABLE_DISHES, columns,
                DishDbHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
        if (cursor == null) Log.d(LOG_TAG, "cursor null");
        cursor.moveToFirst();
        Dish dish = cursorToDish(cursor);
        cursor.close();
        Log.d(LOG_TAG, "cursor closed");
        return dish;
    }

    public void storeDish(Dish dish) {
        //TODO
        createDish(dish.getName(), dish.getDescription(), dish.getPrice());
    }

    public Dish cursorToDish(Cursor cursor) {
        assert(cursor != null);
        int idIndex = cursor.getColumnIndex(DishDbHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(DishDbHelper.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(DishDbHelper.COLUMN_DESCRIPTION);
        int idPrice = cursor.getColumnIndex(DishDbHelper.COLUMN_PRICE);

        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);
        int price = cursor.getInt(idPrice);
        long id = cursor.getLong(idIndex);

        Dish dish = new Dish(name, description, price, null);
        return dish;
    }

    public List<Dish> getAllDishes() {
        List<Dish> dishList = new ArrayList<>();

        Cursor cursor = database.query(DishDbHelper.TABLE_DISHES,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Dish dish;

        while(!cursor.isAfterLast()) {
            dish = cursorToDish(cursor);
            dishList.add(dish);
            Log.d(LOG_TAG, ", Inhalt: " + dish.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return dishList;
    }

    public String[] getAllDishesNamesAsStringArray() {
        String[] array = {};
        ArrayList<String> dishesNamesList = new ArrayList<>();
        for (Dish dish: getAllDishes()) {
            dishesNamesList.add(dish.getName());
        }
        return dishesNamesList.toArray(array);
    }

    public String[] getAllDishesDescriptionAsStringArray() {
        String[] array = {};
        ArrayList<String> dishesDescriptionList = new ArrayList<>();
        for (Dish dish: getAllDishes()) {
            dishesDescriptionList.add(dish.getDescription());
        }
        return dishesDescriptionList.toArray(array);
    }

    public Integer[] getAllDishesPricesAsStringArray(){
        Integer[] array = {};
        ArrayList<Integer> dishesPriceList = new ArrayList<>();
        for(Dish dish: getAllDishes()) {
            dishesPriceList.add(dish.getPrice());
        }
        return dishesPriceList.toArray(array);
    }
}

