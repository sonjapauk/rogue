package org.example.model.game;

import org.example.*;
import org.example.model.Direction;
import org.example.model.UserAction;
import org.example.model.game.AllAboutHero.Hero;
import org.example.model.game.AllAboutHero.InventorySelector;
import org.example.model.game.AllAboutHero.ItemSelectorType;
import org.example.model.game.enemy.*;

import java.util.*;

import org.example.model.game.item.*;
import org.example.model.game.level.EnemyRandomizer;
import org.example.model.game.level.ItemsRandomizer;
import org.example.model.game.level.Level;
import org.example.model.game.level.Room;
import org.example.rating.Statistics;

public class Game {

    DifficultyController difficultyController;

    InventorySelector inventorySelector;
    GameState currentState = GameState.START;
    Hero hero;
    Level map;
    Exit exit;
    FinalMapWithFog finalMap = new FinalMapWithFog();
    EventHandler eventHandler = new EventHandler();

    List<GameEvent> currentEventsForSound = new ArrayList<>();

    Statistics statistics = new Statistics();

    public Game(GameInfo gameInfo) {
    }

    public void initGame(GameInfo currentGameInfo) {
        currentGameInfo.setHasSave(true);

        exit = new Exit();
        hero = new Hero();

        inventorySelector = new InventorySelector();

        finalMap = new FinalMapWithFog();

        eventHandler = new EventHandler();
        statistics = new Statistics();

        difficultyController = new DifficultyController();
        difficultyController.startDifficultyControl(hero.getCurrentHealth());

        generateNextLevel(1);
    }

    public void loadGame(GameInfo currentGameInfo) {
        statistics = currentGameInfo.getRating().getCurrentStats();

        exit = currentGameInfo.getExit();
        hero = currentGameInfo.getHero();

        inventorySelector = currentGameInfo.getInventorySelector();

        currentState = currentGameInfo.getCurrentGameState();
        eventHandler = currentGameInfo.getEventHandler();

        difficultyController = currentGameInfo.getDifficultyController();

        map = new Level();
        map.setLevel(currentGameInfo.getCurrentLevel());
        map.setMap(currentGameInfo.getMap());
        map.setCurrentMap(currentGameInfo.getLevelMap());
        map.setItemsOnLevel(currentGameInfo.getItemsOnMap());
        map.setEnemiesOnLevel(currentGameInfo.getEnemiesOnMap());
        finalMap = currentGameInfo.getFinalMap();
    }

    public void generateNextLevel(int nextLevel) {
        difficultyController.calculateDifficultyIndex(hero.getCurrentHealth());

        map = new Level(nextLevel);
        hero.setCoordinate(map.getHeroCoordinateOnMap());
        exit.setCoordinate(map.getExitCoordinateOnMap());

        setAllItems(map.getLevel(), difficultyController.getDifficultyIndex());
        setAllEnemies(map.getLevel(), difficultyController.getDifficultyIndex());

        finalMap.prepareFinalMap(map, hero.getCoordinate(), exit.getCoordinate());

        difficultyController.startDifficultyControl(hero.getCurrentHealth());
    }

    public void setAllItems(int currentLevel, int difficultyIndex) {
        List<Item> itemsForMapping = ItemsRandomizer.getItemsListForMapping(currentLevel, difficultyIndex);
        Map<Coordinate, Item> itemsResult = new HashMap<>();

        for (Item item : itemsForMapping) {
            item.setCoordinate(map.getItemCoordinate());
            itemsResult.put(item.getCoordinate(), item);
        }

        map.setItemsOnLevel(itemsResult);
    }

    public void setAllEnemies(int currentLevel, int difficultyIndex) {
        List<Enemy> enemiesForMapping = EnemyRandomizer.getEnemyListForMapping(currentLevel, difficultyIndex);
        Map<Coordinate, Enemy> enemyResult = new HashMap<>();

        for (Enemy enemy : enemiesForMapping) {
            enemy.setCoordinate(map.getEnemiesCoordinate());
            if (enemy instanceof Ghost) {
                int roomNumber = map.getCurrentMap().getRoomNumber(enemy.getCoordinate());
                Room hostRoom = map.getCurrentMap().getRooms().get(roomNumber);
                ((Ghost) enemy).setArea(new Coordinate(hostRoom.getX(), hostRoom.getY()), hostRoom.getLength(), hostRoom.getWidth());
            }

            enemyResult.put(new Coordinate(enemy.getCoordinate().getX(), enemy.getCoordinate().getY()), enemy);
        }

        map.setEnemiesOnLevel(enemyResult);
    }

    public void updateGameInfo(GameInfo currentGameInfo) {
        currentGameInfo.setMap(map.getMap());
        currentGameInfo.setHero(hero);
        currentGameInfo.setItemsOnMap(map.getItemsOnLevel());
        currentGameInfo.setInventorySelector(inventorySelector);
        currentGameInfo.setCurrentGameState(currentState);
        currentGameInfo.setEnemiesOnMap(map.getEnemiesOnLevel());
        currentGameInfo.setExit(exit);
        currentGameInfo.setCurrentLevel(map.getLevel());
        currentGameInfo.setFinalMap(finalMap);
        currentGameInfo.setCurrentEventsSound(currentEventsForSound);
        currentGameInfo.setLevelMap(map.getCurrentMap());
        currentGameInfo.setEventHandler(eventHandler);
        currentGameInfo.setDifficultyController(difficultyController);

        currentGameInfo.getRating().setCurrentStats(statistics);
    }

    public Coordinate getEnemyCoordinate(UserAction action) {
        return switch (action) {
            case UP -> new Coordinate(hero.getCoordinate().getX() - 1, hero.getCoordinate().getY());
            case DOWN -> new Coordinate(hero.getCoordinate().getX() + 1, hero.getCoordinate().getY());
            case LEFT -> new Coordinate(hero.getCoordinate().getX(), hero.getCoordinate().getY() - 1);
            case RIGHT -> new Coordinate(hero.getCoordinate().getX(), hero.getCoordinate().getY() + 1);
            default -> null;
        };
    }

    public void heroStep(UserAction action) {
        hero.updateEffectsDuration();

        if (hero.isStunned()) {
            hero.stun();
            eventHandler.addEvent(new GameEvent(EventType.HERO_IS_STUNNED, hero.getStrength()));
            return;
        }

        moveHero(action);

        if (hero.foundTarget()) {
            Enemy enemy = map.getEnemiesOnLevel().get(getEnemyCoordinate(action));

            if (hero.hit(enemy)) {
                statistics.hitsCountIncrement();
                eventHandler.addEvent(new GameEvent(EventType.HERO_HIT_SUCCESS, hero.getStrength()));
                currentEventsForSound.add(new GameEvent(EventType.HERO_HIT_SUCCESS, enemy.getStrength()));
            } else {
                eventHandler.addEvent(new GameEvent(EventType.HERO_HIT_FAIL, 0));
                currentEventsForSound.add(new GameEvent(EventType.HERO_HIT_FAIL, enemy.getStrength()));
            }

            if (enemy.getCurrentHealth() <= 0) {
                statistics.defeatEnemiesCountIncrement();
                map.killEnemy(enemy);
                eventHandler.addEvent(new GameEvent(EventType.KILL_ENEMY, enemy.getValue()));
                currentEventsForSound.add(new GameEvent(EventType.KILL_ENEMY, enemy.getValue()));
                hero.getInventory().getTreasures().addValue(enemy.getValue());
            }
        }
    }

    public void enemyStep(Enemy enemy) {
        if (enemy.isStunned()) {
            enemy.stun();
            eventHandler.addEvent(new GameEvent(EventType.ENEMY_IS_STUNNED, enemy.getStrength()));
            return;
        }

        Enemy tmpEnemy = enemy.clone();

        Coordinate tmpCoordinate = new Coordinate(enemy.getCoordinate().getX(), enemy.getCoordinate().getY());
        moveEnemy(tmpEnemy);

        if (tmpEnemy.foundTarget()) {
            if (tmpEnemy.hit(hero)) {
                statistics.enemiesHitsCountIncrement();
                eventHandler.addEvent(new GameEvent(EventType.ENEMY_HIT_SUCCESS, enemy.getStrength()));
                currentEventsForSound.add(new GameEvent(EventType.ENEMY_HIT_SUCCESS, enemy.getStrength()));
            } else {
                eventHandler.addEvent(new GameEvent(EventType.ENEMY_HIT_FAIL, 0));
                currentEventsForSound.add(new GameEvent(EventType.ENEMY_HIT_FAIL, 0));
            }
        }

        if (map.getEnemiesOnLevel().containsKey(tmpEnemy.getCoordinate())) {
            tmpEnemy.setCoordinate(tmpCoordinate);
        }

        map.getEnemiesOnLevel().remove(enemy.getCoordinate());
        map.getEnemiesOnLevel().putIfAbsent(tmpEnemy.getCoordinate(), tmpEnemy);
    }

    public void statisticUpdate() {
        statistics.setTreasureCount(hero.getInventory().getTreasures().getValue());
        statistics.setLevel(map.getLevel());
    }

    public void updateState(UserAction action, GameInfo currentGameInfo) {
        currentEventsForSound.clear();

        if (action == UserAction.EXIT && (currentState == GameState.GAME || currentState == GameState.WIN || currentState == GameState.GAME_OVER)) {
            currentGameInfo.setGlobalState(GlobalState.MENU);
            return;
        }

        if (currentState == GameState.WIN || currentState == GameState.GAME_OVER) {
            if(action == UserAction.ACTION) {
                currentGameInfo.getRating().addStatics();
                currentGameInfo.setGlobalState(GlobalState.MENU);
                currentGameInfo.setHasSave(false);
            } else {
                return;
            }
        }

        eventHandler.removeEvent(action);

        if (currentState == GameState.START) {
            initGame(currentGameInfo);
            currentState = GameState.GAME;
        }

        if (isInventoryState(action)) {
            currentState = GameState.INVENTORY;
        }

        if (currentState == GameState.GAME) {
            if(action != UserAction.SKIP && action != UserAction.SKIP_ALL && action != UserAction.ACTION) {

                heroStep(action);

                PriorityQueue<Enemy> enemies = new PriorityQueue<>(Comparator.comparingInt(Enemy::getPathSize));
                enemies.addAll(map.getEnemiesOnLevel().values());

                while (!enemies.isEmpty()) {
                    enemyStep(enemies.poll());

                    if (hero.getCurrentHealth() <= 0) {
                        currentState = GameState.GAME_OVER;
                    }
                }
            }

            gameStateProcess(action);
        } else if (currentState == GameState.INVENTORY) {
            if (action == UserAction.EXIT) {
                currentState = GameState.GAME;
            } else {
                updateInventory(action);
            }
        }

        finalMap.updateFinalMap(map, hero.getCoordinate());
        finalMap.updateVisible(map, hero.getCoordinate());

        statisticUpdate();
        updateGameInfo(currentGameInfo);
    }

    void gameStateProcess(UserAction action) {
        if (isExit(action)) {
            int nextLevel = map.getLevel() + 1;

            if (nextLevel > 21) {
                currentState = GameState.WIN;
            } else {
                generateNextLevel(nextLevel);
            }
        }
    }

    void updateInventory(UserAction action) {
        if (action == UserAction.ACTION) {
            switch (inventorySelector.getCurrentType()) {
                case ELIXIR -> {
                    hero.useElixir(hero.getInventory().removeElixir(inventorySelector.getCurrentIndex()));
                    statistics.elixirCountIncrement();

                    if (inventorySelector.getCurrentIndex() == inventorySelector.getMaxIndex() - 1) {
                        inventorySelector.setCurrentIndex(inventorySelector.getCurrentIndex() - 1);
                    }

                    inventorySelector.setMaxIndex(inventorySelector.getMaxIndex() - 1);
                }

                case SCROLL -> {
                    hero.useScroll(hero.getInventory().removeScroll(inventorySelector.getCurrentIndex()));
                    statistics.scrollsCountIncrement();

                    if (inventorySelector.getCurrentIndex() == inventorySelector.getMaxIndex() - 1) {
                        inventorySelector.setCurrentIndex(inventorySelector.getCurrentIndex() - 1);
                    }

                    inventorySelector.setMaxIndex(inventorySelector.getMaxIndex() - 1);
                }

                case FOOD -> {
                    hero.useFood(hero.getInventory().removeFood(inventorySelector.getCurrentIndex()));
                    statistics.foodCountIncrement();

                    if (inventorySelector.getCurrentIndex() == inventorySelector.getMaxIndex() - 1) {
                        inventorySelector.setCurrentIndex(inventorySelector.getCurrentIndex() - 1);
                    }

                    inventorySelector.setMaxIndex(inventorySelector.getMaxIndex() - 1);
                }

                case WEAPON -> {
                    hero.setStrength(hero.getStrength() - hero.getInventory().getWeapons().get(hero.getCurrentWeaponIndex()).getValue());
                    hero.setCurrentWeaponIndex(inventorySelector.getCurrentIndex());
                    hero.setStrength(hero.getStrength() + hero.getInventory().getWeapons().get(hero.getCurrentWeaponIndex()).getValue());
                }
            }
        } else {
            inventorySelector.processInventory(action);
        }
    }

    private boolean isInventoryState(UserAction action) {
        if (action == UserAction.ELIXIRS) {
            inventorySelector.StartInventoryState(hero.getInventory().getElixirs().size(), ItemSelectorType.ELIXIR);
            return true;
        }

        if (action == UserAction.FOOD) {
            inventorySelector.StartInventoryState(hero.getInventory().getFoods().size(), ItemSelectorType.FOOD);
            return true;
        }

        if (action == UserAction.WEAPONS) {
            inventorySelector.StartInventoryState(hero.getInventory().getWeapons().size(), ItemSelectorType.WEAPON);
            return true;
        }

        if (action == UserAction.SCROLLS) {
            inventorySelector.StartInventoryState(hero.getInventory().getScrolls().size(), ItemSelectorType.SCROLL);
            return true;
        }

        return false;
    }

    void moveHero(UserAction action) {
        int tmpX = hero.getCoordinate().getX();
        int tmpY = hero.getCoordinate().getY();

        switch (action) {
            case UP -> hero.move(Direction.UP);
            case DOWN -> hero.move(Direction.DOWN);
            case LEFT -> hero.move(Direction.LEFT);
            case RIGHT -> hero.move(Direction.RIGHT);
        }

        hero.setFoundTarget(map.isEnemy(hero.getCoordinate()));

        if (map.isCollide(hero.getCoordinate()) || hero.foundTarget()) {
            hero.getCoordinate().setX(tmpX);
            hero.getCoordinate().setY(tmpY);
        } else {
            statistics.moveCountIncrement();
        }

        if (map.isItem(hero.getCoordinate())) {
            addItem();
        }
    }

    void addItem() {
        if (hero.getInventory().addItem(map.getItemsOnLevel().get(hero.getCoordinate()))) {
            eventHandler.addEvent(getItemEvent(map.getItemsOnLevel().get(hero.getCoordinate())));
            currentEventsForSound.add(getItemEvent(map.getItemsOnLevel().remove(hero.getCoordinate())));
            ;
        } else if (map.getItemsOnLevel().get(hero.getCoordinate()) instanceof Weapon) {
            Item currentWeapon = map.getItemsOnLevel().get(hero.getCoordinate());
            eventHandler.addEvent(getItemEvent(currentWeapon));
            currentEventsForSound.add(getItemEvent(currentWeapon));

            Item inventoryWeapon;

            if (hero.getInventory().getWeapons().get(hero.getCurrentWeaponIndex()).getItemType() == WeaponsType.HANDS) {
                hero.setCurrentWeaponIndex(1);
                inventoryWeapon = hero.getInventory().getWeapons().get(hero.getCurrentWeaponIndex());
            } else {
                inventoryWeapon = hero.getInventory().getWeapons().get(hero.getCurrentWeaponIndex());
                hero.setStrength(hero.getStrength() - inventoryWeapon.getValue());
            }

            Item tmpWeapon = new Weapon(currentWeapon.getCoordinate(), currentWeapon.getValue(), currentWeapon.getItemType());

            currentWeapon.setItemType(inventoryWeapon.getItemType());
            currentWeapon.setValue(inventoryWeapon.getValue());

            inventoryWeapon.setItemType(tmpWeapon.getItemType());
            inventoryWeapon.setValue(tmpWeapon.getValue());

            hero.setStrength(hero.getStrength() + inventoryWeapon.getValue());
        }
    }

    void moveEnemy(Enemy enemy) {
        if (enemy.needToChase(hero.getCoordinate()) &&
                ((map.isNear(hero.getCoordinate(), enemy.getCoordinate()) && !enemy.seeHero()) ||
                        enemy.seeHero())) {
            enemy.setChasePath(map.connectPoints(enemy.getCoordinate(), hero.getCoordinate()));
            enemy.chaseSmart();
            enemy.setSeeHero(true);
        } else {
            enemy.setSeeHero(false);

            Coordinate tmp = new Coordinate(enemy.getCoordinate().getX(), enemy.getCoordinate().getY());
            enemy.move();

            Coordinate current = enemy.getCoordinate();

            if (map.outOfMap(current) ||
                    map.isCollide(current) ||
                    (!(enemy instanceof Ghost) &&
                            (map.connectPoints(tmp, current).isEmpty() || map.connectPoints(tmp, current).size() > 2))) {
                enemy.setCoordinate(tmp);
            }
        }
    }

    boolean isExit(UserAction action) {
        return action == UserAction.ACTION && hero.getCoordinate().equals(exit.getCoordinate());
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public GameEvent getItemEvent(Item item) {
        EventType eventType = switch (item) {
            case Elixir elixir -> switch ((ElixirType) elixir.getItemType()) {
                case AGILITY_ELIXIR -> EventType.AGILITY_ELIXIR;
                case MAX_HEALTH_ELIXIR -> EventType.MAX_HEALTH_ELIXIR;
                case STRENGTH_ELIXIR -> EventType.STRENGTH_ELIXIR;
            };

            case Food food -> switch ((FoodType) food.getItemType()) {
                case APPLE -> EventType.APPLE;
                case BREAD -> EventType.BREAD;
                case MEAT -> EventType.MEAT;
            };

            case Scroll scroll -> switch ((ScrollsType) scroll.getItemType()) {
                case AGILITY_SCROLL -> EventType.AGILITY_SCROLL;
                case STRENGTH_SCROLL -> EventType.STRENGTH_SCROLL;
                case MAXIMUM_HEALTH_SCROLL -> EventType.MAX_HEALTH_SCROLL;
            };

            case Weapon weapon -> switch ((WeaponsType) weapon.getItemType()) {
                case AXE -> EventType.AXE;
                case SWORD, HANDS -> EventType.NONE;
                case KNIFE -> EventType.KNIFE;
                case HAMMER -> EventType.HAMMER;
                case STICK -> EventType.STICK;
            };

            case Treasure ignored -> EventType.TREASURE;
            case null, default -> {
                assert item != null;
                throw new IllegalStateException("unknown item: " + item.getClass());
            }
        };

        return new GameEvent(eventType, item.getValue());
    }
}
