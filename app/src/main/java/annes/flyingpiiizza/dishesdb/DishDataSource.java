package annes.flyingpiiizza.dishesdb;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import annes.flyingpiiizza.Dish;
import annes.flyingpiiizza.Order;

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
            DishDbHelper.DB_TABLE_INGREDIENTS_COL_NAME,
            DishDbHelper.DB_TABLE_INGREDIENTS_COL_DISH_ID
    };

    private static final String[] ID_DISHES_COLUMNS = {
            DishDbHelper.DB_TABLE_DISHES_COL_ID
    };

    private static final String[] NAME_ID_INGREDIENTS_COLUMNS = {
            DishDbHelper.DB_TABLE_INGREDIENTS_COL_ID,
            DishDbHelper.DB_TABLE_INGREDIENTS_COL_NAME
    };

    private static final String[] ID_ = {
            DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_COL_ORDERID,


    };


    private Context context;


    //Constructor
    public DishDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new DishDbHelper(context);
        this.context = context;
    }


    //Opens the Database
    public void open() {
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    //Closes the Database
    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    //Creates a dish with the given values and stores it in the database
    public Dish createDish(String name, String dishtype, int price, String vegetarian) {
        assert (name != null && price != 0);

        ContentValues values = new ContentValues();

        values.put(DishDbHelper.DB_TABLE_DISHES_COL_NAME, name);
        values.put(DishDbHelper.DB_TABLE_DISHES_COL_DISHTYPE, dishtype);
        values.put(DishDbHelper.DB_TABLE_DISHES_COL_PRICE, price);
        values.put(DishDbHelper.DB_TABLE_DISHES_COL_VEGETARIAN, vegetarian);

        Log.d(LOG_TAG, "values put");

        try {
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
        } catch (Exception e) {
            return null;
        }


    }

    //Stores the given Dish in the Database
    public Dish storeDish(Dish dish) {
        return createDish(dish.getName(), dish.getDishtype(), dish.getPrice(), dish.getVegetarian());
    }

    public long createOrder(String name) {
        assert (name != null);
        ContentValues values = new ContentValues();
        values.put(DishDbHelper.DB_TABLE_ORDERS_COL_NAME, name);
        Log.d(LOG_TAG, "values put");

        long id = -1;
        try {
            id = database.insert(DishDbHelper.DB_TABLE_ORDERS_NAME, null, values);
            Log.d(LOG_TAG, "inserted");
        } catch (Exception e) {
            return -1;
        }
        return id;
    }

    public boolean createOrderDishRelation(Integer orderID, Integer dishID) {
        assert (orderID != null && dishID != null);
        ContentValues values = new ContentValues();
        values.put(DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_COL_DISHID, dishID);
        values.put(DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_COL_ORDERID, orderID);
        Log.d(LOG_TAG, "values put");

        try {
            database.insert(DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_NAME, null, values);
            Log.d(LOG_TAG, "inserted");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //This Method stores the Ingredients of a given Dish in the database
    public void storeIngredients(ArrayList<String> ingredients, Dish dish) {
        int id = getIdByDish(dish);
        for (int i = 0; i < ingredients.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(DishDbHelper.DB_TABLE_INGREDIENTS_COL_NAME, ingredients.get(i));
            values.put(DishDbHelper.DB_TABLE_INGREDIENTS_COL_DISH_ID, id);
            Log.d(LOG_TAG, "values put");
            database.insert(DishDbHelper.DB_TABLE_INGREDIENTS_NAME, null, values);
            Log.d(LOG_TAG, "inserted");
        }
    }

    //Method returnes the Dish, the cursor points to
    public Dish cursorToDish(Cursor cursor) {
        assert (cursor != null);
        int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_ID);
        int idName = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_NAME);
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

    //A List of all Dishes stored in the Database is returned
    public List<Dish> getAllDishes() {
        List<Dish> dishList = new ArrayList<>();

        Cursor cursor = database.query(DishDbHelper.DB_TABLE_DISHES_NAME,
                DISHES_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        Dish dish;

        while (!cursor.isAfterLast()) {
            dish = cursorToDish(cursor);
            dishList.add(dish);
            Log.d(LOG_TAG, ", Inhalt: " + dish.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return dishList;
    }

    //A List of all Ingredients, that belong to a given Dish id, is returned
    public List<String> getAllIngredientsByDishID(int id) {
        List<String> ingredients = new ArrayList<>();

        Cursor cursor = database.query(DishDbHelper.DB_TABLE_INGREDIENTS_NAME,
                NAME_ID_INGREDIENTS_COLUMNS, DishDbHelper.DB_TABLE_INGREDIENTS_COL_DISH_ID
                        + "=" + id, null, null, null, null);

        cursor.moveToFirst();
        String ingredient;

        while (!cursor.isAfterLast()) {
            int index = cursor.getColumnIndex(DishDbHelper.DB_TABLE_INGREDIENTS_COL_NAME);
            ingredient = cursor.getString(index);
            ingredients.add(ingredient);
            cursor.moveToNext();
        }

        return ingredients;
    }

    //A List of all Dish IDs is returned
    public List<Integer> getAllIDs() {
        List<Integer> idList = new ArrayList<>();

        Cursor cursor = database.query(DishDbHelper.DB_TABLE_DISHES_NAME,
                ID_DISHES_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        int currentID;
        while (!cursor.isAfterLast()) {
            int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_ID);
            currentID = cursor.getInt(idIndex);
            idList.add(currentID);
            cursor.moveToNext();
        }
        cursor.close();
        return idList;
    }

    //All Dish Names are returned as an String Array
    public String[] getAllDishesNamesAsStringArray() {
        String[] array = {};
        ArrayList<String> dishesNamesList = new ArrayList<>();
        for (Dish dish : getAllDishes()) {
            dishesNamesList.add(dish.getName());
        }
        return dishesNamesList.toArray(array);
    }

    //All Dish Types are returned as an String Array
    public String[] getAllDishesTypesAsStringArray() {
        String[] array = {};
        ArrayList<String> dishesTypesList = new ArrayList<>();
        for (Dish dish : getAllDishes()) {
            dishesTypesList.add(dish.getDishtype());
        }
        return dishesTypesList.toArray(array);
    }

    //All Dish Prices are returned as an Integer Array
    public Integer[] getAllDishesPricesAsIntegerArray() {
        Integer[] array = {};
        ArrayList<Integer> dishesPriceList = new ArrayList<>();
        for (Dish dish : getAllDishes()) {
            dishesPriceList.add(dish.getPrice());
        }
        return dishesPriceList.toArray(array);
    }

    //All vegetarian values are returned as an String array
    public String[] getAllDishesVegetarianAsStringArray() {
        String[] array = {};
        ArrayList<String> dishesVegetarianList = new ArrayList<>();
        for (Dish dish : getAllDishes()) {
            dishesVegetarianList.add(dish.getVegetarian());
        }
        return dishesVegetarianList.toArray(array);
    }

    //A dish is returned by its id
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

    //This method returnes the id of a stored dish, or -1
    public int getIdByDish(Dish dish) {
        int id = -1;
        Cursor cursor = database.query(DishDbHelper.DB_TABLE_DISHES_NAME,
                DISHES_COLUMNS, DishDbHelper.DB_TABLE_DISHES_COL_NAME + "=\"" + dish.getName() + "\" AND "
                        + DishDbHelper.DB_TABLE_DISHES_COL_PRICE + "=\"" + dish.getPrice() + "\" AND  " +
                        DishDbHelper.DB_TABLE_DISHES_COL_DISHTYPE + "=\"" + dish.getDishtype() + "\" AND  " +
                        DishDbHelper.DB_TABLE_DISHES_COL_VEGETARIAN + "=\"" + dish.getVegetarian() + "\";",
                null, null, null, null);
        if (cursor == null) {
            Log.d(LOG_TAG, "cursor null");
            return id;
        }
        cursor.moveToFirst();
        int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_ID);
        id = cursor.getInt(idIndex);
        cursor.close();
        return id;
    }

    //This method deletes a Dish and all ingredients of this Dish
    public boolean deleteDishByID(int id) {

        if (!isDishInAnyOrder(id)) {
            database.delete(DishDbHelper.DB_TABLE_INGREDIENTS_NAME,
                    DishDbHelper.DB_TABLE_INGREDIENTS_COL_DISH_ID
                            + "=\"" + id + "\"", null);

            database.delete(DishDbHelper.DB_TABLE_DISHES_NAME,
                    DishDbHelper.DB_TABLE_DISHES_COL_ID
                            + "=\"" + id + "\"", null);

            Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id);
            return true;
        } else {
           return false;
        }
    }

    //Search Query method
    public List<Dish> getAllDishesByQuery(String query) {
        List<Dish> dishList = new ArrayList<>();

        Cursor cursor = database.query(DishDbHelper.DB_TABLE_DISHES_NAME,
                DISHES_COLUMNS, DishDbHelper.DB_TABLE_DISHES_COL_NAME + " LIKE '" + query + "%' OR "
                        + DishDbHelper.DB_TABLE_DISHES_COL_NAME + " LIKE '% " + query + "%'",
                null, null, null, null);

        cursor.moveToFirst();
        Dish dish;

        while (!cursor.isAfterLast()) {
            dish = cursorToDish(cursor);
            dishList.add(dish);
            Log.d(LOG_TAG, ", Inhalt: " + dish.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return dishList;
    }

    //This Method returns the ID of a dish by its name
    public Integer getIdByDishName(String name) {
        List<Integer> idList = new ArrayList<>();

        Cursor cursor = database.query(DishDbHelper.DB_TABLE_DISHES_NAME,
                ID_DISHES_COLUMNS, DishDbHelper.DB_TABLE_DISHES_COL_NAME + " = '" + name + "'",
                null, null, null, null);

        cursor.moveToFirst();
        int currentID;
        while (!cursor.isAfterLast()) {
            int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_DISHES_COL_ID);
            currentID = cursor.getInt(idIndex);
            idList.add(currentID);
            cursor.moveToNext();
        }
        cursor.close();
        if (idList.size() != 1) {
            return -1;
        } else {
            return idList.get(0);
        }
    }

    public List<Dish> getAllDishesByOrderID(int orderId) {
        if (orderId != -1) {
            List<Integer> dishesIds = new ArrayList<Integer>();
            List<Dish> dishesList = new ArrayList<Dish>();
            String[] dishIdColumn = {DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_COL_DISHID};

            Cursor cursor = database.query(DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_NAME,
                    dishIdColumn, DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_COL_ORDERID
                            + " = " + orderId, null, null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_COL_DISHID);
                int id = cursor.getInt(idIndex);
                dishesIds.add(id);
                cursor.moveToNext();
            }

            cursor.close();

            for (int dishId : dishesIds) {
                dishesList.add(getDishByID(dishId));
            }
            return dishesList;
        } else {
            return null;
        }
    }

    public int calculateOrderCost(int id) {
        if (id != -1) {
            List<Dish> dishList = getAllDishesByOrderID(id);
            int orderCost = 0;
            for (Dish dish : dishList) {
                orderCost = orderCost + dish.getPrice();
            }
            return orderCost;
        } else {
            return 0;
        }
    }

    public String[] getAllOrderNames() {
        String[] nameOrderColumn = { DishDbHelper.DB_TABLE_ORDERS_COL_NAME };
        List<String> allOrderNames = new ArrayList<String>();

        Cursor cursor = database.query(DishDbHelper.DB_TABLE_ORDERS_NAME, nameOrderColumn, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_ORDERS_COL_NAME);
            String order = cursor.getString(idIndex);
            allOrderNames.add(order);
            cursor.moveToNext();
        }
        cursor.close();

        String[] allOrderNamesArray = new String[allOrderNames.size()];
        allOrderNames.toArray(allOrderNamesArray);

        return allOrderNamesArray;
    }

    public Integer[] getAllOrderIds() {
        String[] nameOrderColumn = { DishDbHelper.DB_TABLE_ORDERS_COL_ID };
        List<Integer> allOrderIds = new ArrayList<Integer>();

        Cursor cursor = database.query(DishDbHelper.DB_TABLE_ORDERS_NAME, nameOrderColumn, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_ORDERS_COL_ID);
            Integer id = cursor.getInt(idIndex);
            allOrderIds.add(id);
            cursor.moveToNext();
        }
        cursor.close();

        Integer[] allOrderIdsInteger = new Integer[allOrderIds.size()];
        allOrderIds.toArray(allOrderIdsInteger);
        return allOrderIdsInteger;
    }

    public Integer[] getAllOrderTotalCost() {
        List<Integer> totalCostList = new ArrayList<Integer>();
        for (Integer totalCost: getAllOrderIds()) {
            totalCostList.add(calculateOrderCost(totalCost));
        }
        Integer[] totalCostArray = new Integer[totalCostList.size()];
        totalCostList.toArray(totalCostArray);
        return totalCostArray;
    }

    public String getNameOfOrderById(int id) {
        String[] nameColumn = {DishDbHelper.DB_TABLE_ORDERS_COL_NAME};
        String name = "";
        Cursor cursor = database.query(DishDbHelper.DB_TABLE_ORDERS_NAME,
                nameColumn, DishDbHelper.DB_TABLE_ORDERS_COL_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_ORDERS_COL_NAME);
        name = cursor.getString(idIndex);
        cursor.close();
        return name;
    }

    public boolean isDishInAnyOrder(int id) {
        List<Integer> foundIntegers = new ArrayList<Integer>();
        String[] column = {DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_COL_ORDERID };
        Cursor cursor = database.query(DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_NAME,
                column, DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_COL_ORDERID + " = " +id,
                null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            int idIndex = cursor.getColumnIndex(DishDbHelper.DB_TABLE_ORDERS_DISHES_RELATION_COL_ORDERID);
            Integer foundId = cursor.getInt(idIndex);
            foundIntegers.add(foundId);
            cursor.moveToNext();
        }

        cursor.close();
        if (foundIntegers.size() > 0) return false;
        else return true;
    }
}



