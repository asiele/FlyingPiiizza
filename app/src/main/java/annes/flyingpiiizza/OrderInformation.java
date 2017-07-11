package annes.flyingpiiizza;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class OrderInformation extends AppCompatActivity {

    private TextView sumPrice;
    private TextView nameOrder;
    private Button buttonBack;
    private DishDataSource dataSource;
    private List<Dish> allDishes;
    private ListView listOfOrders;
    private ListView proposals;
    private EditText dishNameSearch;
    private List<Dish> queryList;
    private int id;
    private Button buttonDelete;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sumPrice = (TextView) findViewById(R.id.priceOrderInfo);
        nameOrder = (TextView) findViewById(R.id.nameOrderInfo);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        listOfOrders = (ListView) findViewById(R.id.listOfOrders);
        proposals = (ListView) findViewById(R.id.proposals);
        dishNameSearch = (EditText) findViewById(R.id.dishNameSearch);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        dataSource = new DishDataSource(this);

        dataSource.open();

        id = -1;
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

        final Context context = this;
        listOfOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id_) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Möchten Sie das Gericht wirklich aus der Bestellung entfernen?");
                builder1.setCancelable(true);
                builder1.setNegativeButton(
                        "Nein",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id__) {
                                dialog.cancel();
                            }
                        });
                builder1.setPositiveButton(
                        "Ja",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id__) {
                                dataSource.open();
                                dataSource.deleteDishFromOrder(dataSource.getIdByDishName(allDishes.get(position).getName()), id);
                                dataSource.close();

                                for (int i = 0; i < allDishes.size(); i++) {
                                    if (allDishes.get(i).getName().equals(allDishes.get(position).getName())) {
                                        allDishes.remove(i);
                                        break;
                                    }
                                }

                                updateOrderListView();
                                sumPrice.setText(Integer.toString(dataSource.calculateOrderCost(id)));
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dataSource.open();
                dataSource.deleteOrderById(id);
                dataSource.close();
                finish();
            }
        });

        dishNameSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchDish();
            }
        });

        dishNameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchDish();
            }
        });


        proposals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id_) {
                allDishes.add(queryList.get(position));
                dishNameSearch.setText("", null);
                proposals.setVisibility(View.INVISIBLE);
                sumPrice.setText(Integer.toString(calculateCosts()), null);
                updateOrderListView();
                GrowingListViewUtils.adaptListViewSize(proposals);

                dataSource.open();
                boolean ok = dataSource.createOrderDishRelation((int) id,
                        dataSource.getIdByDishName(queryList.get(position).getName()));
                if(!ok) {
                    Toast.makeText(getApplicationContext(), "Etwas beim Einfügen der bestellten Gerichte ist falsch gelaufen!", Toast.LENGTH_LONG).show();
                }
                dataSource.close();
                GrowingListViewUtils.adaptListViewSize(listOfOrders);
            }
        });

        GrowingListViewUtils.adaptListViewSize(listOfOrders);
    }

    private void searchDish() {
        dataSource.open();

        String searchQuery = dishNameSearch.getText().toString();

        List<String> nameList = new ArrayList<String>();

        if (searchQuery.equals("")) {
        } else {
            queryList = dataSource.getAllDishesByQuery(searchQuery);
            for(int i = 0; i < queryList.size(); i++) {
                nameList.add(queryList.get(i).getName());
            }
        }

        final ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_ingredient, nameList);
        proposals.setAdapter(adapter);
        dataSource.close();
        proposals.setVisibility(View.VISIBLE);

        GrowingListViewUtils.adaptListViewSize(proposals);
    }

    private int calculateCosts() {
        int costs = 0;
        for(int i = 0; i < allDishes.size(); i++) {
            costs = costs + allDishes.get(i).getPrice();
        }
        return costs;
    }

    private void updateOrderListView() {
        String dishNames[] = new String[allDishes.size()];
        Integer dishPrices[] = new Integer[allDishes.size()];
        String dishTypes[] = new String[allDishes.size()];
        List<Bitmap> imgrid=new ArrayList<Bitmap>();
        for(int i = 0; i < allDishes.size(); i++) {
            dishNames[i] = allDishes.get(i).getName();
            dishPrices[i] = allDishes.get(i).getPrice();
            dishTypes[i] = allDishes.get(i).getDishtype();
        }
        CustomListAdapter adapter=new CustomListAdapter(this, dishNames, dishPrices, dishTypes,
                imgrid);
        listOfOrders.setAdapter(adapter);
        GrowingListViewUtils.adaptListViewSize(listOfOrders);
    }

}
