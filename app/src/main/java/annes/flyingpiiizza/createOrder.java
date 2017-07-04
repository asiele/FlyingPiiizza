package annes.flyingpiiizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class CreateOrder extends AppCompatActivity {

    private Button back;
    private ListView proposals;
    private ListView orderList;
    private Button createOrder;
    private EditText orderName;
    private EditText dishNameSearch;
    private ArrayList<Dish> orderedDishes;
    private List<Dish> queryList;
    private TextView priceOrderInfo;
    private Button resetDishes;

    private DishDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back = (Button) findViewById(R.id.buttonBack);
        proposals = (ListView) findViewById(R.id.proposals);
        createOrder = (Button) findViewById(R.id.createOrder);
        orderName = (EditText) findViewById(R.id.orderName);
        dishNameSearch = (EditText) findViewById(R.id.dishNameSearch);
        priceOrderInfo = (TextView) findViewById(R.id.priceOrderInfo);
        orderList = (ListView) findViewById(R.id.listOfDishes);
        resetDishes = (Button) findViewById(R.id.resetDishes);

        dataSource = new DishDataSource(this);
        orderedDishes = new ArrayList<Dish>();

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        orderName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                proposals.setVisibility(View.INVISIBLE);
            }
        });

        dishNameSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dataSource.open();

                queryList = dataSource.getAllDishesByQuery(dishNameSearch.getText().toString());
                List<String> nameList = new ArrayList<String>();
                for(int i = 0; i < queryList.size(); i++) {
                    nameList.add(queryList.get(i).getName());
                }
                final ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_ingredient, nameList);
                proposals.setAdapter(adapter);
                dataSource.close();
                proposals.setVisibility(View.VISIBLE);
            }
        });

        proposals.setOnTouchListener(new ListView.OnTouchListener() {
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

        orderList.setOnTouchListener(new ListView.OnTouchListener() {
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

        proposals.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                orderedDishes.add(queryList.get(position));
                dishNameSearch.setText("", null);
                proposals.setVisibility(View.INVISIBLE);
                priceOrderInfo.setText(Integer.toString(calculateCosts()), null);
                updateOrderListView();
            }
        });

        resetDishes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dishNameSearch.setText("", null);
                proposals.setVisibility(View.INVISIBLE);
                orderedDishes = new ArrayList<Dish>();
                updateOrderListView();
                priceOrderInfo.setText("0", null);
            }
        });

        proposals.setVisibility(View.INVISIBLE);
    }

    private int calculateCosts() {
        int costs = 0;
        for(int i = 0; i < orderedDishes.size(); i++) {
            costs = costs + orderedDishes.get(i).getPrice();
        }
        return costs;
    }

    private void updateOrderListView() {
        String dishNames[] = new String[orderedDishes.size()];
        Integer dishPrices[] = new Integer[orderedDishes.size()];
        String dishTypes[] = new String[orderedDishes.size()];
        Integer[] imgrid={};
        for(int i = 0; i < orderedDishes.size(); i++) {
            dishNames[i] = orderedDishes.get(i).getName();
            dishPrices[i] = orderedDishes.get(i).getPrice();
            dishTypes[i] = orderedDishes.get(i).getDishtype();
        }
        CustomListAdapter adapter=new CustomListAdapter(this, dishNames, dishPrices, dishTypes,
                imgrid);
        orderList.setAdapter(adapter);
    }

}
