package annes.flyingpiiizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class DishActivity extends AppCompatActivity {

    Button backButton;
    Button impressumButton;
    Button newDishButton;
    Button allDishesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        backButton = (Button) findViewById(R.id.buttonBack);
        impressumButton = (Button) findViewById(R.id.buttonImpressum);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        newDishButton = (Button) findViewById(R.id.buttonNewDish);
        allDishesButton = (Button) findViewById(R.id.buttonAllDishes);

        impressumButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishActivity.this, ImpressumActivity.class);
                startActivity(intent);
            }
        });

        newDishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishActivity.this, CreateDishActivity.class);
                startActivity(intent);
            }
        });

        allDishesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishActivity.this, AllDishesActivity.class);
                startActivity(intent);
            }

        });
    }

}
