package annes.flyingpiiizza.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lbach on 26.04.2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "db";
    private static final String DISHES_TABLE_CREATE = "" +
            "CREATE TABLE dishes (" +
            "id int," +
            "price int," +
            "name varchar(255)," +
            "description varchar(500)" +
            ");";

    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DISHES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
