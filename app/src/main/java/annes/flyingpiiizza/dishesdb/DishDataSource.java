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

    private static final String[] DISHES_COLUMNS = {
            DishDbHelper.DB_TABLE_DISHES_COL_ID,
            DishDbHelper.DB_TABLE_DISHES_COL_NAME,
            DishDbHelper.DB_TABLE_DISHES_COL_DISHTYPE,
            DishDbHelper.DB_TABLE_DISHES_COL_PRICE,
            DishDbHelper.DB_TABLE_DISHES_COL_VEGETARIAN
    };

    private static final String[] INGREDIENTS_COLUMNS = {
            DishDbHelper.DB_TABLE_INGREDIENTS_COL_ID,
            DishDbHelper.DB_TABLE_INGREDIENTS_COL_NAME
    };

    private static final String[] ID_DISHES_COLUMNS = {
        DishDbHelper.DB_TABLE_DISHES_COL_ID
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

    public Dish createDish(String name, String dishtype, int price, String vegetarian) {
        assert(name != null && price != 0);

        ContentValues values = new ContentValues();

        values.put(DishDbHelper.DB_TABLE_DISHES_COL_NAME, name);
        values.put(DishDbHelper.DB_TABLE_DISHES_COL_DISHTYPE, dishtype);
        values.put(DishDbHelper.DB_TABLE_DISHES_COL_PRICE, price);
        values.put(DishDbHelper.DB_TABLE_DISHES_COL_VEGETARIAN, vegetarian);

        Log.d(LOG_TAG, "values put");

        long insertId = database.insert(DishDbHelper.DB_TABLE_DISHES_NAME, null, values);

        Log.d(LOG_TAG, "inserted");

        Cursor cursor = database.query(DishDbHelper.DB_TABLE_DISHES_NAME, DISHES_COLUMNS,
                DishDbHelper.DB_TABLE_DISHES_COL_ID + "=" + insertId, null, null, null, null);

        if (cursor == null) Log.d(LOG_TAG, "cursor null");
        cursor.moveToFirst();
        Dish dish = cursorToDish(cursor);
        cursor.close();

        Log.d(LOG_TAG, "cursor closed");
        return dish;
    }

    public void storeDish(Dish dish) {
        //TODO
        createDish(dish.getName(), dish.getDishtype(), dish.getPrice(), dish.getVegetarian());
    }

    public Dish cursorToDish(Cursor cursor) {
        assert(cursor != null);
        int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_ID);
        int idName = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_NAME );
        int idDishtype = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_DISHTYPE);
        int idPrice = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_PRICE);
        int idVegetarian = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_VEGETARIAN);

        String name = cursor.getString(idName);
        String dishtype = cursor.getString(idDishtype);
        String vegetarian = cursor.getString(idVegetarian);
        int price = cursor.getInt(idPrice);
        long id = cursor.getLong(idIndex);

        Dish dish = new Dish(name, dishtype, price, vegetarian, null);
        return dish;
    }

    public List<Dish> getAllDishes() {
        List<Dish> dishList = new ArrayList<>();

        Cursor cursor = database.query(DishDbHelper.DB_TABLE_DISHES_NAME,
                DISHES_COLUMNS, null, null, null, null, null);

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

    public List<Integer> getAllIDs() {
        List<Integer> idList = new ArrayList<>();

        Cursor cursor = database.query(DishDbHelper.DB_TABLE_DISHES_NAME,
        ID_DISHES_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        int currentID;
        while(!cursor.isAfterLast()) {
            int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_ID);
            currentID = cursor.getInt(idIndex);
            idList.add(currentID);
            cursor.moveToNext();
        }
        cursor.close();
        return idList;
    }

    public String[] getAllDishesNamesAsStringArray() {
        String[] array = {};
        ArrayList<String> dishesNamesList = new ArrayList<>();
        for (Dish dish: getAllDishes()) {
            dishesNamesList.add(dish.getName());
        }
        return dishesNamesList.toArray(array);
    }

    public String[] getAllDishesTypesAsStringArray() {
        String[] array = {};
        ArrayList<String> dishesTypesList = new ArrayList<>();
        for (Dish dish: getAllDishes()) {
            dishesTypesList.add(dish.getDishtype());
        }
        return dishesTypesList.toArray(array);
    }

    public Integer[] getAllDishesPricesAsIntegerArray(){
        Integer[] array = {};
        ArrayList<Integer> dishesPriceList = new ArrayList<>();
        for(Dish dish: getAllDishes()) {
            dishesPriceList.add(dish.getPrice());
        }
        return dishesPriceList.toArray(array);
    }

    public String[] getAllDishesVegetarianAsStringArray() {
        String[] array = {};
        ArrayList<String> dishesVegetarianList = new ArrayList<>();
        for (Dish dish: getAllDishes()) {
            dishesVegetarianList.add(dish.getVegetarian());
        }
        return dishesVegetarianList.toArray(array);
    }

    public Dish getDishByID(int id) {
        Dish dish;
        Cursor cursor = database.query(DishDbHelper.DB_TABLE_DISHES_NAME,
                DISHES_COLUMNS, DishDbHelper.DB_TABLE_DISHES_COL_ID + "=" + id, null, null, null, null);
        if (cursor == null) Log.d(LOG_TAG, "cursor null");
        cursor.moveToFirst();
        dish = cursorToDish(cursor);
        cursor.close();

        Log.d(LOG_TAG, "cursor closed");
        return dish;
    }
}

