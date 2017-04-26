package annes.flyingpiiizza.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lbach on 26.04.2017.
 */

public class DatabaseInterface {
    private static DatabaseInterface dbi;
    private SQLiteDatabase db;

    private DatabaseInterface() {

    }

    public static DatabaseInterface getInstance() {
        if (dbi == null) {
            dbi = new DatabaseInterface();

            //dbOpenHelper = new DatabaseOpenHelper(getContext());
        }

        return dbi;
    }
}
