package annes.flyingpiiizza;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class DishInformation extends AppCompatActivity {

    private Button back;
    private TextView name;
    private TextView price;
    private TextView dishType;
    DishDataSource dataSource;
    private int thisID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back = (Button) findViewById(R.id.buttonBack);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int position = -1;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {

        } else {
            position = (int) extras.get("name");
        }

        dataSource = new DishDataSource(this);
        dataSource.open();
        List<Integer> idList = dataSource.getAllIDs();
        thisID = idList.get(position);
        Dish dish = dataSource.getDishByID(thisID);

        name = (TextView) findViewById(R.id.nameDishInfo);
        price = (TextView) findViewById(R.id.priceDishInfo);
        dishType = (TextView) findViewById(R.id.typeDishInfo);

        name.setText(dish.getName());
        price.setText(Integer.toString(dish.getPrice()));
        dishType.setText(dish.getDishtype());
        dataSource.close();

    }

}
