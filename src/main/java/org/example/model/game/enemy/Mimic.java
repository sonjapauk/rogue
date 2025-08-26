package org.example.model.game.enemy;

import org.example.model.game.Coordinate;
import org.example.model.game.item.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Mimic extends Enemy {
    private ItemType itemSkin;
    private boolean isVisible = false;
    private boolean isHit = false;

    public Mimic() {
        itemSkin = generateItemSkin();
    }

    public Mimic(Coordinate coordinate, int health, int strength, int agility, int hostility) {
        super(coordinate, health, strength, agility, hostility, strength * agility);
        itemSkin = generateItemSkin();
    }

    public Mimic(Mimic mimic) {
        super(mimic);
        isVisible = mimic.isVisible;
        itemSkin = mimic.itemSkin;
        isHit = mimic.isHit;
    }

    @Override
    public void move() {
    }

    @Override
    public Enemy clone() {
        return new Mimic(this);
    }

    @Override
    public void stun() {

    }

    @Override
    public boolean hit(Actor target) {
        isVisible = true;
        setHostility(2);
        return super.hit(target);
    }

    public ItemType getItemSkin() {
        return itemSkin;
    }

    public void setItemSkin(ItemType itemSkin) {
        this.itemSkin = itemSkin;
    }

    public ItemType generateItemSkin() {
        List<ItemType> types = new ArrayList<>();

        types.addAll(Arrays.asList(FoodType.values()));
        types.addAll(Arrays.asList(WeaponsType.values()));
        types.addAll(Arrays.asList(ScrollsType.values()));
        types.addAll(Arrays.asList(TreasureType.values()));
        types.addAll(Arrays.asList(ElixirType.values()));

        Random random = new Random();

        return types.get(random.nextInt(types.size()));
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public void chaseSmart() {
        super.chaseSmart();

        if (foundTarget && !isHit) {
            foundTarget = false;
            isHit = true;
        }
    }

    public void setSeeHero(boolean seeHero) {
        if (!seeHero) {
            isVisible = false;
            setHostility(1);
            isHit = false;
        }

        super.setSeeHero(seeHero);
    }


    public boolean getIsHit() {
        return isHit;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
    }
}
