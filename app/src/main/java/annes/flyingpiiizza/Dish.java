package annes.flyingpiiizza;

import java.util.List;

/**
 * Created by Anne on 09.04.2017.
 */

public class Dish {

    private int price;
    private List<String> ingredients;
    private String name;
    private String dishtype;
    private String vegetarian;

    public Dish() {

    }

    public Dish(String name, String dishtype, int price, String vegetarian, List<String> ingredients) {
        this.price = price;
        this.ingredients = ingredients;
        this.name = name;
        this.dishtype = dishtype;
        this.vegetarian = vegetarian;
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

    public void setDishtype(String dishtype) {
        this.dishtype = dishtype;
    }

    public void setVegetarian(String vegetarian) {
        this.vegetarian = vegetarian;
    }

    public String getVegetarian() {
        return vegetarian;
    }

    public void setIngredients(List<String> ingredients)  { this.ingredients = ingredients; }

    public String getDishtype() {
        return dishtype;
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

    public String toString() {
        return "\nName: " + name + "Gericht Typ: " + dishtype + "\nPreis: " + price;
    }
}
