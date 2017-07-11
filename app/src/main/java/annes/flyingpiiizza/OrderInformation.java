package annes.flyingpiiizza;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class OrderInformation extends AppCompatActivity {

    private TextView sumPrice;
    private  TextView nameOrder;
    private Button buttonBack;
    private DishDataSource dataSource;
    private List<Dish> allDishes;
    private ListView listOfOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sumPrice = (TextView) findViewById(R.id.priceOrderInfo);
        nameOrder = (TextView) findViewById(R.id.nameOrderInfo);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        listOfOrders = (ListView) findViewById(R.id.listOfOrders);

        dataSource = new DishDataSource(this);

        dataSource.open();

        int id = -1;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {

        } else {
            id = (int) extras.get("IDOrderExtra");
        }

        sumPrice.setText(Integer.toString(dataSource.calculateOrderCost(id)));
        nameOrder.setText(dataSource.getNameOfOrderById(id));

        allDishes = dataSource.getAllDishesByOrderID(id);
        List<Bitmap> imgrid=new ArrayList<Bitmap>();
        String[] dishNames = new String[allDishes.size()];
        String[] dishTypes = new String[allDishes.size()];
        Integer[] dishPrice = new Integer[allDishes.size()];
        for(int i = 0; i < allDishes.size(); i++) {
            dishNames[i] = allDishes.get(i).getName();
            dishTypes[i] = allDishes.get(i).getDishtype();
            dishPrice[i] = allDishes.get(i).getPrice();
        }
        CustomListAdapter adapter=new CustomListAdapter(this, dishNames, dishPrice, dishTypes,
                imgrid);
        listOfOrders.setAdapter(adapter);
        dataSource.close();

        buttonBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
