package annes.flyingpiiizza;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class AllOrders extends AppCompatActivity {

    private Button back;
    private Button newOrder;
    private ListView allOrders;
    DishDataSource dataSource;
    String[] dishNames = {};
    Integer[] prices = {};
    Integer[] IDs = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back = (Button) findViewById(R.id.backButton);
        allOrders = (ListView) findViewById(R.id.listOfAllOrders);
        newOrder = (Button) findViewById(R.id.newOrderButton);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        newOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllOrders.this, CreateOrder.class);
                startActivity(intent);
            }
        });
        showAllOrders();
    }

    private void showAllOrders() {
        dataSource = new DishDataSource(this);

        dataSource.open();
        dishNames = dataSource.getAllOrderNames();
        prices =  dataSource.getAllOrderTotalCost();
        IDs = dataSource.getAllOrderIds();
        String[] ArrayString = {};
        List<Bitmap> ArrayInteger = new ArrayList<Bitmap>();
        CustomListAdapter adapter = new CustomListAdapter(this, dishNames, prices, ArrayString, ArrayInteger);
        allOrders.setAdapter(adapter);

        allOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(AllOrders.this, OrderInformation.class);
                intent.putExtra("IDOrderExtra", IDs[position]);
                startActivity(intent);
            }
        });

        dataSource.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showAllOrders(); // Refresh the order list every time the view is shown in case the list changed
    }

}
