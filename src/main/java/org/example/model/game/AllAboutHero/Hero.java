package org.example.model.game.AllAboutHero;

import org.example.model.Direction;
import org.example.model.game.enemy.Actor;
import org.example.model.game.item.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Hero extends Actor {
    List<Elixir> tempElixirsEffects = new ArrayList<>();
    private Inventory inventory = new Inventory();

    private int currentWeaponIndex = 0;

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void move(Direction direction) {
        switch (direction) {
            case UP -> coordinate.setX(coordinate.getX() - 1);
            case DOWN -> coordinate.setX(coordinate.getX() + 1);
            case LEFT -> coordinate.setY(coordinate.getY() - 1);
            case RIGHT -> coordinate.setY(coordinate.getY() + 1);
        }
    }

    public void setCurrentWeaponIndex(int currentWeaponIndex) {
        this.currentWeaponIndex = currentWeaponIndex;
    }

    public int getCurrentWeaponIndex() {
        return currentWeaponIndex;
    }

    public void useScroll(Scroll scroll) {
        if (scroll != null) {
            switch (scroll.getItemType()) {
                case ScrollsType.AGILITY_SCROLL -> agility += scroll.getValue();
                case ScrollsType.STRENGTH_SCROLL -> strength += scroll.getValue();
                case ScrollsType.MAXIMUM_HEALTH_SCROLL -> {
                    maxHealth += scroll.getValue();
                    currentHealth += scroll.getValue();
                }

                default -> throw new IllegalStateException("Unexpected value: " + scroll.getItemType());
            }
        }
    }

    public void useFood(Food food) {
        if (food != null) {
            if (currentHealth + food.getValue() > maxHealth) {
                currentHealth = maxHealth;
            } else {
                currentHealth += food.getValue();
            }
        }
    }

    public void useElixir(Elixir elixir) {
        if (elixir != null) {
            addEffects(elixir);

            switch (elixir.getItemType()) {
                case ElixirType.AGILITY_ELIXIR -> agility += elixir.getValue();
                case ElixirType.STRENGTH_ELIXIR -> strength += elixir.getValue();
                case ElixirType.MAX_HEALTH_ELIXIR -> {
                    maxHealth += elixir.getValue();
                    currentHealth += elixir.getValue();
                }

                default -> throw new IllegalStateException("Unexpected value: " + elixir.getItemType());
            }
        }
    }

    public List<Elixir> getCurrentElixirsEffects() {
        return tempElixirsEffects;
    }

    public void setCurrentElixirsEffects(List<Elixir> tempEffects) {
        this.tempElixirsEffects = tempEffects;
    }

    public void updateEffectsDuration() {
        Iterator<Elixir> iterator = tempElixirsEffects.iterator();

        while (iterator.hasNext()) {
            Elixir e = iterator.next();

            if (e.getDuration() == 0) {
                removeEffects(e);
                iterator.remove();
            } else {
                e.setDuration(e.getDuration() - 1);
            }
        }
    }


    void addEffects(Elixir elixir) {
        tempElixirsEffects.add(elixir);
    }

    void removeEffects(Elixir elixir) {
        switch (elixir.getItemType()) {
            case ElixirType.AGILITY_ELIXIR -> agility -= elixir.getValue();
            case ElixirType.STRENGTH_ELIXIR -> strength -= elixir.getValue();
            case ElixirType.MAX_HEALTH_ELIXIR -> {
                if (currentHealth > 0 && currentHealth - elixir.getValue() <= 0) {
                    currentHealth = 1;
                } else {
                    currentHealth -= elixir.getValue();
                }

                maxHealth -= elixir.getValue();
            }

            default -> throw new IllegalStateException("Unexpected value: " + elixir.getItemType());
        }
    }

    @Override
    public void stun() {
        isStunned = false;
    }
}
