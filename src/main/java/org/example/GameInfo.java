package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.model.game.*;
import org.example.model.game.AllAboutHero.Hero;
import org.example.model.game.AllAboutHero.InventorySelector;
import org.example.model.game.enemy.Enemy;
import org.example.model.game.level.LevelMap;
import org.example.model.menu.MenuState;
import org.example.model.game.item.Item;
import org.example.rating.Rating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameInfo {
    private int currentLevel = 1;

    private LevelMap levelMap;

    private MenuState currentMenuState = MenuState.NEW_GAME;
    private GameState currentGameState = GameState.GAME;

    private boolean hasSave = false;
    private GlobalState globalState = GlobalState.MENU;

    private Hero hero;

    private Exit exit;

    private Map<Coordinate, Item> itemsOnMap = new HashMap<>();

    private Map<Coordinate, Enemy> enemiesOnMap = new HashMap<>();

    private int[][] map;

    private InventorySelector inventorySelector;

    private FinalMapWithFog finalMap;

    private EventHandler eventHandler;

    private List<GameEvent> currentEventsSound = new ArrayList<>();

    private Rating rating = new Rating();

    private DifficultyController difficultyController;

    public DifficultyController getDifficultyController() {
        return difficultyController;
    }

    public void setDifficultyController(DifficultyController difficultyController) {
        this.difficultyController = difficultyController;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    @JsonIgnore
    public Map<Coordinate, Item> getItemsOnMap() {
        return itemsOnMap;
    }

    @JsonIgnore
    public void setItemsOnMap(Map<Coordinate, Item> itemsOnMap) {
        this.itemsOnMap = itemsOnMap;
    }

    @JsonProperty("itemsOnMap")
    public Map<String, Item> getItemsOnMapAsStringKeys() {
        Map<String, Item> converted = new HashMap<>();

        for (Map.Entry<Coordinate, Item> entry : itemsOnMap.entrySet()) {
            converted.put(CoordinateHelper.toString(entry.getKey()), entry.getValue());
        }

        return converted;
    }

    @JsonProperty("itemsOnMap")
    public void setItemsOnMapFromStringKeys(Map<String, Item> mapFromJson) {
        itemsOnMap = new HashMap<>();

        for (Map.Entry<String, Item> entry : mapFromJson.entrySet()) {
            itemsOnMap.put(CoordinateHelper.fromString(entry.getKey()), entry.getValue());
        }
    }

    public LevelMap getLevelMap() {
        return levelMap;
    }

    public void setLevelMap(LevelMap levelMap) {
        this.levelMap = levelMap;
    }

    public List<GameEvent> getCurrentEventsSound() {
        return currentEventsSound;
    }

    public void setCurrentEventsSound(List<GameEvent> currentEventsSound) {
        this.currentEventsSound = currentEventsSound;
    }

    public void setFinalMap(FinalMapWithFog finalMap) {
        this.finalMap = finalMap;
    }

    public FinalMapWithFog getFinalMap() {
        return finalMap;
    }

    public void setHasSave(boolean hasSave) {
        this.hasSave = hasSave;
    }

    public boolean getHasSave() {
        return hasSave;
    }

    public MenuState getCurrentMenuState() {
        return currentMenuState;
    }

    public void setCurrentMenuState(MenuState currentMenuState) {
        this.currentMenuState = currentMenuState;
    }

    public GlobalState getGlobalState() {
        return globalState;
    }

    public void setGlobalState(GlobalState globalState) {
        this.globalState = globalState;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Exit getExit() {
        return exit;
    }

    public void setExit(Exit exit) {
        this.exit = exit;
    }

    @JsonIgnore
    public Map<Coordinate, Enemy> getEnemiesOnMap() {
        return enemiesOnMap;
    }

    @JsonIgnore
    public void setEnemiesOnMap(Map<Coordinate, Enemy> enemiesOnMap) {
        this.enemiesOnMap = enemiesOnMap;
    }

    @JsonProperty("enemiesOnMap")
    public Map<String, Enemy> getEnemiesOnMapAsStringKeys() {
        Map<String, Enemy> converted = new HashMap<>();

        for (Map.Entry<Coordinate, Enemy> entry : enemiesOnMap.entrySet()) {
            converted.put(CoordinateHelper.toString(entry.getKey()), entry.getValue());
        }

        return converted;
    }

    @JsonProperty("enemiesOnMap")
    public void setEnemiesOnMapFromStringKeys(Map<String, Enemy> mapFromJson) {
        enemiesOnMap = new HashMap<>();

        for (Map.Entry<String, Enemy> entry : mapFromJson.entrySet()) {
            enemiesOnMap.put(CoordinateHelper.fromString(entry.getKey()), entry.getValue());
        }
    }

    public InventorySelector getInventorySelector() {
        return inventorySelector;
    }

    public void setInventorySelector(InventorySelector inventorySelector) {
        this.inventorySelector = inventorySelector;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
}
