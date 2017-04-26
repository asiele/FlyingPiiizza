package annes.flyingpiiizza;

import java.util.List;

/**
 * Created by Anne on 09.04.2017.
 */

public class Dish {

    private int price;
    private List<String> ingredients;
    private String name;
    private String description;

    public Dish() {

    }

    public Dish(int price, List<String> ingredients, String name, String description) {
        this.price = price;
        this.ingredients = ingredients;
        this.name = name;
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void addIngredient(String zutat) {
        ingredients.add(zutat);
    }

    public boolean removeIngredient(String ingredient) {
        if(ingredients.contains(ingredient)) {
            ingredients.remove(ingredients.indexOf(ingredient));
            return true;
        } else {
            return false;
        }
    }
}
