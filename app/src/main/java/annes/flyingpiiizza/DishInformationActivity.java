package annes.flyingpiiizza;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class DishInformationActivity extends AppCompatActivity {

    private static final String LOG_TAG = DishDataSource.class.getSimpleName();
    private Button back;
    private Button delete;
    private TextView name;
    private TextView price;
    private TextView dishType;
    private TextView vegetarian;
    DishDataSource dataSource;
    private int thisID;
    private ListView list;
    private ArrayList ingredients;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back = (Button) findViewById(R.id.buttonBack);
        list = (ListView) findViewById(R.id.listOfIngredientNames);
        delete = (Button) findViewById(R.id.buttonDelete);
        picture = (ImageView) findViewById(R.id.picture);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        String dishName = "";
        Bundle extras = getIntent().getExtras();
        if(extras == null) {

        } else {
            dishName = (String) extras.get("nameExtra");
        }

        dataSource = new DishDataSource(this);
        dataSource.open();

        thisID = dataSource.getIdByDishName(dishName);
        Dish dish = dataSource.getDishByID(thisID);

        name = (TextView) findViewById(R.id.nameDishInfo);
        price = (TextView) findViewById(R.id.priceDishInfo);
        dishType = (TextView) findViewById(R.id.typeDishInfo);
        vegetarian = (TextView) findViewById(R.id.vegetarianDishInfo);

        name.setText(dish.getName());
        price.setText(Integer.toString(dish.getPrice()));
        dishType.setText(dish.getDishtype());
        vegetarian.setText(dish.getVegetarian());

        ingredients = (ArrayList) dataSource.getAllIngredientsByDishID(thisID);
        final ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_ingredient, ingredients);
        list.setAdapter(adapter);
        GrowingListViewUtils.adaptListViewSize(list);

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dataSource.open();
                boolean noError = dataSource.deleteDishByID(thisID);
                if(!noError) {
                    Toast.makeText(getApplicationContext(), "Dieses Gericht kann nicht gelöscht werden, da es noch in Bestellungen verwendet wird!", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });


        File img = new File(getApplicationContext().getFilesDir().getAbsoluteFile().getAbsolutePath() + "/dishimg" + thisID + ".jpg");

        if (img.exists()) {
            picture.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
        }

        dataSource.close();


    }
}
