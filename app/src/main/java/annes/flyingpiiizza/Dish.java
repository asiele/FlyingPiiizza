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

    public Dish(String name, String description, int price, List<String> ingredients) {
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

    public void setIngredients(List<String> ingredients)  { this.ingredients = ingredients; }

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

    public String toString() {
        return "\nName: " + name + "Beschreibung: " + description + "\nPrice: " + price;
    }
}
