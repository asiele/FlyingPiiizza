package annes.flyingpiiizza;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class CreateDishActivity extends AppCompatActivity {

    private Button createButton;
    private EditText dishNameField;
    private EditText dishPriceField;

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private DishDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createButton = (Button) findViewById(R.id.createDish);
        dishNameField = (EditText) findViewById(R.id.dishName);
        dishPriceField = (EditText) findViewById(R.id.dishPrice);

        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Dish newDish = new Dish(
                        dishNameField.getText().toString(),
                        "",
                        Integer.parseInt(dishPriceField.getText().toString()),
                        new ArrayList<String>()
                );

                storeDishToDb(newDish);

                finish();
            }
        });
    }

    private void storeDishToDb(Dish dish) {
        dataSource = new DishDataSource(this);

        dataSource.open();
        dataSource.storeDish(dish);

        Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        showAllListEntries();

        dataSource.close();
    }

    private void showAllListEntries () {
        List<Dish> dishList = dataSource.getAllDishes();

        ArrayAdapter<Dish> dishArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                dishList);

        // ListView dishListView = (ListView) findViewById(R.id.listview_shopping_memos);
        // dishListView.setAdapter(dishArrayAdapter);
    }

}
