package annes.flyingpiiizza;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDataSource;


public class AllDishesActivity extends AppCompatActivity {

    DishDataSource dataSource;
    ListView list;
    String[] dishNames = {};
    String[] dishDescriptions = {};
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
    }

    private void showAllDishes() {
        dataSource = new DishDataSource(this);

        dataSource.open();
        dishNames = dataSource.getAllDishesNamesAsStringArray();
        dishPrices = dataSource.getAllDishesPricesAsStringArray();
        dishDescriptions = dataSource.getAllDishesDescriptionAsStringArray();
        CustomListAdapter adapter=new CustomListAdapter(this, dishNames, dishPrices, dishDescriptions, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= dishNames[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

        dataSource.close();
    }


}
