package annes.flyingpiiizza;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CreateOrder extends AppCompatActivity {

    private Button back;
    private ListView proposals;
    private Button createOrder;
    private EditText orderName;
    private EditText dishNameSearch;

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

        proposals.setVisibility(View.INVISIBLE);
    }

}
