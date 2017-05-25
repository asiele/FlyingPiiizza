package annes.flyingpiiizza;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] dishNames;
    private final Integer[] dishPrices;
    private final String[] dishTypes;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, String[] dishNames, Integer[] dishPrices, String[] dishTypes, Integer[] imgid) {
        super(context, R.layout.mylist, dishNames);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.dishNames = dishNames;
        this.imgid=imgid;
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
        if(position < imgid.length) {
            imageView.setImageResource(imgid[position]);
        }
        priceView.setText("Preis: " + dishPrices[position]);
        if(position < dishTypes.length) {
            extratxt.setText("Gericht Typ: " + dishTypes[position]);
        }
        return rowView;

    }
}
