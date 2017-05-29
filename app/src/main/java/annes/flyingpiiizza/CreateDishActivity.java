package annes.flyingpiiizza;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class CreateDishActivity extends AppCompatActivity {

    private Button createButton;
    private Button back;
    private Button okButton;
    private Button resetIngredients;
    private Button takePicture;
    private EditText dishNameField;
    private EditText dishPriceField;
    private EditText dishTypeField;
    private EditText ingredientNameField;
    private List ingredientList = new ArrayList<String>();
    private ListView list;
    private ImageView picture;
    private Intent pictureIntent;
    private File pictureFile;
    private Uri pictureURI;
    private Bitmap map;
    private CheckBox vegetarian;

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private DishDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createButton = (Button) findViewById(R.id.createDish);
        resetIngredients = (Button) findViewById(R.id.resetIngredients);
        dishNameField = (EditText) findViewById(R.id.dishName);
        dishPriceField = (EditText) findViewById(R.id.dishPrice);
        dishTypeField = (EditText) findViewById(R.id.dishType);
        okButton = (Button) findViewById(R.id.OKButton);
        ingredientNameField = (EditText) findViewById(R.id.ingredientName);
        back = (Button) findViewById(R.id.buttonBack);
        list = (ListView) findViewById(R.id.listOfIngredientNames);
        picture = (ImageView) findViewById(R.id.picture);
        takePicture = (Button) findViewById(R.id.takePicture);
        vegetarian = (CheckBox) findViewById(R.id.vegetarian);

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
                String isVegetarian;
                if(vegetarian.isChecked()) {
                    isVegetarian = "true";
                } else {
                    isVegetarian = "false";
                }

                Dish newDish = new Dish(
                        dishNameField.getText().toString(),
                        dishTypeField.getText().toString(),
                        Integer.parseInt(dishPriceField.getText().toString()),
                        isVegetarian,
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

        resetIngredients.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ingredientList.clear();
                ingredientNameField.setText("");
                list.invalidateViews();
            }
        });

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pictureFile = new File(getApplicationContext().getExternalCacheDir().getAbsoluteFile() + "/image.jpg");
                    pictureURI = Uri.fromFile(pictureFile);

                    pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureURI);
                    startActivityForResult(pictureIntent, 1);

                    Log.d(LOG_TAG, "Picture taken: " + pictureFile.getAbsoluteFile());
                } catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Kamera wird nicht unterstützt!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == 1) {
                Toast.makeText(getApplicationContext(), "Bild wurde gespeichert unter: " + pictureFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                map = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
                picture.setImageBitmap(map);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
