package annes.flyingpiiizza;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GrowingListViewUtils {

    static public void adaptListViewSize(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    static public void setImageViewSize(ImageView imageView) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = 250;
        imageView.setLayoutParams(params);
        imageView.requestLayout();
    }

    static public void setImageViewSizeMax(ImageView imageView, int width, int height) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = height;
        params.width = width;
        imageView.setLayoutParams(params);
        imageView.requestLayout();
    }
}
