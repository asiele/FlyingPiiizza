package annes.flyingpiiizza;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;

import static annes.flyingpiiizza.MainActivity.LOG_TAG;

/**
 * Created by Johanna on 30.04.2017.
 */

public class AllDishesActivity extends AppCompatActivity {

    ListView listView;
    DishDataSource dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_dishes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.all_dishes);
        showAllDishes();
    }

    private void showAllDishes() {
        dataSource = new DishDataSource(this);

        dataSource.open();

        List<Dish> dishList = dataSource.getAllDishes();

        ArrayAdapter<Dish> dishArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                dishList);

        if(listView == null) {
            Log.d(LOG_TAG, "list view is null !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        listView.setAdapter(dishArrayAdapter);

        dataSource.close();
    }


}
