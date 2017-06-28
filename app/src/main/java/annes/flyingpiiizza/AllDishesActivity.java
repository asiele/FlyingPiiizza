package annes.flyingpiiizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Button;

import annes.flyingpiiizza.dishesdb.DishDataSource;


public class AllDishesActivity extends AppCompatActivity {

    private Button back;
    private Button impressum;

    DishDataSource dataSource;
    ListView list;
    String[] dishNames = {};
    String[] dishTypes = {};
    Integer[] dishPrices = {};
    Integer[] imgid={
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_dishes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showAllDishes();

        back = (Button) findViewById(R.id.backButton);
        impressum = (Button) findViewById(R.id.impressumButton);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        impressum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllDishesActivity.this, ImpressumActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showAllDishes(); // Refresh the dish list every time the view is shown in case the list changed
    }

    private void showAllDishes() {
        dataSource = new DishDataSource(this);

        dataSource.open();
        dishNames = dataSource.getAllDishesNamesAsStringArray();
        dishPrices = dataSource.getAllDishesPricesAsIntegerArray();
        dishTypes = dataSource.getAllDishesTypesAsStringArray();
        CustomListAdapter adapter=new CustomListAdapter(this, dishNames, dishPrices, dishTypes, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(AllDishesActivity.this, DishInformationActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        dataSource.close();
    }


}
