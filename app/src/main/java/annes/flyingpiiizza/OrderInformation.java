package annes.flyingpiiizza;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import annes.flyingpiiizza.dishesdb.DishDataSource;

public class OrderInformation extends AppCompatActivity {

    private TextView sumPrice;
    private DishDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sumPrice = (TextView) findViewById(R.id.priceOrderInfo);
        dataSource = new DishDataSource(this);

        dataSource.open();

        int id = -1;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {

        } else {
            id = (int) extras.get("IDOrderExtra");
        }

        sumPrice.setText(dataSource.calculateOrderCost(id));

        dataSource.close();
    }

}
