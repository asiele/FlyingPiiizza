package annes.flyingpiiizza;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import annes.flyingpiiizza.dishesdb.DishDbHelper;

public class CustomListAdapter extends ArrayAdapter<String> {

    private static final String LOG_TAG = DishDbHelper.class.getSimpleName();
    private final Activity context;
    private final String[] dishNames;
    private final Integer[] dishPrices;
    private final String[] dishTypes;
    private final List<Bitmap> images;

    public CustomListAdapter(Activity context, String[] dishNames, Integer[] dishPrices, String[] dishTypes, List<Bitmap> images) {
        super(context, R.layout.mylist, dishNames);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.dishNames = dishNames;
        this.images = images;
        this.dishPrices = dishPrices;
        this.dishTypes = dishTypes;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView priceView = (TextView) rowView.findViewById(R.id.price);
        TextView extratxt = (TextView) rowView.findViewById(R.id.dishTypeTextView);

        txtTitle.setText(dishNames[position]);
        if(position < images.size() && images.get(position) != null) {
            imageView.setImageBitmap(images.get(position));
        }
        priceView.setText("Preis: " + dishPrices[position]);
        if(position < dishTypes.length) {
            extratxt.setText("Gericht Typ: " + dishTypes[position]);
        }
        return rowView;

    }
}
