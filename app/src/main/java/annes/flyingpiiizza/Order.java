package annes.flyingpiiizza;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anne on 09.04.2017.
 */

public class Order {

    private ArrayList<Dish> order;

    public Order() {
        order = new <Dish>ArrayList();
    }

    public void setOrder(ArrayList<Dish> order) {
        this.order = order;
    }

    public ArrayList<Dish> getOrder() {
        return order;
    }

    
    public int getPrice() {
        int price = 0;
        for(int i = 0; i < order.size(); i++) {
            price = price + order.get(i).getPrice();
        }
        return price;
    }
}
