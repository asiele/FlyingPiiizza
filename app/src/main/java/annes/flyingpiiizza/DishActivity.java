package annes.flyingpiiizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class DishActivity extends AppCompatActivity {

    Button zurück;
    Button impressum;
    Button newDish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        zurück = (Button) findViewById(R.id.buttonBack);
        impressum = (Button) findViewById(R.id.buttonImpressum);

        zurück.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        newDish = (Button) findViewById(R.id.buttonNewDish);

        impressum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishActivity.this, ImpressumActivity.class);
                startActivity(intent);
            }
        });

        newDish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishActivity.this, CreateDishActivity.class);
                startActivity(intent);
            }
        });
    }

}
