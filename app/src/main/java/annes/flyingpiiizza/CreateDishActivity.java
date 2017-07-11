package annes.flyingpiiizza;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
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
    private ArrayList ingredientList = new ArrayList<String>();
    private ListView list;
    private ImageView picture;
    private Intent pictureIntent;
    private File pictureFile;
    private Uri pictureURI;
    private Bitmap map;
    private CheckBox vegetarian;
    private boolean pictureTaken;

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
        pictureTaken = false;

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
        final Context context = this;
        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String isVegetarian;
                if(vegetarian.isChecked()) {
                    isVegetarian = "ja";
                } else {
                    isVegetarian = "nein";
                }

                Dish newDish = new Dish(
                        dishNameField.getText().toString(),
                        dishTypeField.getText().toString(),
                        Integer.parseInt(dishPriceField.getText().toString()),
                        isVegetarian,
                        new ArrayList<String>()
                );

                if (storeDishToDb(newDish)) {
                    if (ingredientList.size() > 0) {
                        storeIngredientsToDb(ingredientList, newDish);
                    }
                    finish();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Ein Gericht mit diesem Namen existiert bereits, bitte einen anderen Namen wählen.");
                    builder1.setCancelable(true);
                    builder1.setNegativeButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }


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
                    pictureFile = new File(getApplicationContext().getExternalCacheDir().getAbsoluteFile() + "/newDishImage.jpg");

                    Log.d(LOG_TAG, getApplicationContext().getFilesDir().getAbsoluteFile().getAbsolutePath() + " and " + getApplicationContext().getExternalCacheDir().getAbsoluteFile());
                    pictureURI = Uri.fromFile(pictureFile);

                    pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureURI);
                    startActivityForResult(pictureIntent, 1);

                    Log.d(LOG_TAG, "Picture taken: " + pictureFile.getAbsoluteFile());

                    pictureTaken = true;
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

    private boolean storeDishToDb(Dish dish) {
        boolean returnValue;
        dataSource = new DishDataSource(this);

        dataSource.open();
        dish = dataSource.storeDish(dish);
        storeImage(dataSource.getIdByDishName(dish.getName()));

        Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        showAllListEntries();

        dataSource.close();
        return dish != null;
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

    }

    private boolean storeImage(int id) {
        if (pictureTaken) {
            try {
                pictureFile = new File(getApplicationContext().getExternalCacheDir().getAbsoluteFile() + "/newDishImage.jpg");
                File targetPictureFile = new File(getApplicationContext().getFilesDir().getAbsoluteFile().getAbsolutePath() + "/dishimg" + id + ".jpg");

                copyFile(pictureFile, targetPictureFile);

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(getApplicationContext().getFilesDir().getAbsoluteFile().getAbsolutePath() + "/dishimg" + id + "-thumb.jpg");
                    ThumbnailUtils
                            .extractThumbnail(BitmapFactory.decodeFile(pictureFile.getAbsolutePath()), 250, 250)
                            .compress(Bitmap.CompressFormat.JPEG, 100, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                pictureFile.delete();
            } catch(Exception e) {
                Log.d(LOG_TAG, e.getMessage());
            }

            Log.d(LOG_TAG, "Moved image to " + getApplicationContext().getFilesDir().getAbsoluteFile().getAbsolutePath() + "/dishimg" + id + ".jpg");
            return true;
        } else {
            return true;
        }
    }

    private void storeIngredientsToDb(ArrayList<String> ingredients, Dish dish) {
        dataSource = new DishDataSource(this);

        dataSource.open();
        dataSource.storeIngredients(ingredients, dish);

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
