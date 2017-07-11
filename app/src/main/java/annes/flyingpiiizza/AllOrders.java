package annes.flyingpiiizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class AllOrders extends AppCompatActivity {

    private Button back;
    private Button newOrder;
    private ListView allOrders;
    DishDataSource dataSource;
    String[] dishNames = {};
    int[] IDs = {};
    int[] prices = {};

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
    }

//    private void showAllDishes() {
//        dataSource = new DishDataSource(this);
//
//        dataSource.open();
//        IDs = //Johannas Methode
//        dishNames = //Johannas Methode Namen
//        price = //Johannas Methode Preis
//        CustomListAdapter adapter = new CustomListAdapter(this, dishNames, prices, null, null);
//        allOrders.setAdapter(adapter);
//
//        allOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Intent intent = new Intent(AllOrders.this, OrderInformation.class);
//                intent.putExtra("IDOrderExtra", IDs[position]);
//                startActivity(intent);
//            }
//        });
//
//        dataSource.close();
//    }

}
