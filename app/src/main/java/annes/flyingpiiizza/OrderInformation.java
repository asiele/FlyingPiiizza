package annes.flyingpiiizza;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class OrderInformation extends AppCompatActivity {

    private TextView sumPrice;
    private TextView nameOrder;
    private Button buttonBack;
    private DishDataSource dataSource;
    private ListView proposals;
    private EditText dishNameSearch;
    private List<Dish> queryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sumPrice = (TextView) findViewById(R.id.priceOrderInfo);
        nameOrder = (TextView) findViewById(R.id.nameOrderInfo);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        proposals = (ListView) findViewById(R.id.proposals);
        dishNameSearch = (EditText) findViewById(R.id.dishNameSearch);

        dataSource = new DishDataSource(this);

        dataSource.open();

        int id = -1;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {

        } else {
            id = (int) extras.get("IDOrderExtra");
        }

        sumPrice.setText(Integer.toString(dataSource.calculateOrderCost(id)));
        nameOrder.setText("Blabla");

        dataSource.close();

        buttonBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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

}
