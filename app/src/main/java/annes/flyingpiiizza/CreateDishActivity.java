package annes.flyingpiiizza;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class CreateDishActivity extends AppCompatActivity {

    private Button createButton;
    private Button back;
    private Button okButton;
    private EditText dishNameField;
    private EditText dishPriceField;
    private EditText dishTypeField;
    private EditText ingredientNameField;
    List ingredientList = new ArrayList<String>();
    ListView list;

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
        okButton = (Button) findViewById(R.id.OKButton);
        ingredientNameField = (EditText) findViewById(R.id.ingredientName);
        back = (Button) findViewById(R.id.buttonBack);
        list = (ListView) findViewById(R.id.listOfIngredientNames);

        list.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

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

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_ingredient, ingredientList);
        list=(ListView)findViewById(R.id.listOfIngredientNames);
        list.setAdapter(adapter);

        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ingredientList.add(ingredientNameField.getText().toString());
                ingredientNameField.setText("");
                list.invalidateViews();
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
