package org.example.model.game.AllAboutHero;

import org.example.model.game.Coordinate;
import org.example.model.game.item.*;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    static final int MAX_ITEM_COUNT = 9;
    private List<Elixir> elixirs;
    private List<Scroll> scrolls;
    private List<Food> foods;
    private List<Weapon> weapons;
    private Treasure treasures;

    public Inventory(List<Elixir> elixirs, List<Scroll> scrolls, List<Food> foods, List<Weapon> weapons, Treasure treasures) {
        this.elixirs = elixirs;
        this.scrolls = scrolls;
        this.foods = foods;
        this.weapons = weapons;
        this.treasures = treasures;
    }

    public Inventory() {
        this.elixirs = new ArrayList<>();
        this.scrolls = new ArrayList<>();
        this.foods = new ArrayList<>();
        this.weapons = new ArrayList<>(List.of(new Weapon(new Coordinate(0, 0), 0, WeaponsType.HANDS)));
        this.treasures = new Treasure(new Coordinate(0, 0), 0);
        ;
    }

    public List<Elixir> getElixirs() {
        return elixirs;
    }

    public void setElixirs(List<Elixir> elixirs) {
        this.elixirs = elixirs;
    }

    public List<Scroll> getScrolls() {
        return scrolls;
    }

    public void setScrolls(List<Scroll> scrolls) {
        this.scrolls = scrolls;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    public Treasure getTreasures() {
        return treasures;
    }

    public void setTreasures(Treasure treasures) {
        this.treasures = treasures;
    }

    public boolean addItem(Item item) {
        if (item instanceof Elixir) {
            return addElixir((Elixir) item);
        }

        if (item instanceof Scroll) {
            return addScroll((Scroll) item);
        }

        if (item instanceof Food) {
            return addFood((Food) item);
        }

        if (item instanceof Weapon) {
            return addWeapon((Weapon) item);
        }

        if (item instanceof Treasure) {
            addTreasure((Treasure) item);
            return true;
        }

        return false;
    }

    public boolean addElixir(Elixir elixir) {
        if (elixirs.size() < MAX_ITEM_COUNT) {
            elixirs.add(elixir);

            return true;
        }

        return false;
    }

    public boolean addScroll(Scroll scroll) {
        if (scrolls.size() < MAX_ITEM_COUNT) {
            scrolls.add(scroll);

            return true;
        }

        return false;
    }

    public boolean addFood(Food food) {
        if (foods.size() < MAX_ITEM_COUNT) {
            foods.add(food);

            return true;
        }

        return false;
    }

    public boolean addWeapon(Weapon weapon) {
        if (weapons.size() < MAX_ITEM_COUNT + 1) {
            weapons.add(weapon);

            return true;
        }

        return false;
    }

    public void addTreasure(Treasure treasure) {
        treasures.addValue(treasure.getValue());
    }

    public Elixir removeElixir(int index) {
        if (!elixirs.isEmpty()) {
            return elixirs.remove(index);
        }

        return null;
    }

    public Scroll removeScroll(int index) {
        if (!scrolls.isEmpty()) {
            return scrolls.remove(index);
        }

        return null;
    }

    public Food removeFood(int index) {
        if (!foods.isEmpty()) {
            return foods.remove(index);
        }

        return null;
    }
}
