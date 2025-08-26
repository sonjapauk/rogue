package org.example.view;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;
import org.example.*;
import org.example.model.game.AllAboutHero.Hero;
import org.example.model.game.level.MapCell;
import org.example.model.menu.MenuState;
import org.example.model.UserAction;
import org.example.model.game.item.*;
import org.example.rating.Statistics;

import java.io.IOException;
import java.util.List;

public class View implements Presentation {

    private static final int SCREEN_WIDTH = 90;
    private static final int SCREEN_HEIGHT = 50;
    private static final int START_X = 10;
    private static final int START_Y = 10;

    private int frame = 0;
    private final Screen screen;

    public View() throws IOException {
        TerminalSize size = new TerminalSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        Terminal terminal = new DefaultTerminalFactory().setTerminalEmulatorTitle("Rogue").setInitialTerminalSize(size).createTerminal();

        screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null);
    }

    @Override
    public void close() throws IOException {
        screen.close();
    }

    @Override
    public void render(GameInfo currentGameInfo) throws IOException {
        screen.doResizeIfNecessary();
        screen.clear();

        if (currentGameInfo.getGlobalState() == GlobalState.MENU) {
            printMenu(currentGameInfo.getCurrentMenuState(), currentGameInfo.getHasSave());
        } else if (currentGameInfo.getGlobalState() == GlobalState.GAME) {
            if (currentGameInfo.getCurrentGameState() == GameState.GAME) {
                printGame(currentGameInfo);
            } else if (currentGameInfo.getCurrentGameState() == GameState.INVENTORY) {
                printInventory(currentGameInfo);
            } else if (currentGameInfo.getCurrentGameState() == GameState.WIN) {
                printWin();
            } else if (currentGameInfo.getCurrentGameState() == GameState.GAME_OVER) {
                printGameover();
            }

            printStats(currentGameInfo.getHero(), currentGameInfo.getCurrentLevel());
        } else if (currentGameInfo.getGlobalState() == GlobalState.RATING) {
            printRating(currentGameInfo);
        }

        screen.refresh();
    }

    void printWin() {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        String[] winArt = {
                "                      You win!",
                "",
                "   Now you can touch the grass and enjoy your treasure!",
                "",
                "               Press enter to go menu"
        };

        for (int i = 0; i < winArt.length; i++) {
            textGraphics.putString(START_X, START_Y + i, winArt[i]);
        }
    }

    void printGameover() {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        String[] gameOverArt = {
                "                     You are dead!",
                "",
                "          Try to be better next time, loser",
                "",
                "                Press enter to go menu"
        };

        for (int i = 0; i < gameOverArt.length; i++) {
            textGraphics.putString(START_X, START_Y + i, gameOverArt[i]);
        }
    }


    void printRating(GameInfo gameInfo) {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        int startX = 2;
        int startY = 2;

        List<Statistics> ratingList = gameInfo.getRating().getStatisticsList();

        textGraphics.putString(startX, startY, "=== Player Rating ===");

        startY += 2;

        textGraphics.putString(startX, startY, String.format(
                "%-3s %-10s %-8s %-8s %-8s %-8s %-8s %-8s %-10s %-6s",
                "#", "Treasure", "Elixirs", "Scrolls", "Food", "Moves",
                "Defeated", "Hits", "Enemy Hits", "Level"));

        startY++;

        textGraphics.putString(startX, startY, "----------------------------------------------------------------------------------------");

        startY++;

        for (int i = 0; i < ratingList.size(); i++) {
            Statistics stats = ratingList.get(i);

            textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);

            String line = String.format(
                    "%-3d %-10d %-8d %-8d %-8d %-8d %-8d %-8d %-10d %-6d",
                    (i + 1),
                    stats.getTreasureCount(),
                    stats.getElixirsCount(),
                    stats.getScrollsCount(),
                    stats.getFoodCount(),
                    stats.getMoveCount(),
                    stats.getDefeatEnemiesCount(),
                    stats.getHitsCount(),
                    stats.getEnemyHistsCount(),
                    stats.getLevel()
            );

            textGraphics.putString(startX, startY + i, line);
        }

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.putString(startX, startY + ratingList.size() + 2, "← Press ESC to return");
    }



    @Override
    public UserAction getUserAction() throws IOException {
        return convert(getUserInput());
    }

    public KeyStroke getUserInput() throws IOException {
        return screen.pollInput();
    }

    public UserAction convert(KeyStroke userInput) {
        if (userInput == null) {
            return UserAction.NONE;
        }

        return switch (userInput.getKeyType()) {
            case Character -> {
                char c = Character.toLowerCase(userInput.getCharacter());
                yield switch (c) {
                    case 'w', 'ц' -> UserAction.UP;
                    case 'a', 'ф' -> UserAction.LEFT;
                    case 's', 'ы' -> UserAction.DOWN;
                    case 'd', 'в' -> UserAction.RIGHT;
                    case 'h', 'р' -> UserAction.WEAPONS;
                    case 'j', 'о' -> UserAction.FOOD;
                    case 'k', 'л' -> UserAction.ELIXIRS;
                    case 'e', 'у' -> UserAction.SCROLLS;
                    case ' ' -> UserAction.SKIP;
                    case 'z', 'я' -> UserAction.SKIP_ALL;
                    default -> UserAction.NONE;
                };
            }

            case Enter -> UserAction.ACTION;
            case Escape -> UserAction.EXIT;
            default -> UserAction.NONE;
        };

    }

    public void printMenu(MenuState currentMenuState, boolean hasSave) {
        TextGraphics textGraphics = screen.newTextGraphics();

        int menuX = 40;
        int menuY = 20;

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        textGraphics.putString(menuX, menuY, "New Game");

        if (!hasSave) {
            textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
            textGraphics.putString(menuX, menuY + 2, "Continue");
            textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        } else {
            textGraphics.putString(menuX, menuY + 2, "Continue");
        }

        textGraphics.putString(menuX, menuY + 4, "Rating");
        textGraphics.putString(menuX, menuY + 6, "Controls");
        textGraphics.putString(menuX, menuY + 8, "Exit");

        textGraphics.enableModifiers(SGR.BOLD);

        switch (currentMenuState) {
            case NEW_GAME -> textGraphics.putString(menuX - 3, menuY, "-> New Game");
            case CONTINUE -> textGraphics.putString(menuX - 3, menuY + 2, "-> Continue");
            case RATING -> textGraphics.putString(menuX - 3, menuY + 4, "-> Rating");
            case CONTROL_INFO -> {
                textGraphics.putString(menuX - 3, menuY + 6, "-> Controls");
                printControls();
            }
            case EXIT -> textGraphics.putString(menuX - 3, menuY + 8, "-> Exit");
        }
    }

    void printControls() {
        TextGraphics tg = screen.newTextGraphics();

        int x = 50;
        int y = 18;
        int width = 26;
        int height = 15;

        TextColor borderColor = TextColor.ANSI.GREEN_BRIGHT;
        TextColor bgColor = TextColor.ANSI.BLACK;

        tg.setForegroundColor(borderColor);
        tg.setBackgroundColor(bgColor);

        tg.setCharacter(x, y, '╔');

        for (int i = 1; i < width - 1; i++) {
            tg.setCharacter(x + i, y, '═');
        }

        tg.setCharacter(x + width - 1, y, '╗');

        for (int i = 1; i < height - 1; i++) {
            tg.setCharacter(x, y + i, '║');
            tg.setCharacter(x + width - 1, y + i, '║');
        }

        tg.setCharacter(x, y + height - 1, '╚');

        for (int i = 1; i < width - 1; i++) {
            tg.setCharacter(x + i, y + height - 1, '═');
        }

        tg.setCharacter(x + width - 1, y + height - 1, '╝');

        tg.setForegroundColor(TextColor.ANSI.GREEN);
        tg.enableModifiers(SGR.BOLD);

        String[] lines = {
                "Controls:",
                "",
                "Move: W / A / S / D",
                "Action: Enter",
                "Exit: Esc",
                "",
                "H - Weapons",
                "J - Food",
                "K - Elixirs",
                "E - Scrolls",
                "Space - Skip Msg",
                "Z - Skip all Msg"
        };

        for (int i = 0; i < lines.length; i++) {
            tg.putString(x + 2, y + 1 + i, lines[i]);
        }
    }

    void printGame(GameInfo gameInfo) {
        printMap(gameInfo.getFinalMap().getFinalMap());
        printEvent(gameInfo.getEventHandler().getCurrentEvent());
        printAnimatedExit(gameInfo.getExit().getCoordinate().getY(), gameInfo.getExit().getCoordinate().getX(), gameInfo.getFinalMap().getFinalMap());

        printHero(gameInfo.getHero().getCoordinate().getY(), gameInfo.getHero().getCoordinate().getX());
    }


    void printEvent(GameEvent event) {
        if (event.getCurrentEvent() != EventType.NONE) {
            TextGraphics textGraphics = screen.newTextGraphics();

            if (event.getCurrentEvent() == EventType.TREASURE) {
                textGraphics.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
                textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            } else if (event.getCurrentEvent() == EventType.APPLE || event.getCurrentEvent() == EventType.BREAD || event.getCurrentEvent() == EventType.MEAT) {
                textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            } else if (event.getCurrentEvent() == EventType.AGILITY_ELIXIR || event.getCurrentEvent() == EventType.STRENGTH_ELIXIR || event.getCurrentEvent() == EventType.MAX_HEALTH_ELIXIR) {
                textGraphics.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
                textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            } else if (event.getCurrentEvent() == EventType.AGILITY_SCROLL || event.getCurrentEvent() == EventType.STRENGTH_SCROLL || event.getCurrentEvent() == EventType.MAX_HEALTH_SCROLL) {
                textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
                textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            } else if (event.getCurrentEvent() == EventType.HERO_HIT_SUCCESS || event.getCurrentEvent() == EventType.HERO_HIT_FAIL || event.getCurrentEvent() == EventType.KILL_ENEMY || event.getCurrentEvent() == EventType.ENEMY_HIT_FAIL || event.getCurrentEvent() == EventType.ENEMY_HIT_SUCCESS) {
                textGraphics.setForegroundColor(TextColor.ANSI.RED);
                textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            } else {
                textGraphics.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
                textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            }

            int boxX = 5;
            int boxY = 1;
            int boxWidth = 50;
            int boxHeight = 5;

            textGraphics.setCharacter(boxX, boxY, '╔');
            textGraphics.setCharacter(boxX + boxWidth - 1, boxY, '╗');
            textGraphics.setCharacter(boxX, boxY + boxHeight - 1, '╚');
            textGraphics.setCharacter(boxX + boxWidth - 1, boxY + boxHeight - 1, '╝');

            for (int x = boxX + 1; x < boxX + boxWidth - 1; x++) {
                textGraphics.setCharacter(x, boxY, '═');
                textGraphics.setCharacter(x, boxY + boxHeight - 1, '═');
            }

            for (int y = boxY + 1; y < boxY + boxHeight - 1; y++) {
                textGraphics.setCharacter(boxX, y, '║');
                textGraphics.setCharacter(boxX + boxWidth - 1, y, '║');
            }

            String line1 = "";
            String line2 = "";

            switch (event.getCurrentEvent()) {
                case STRENGTH_ELIXIR -> {
                    line1 = "You picked up a Strength Elixir";
                    line2 = "(+" + event.getValue() + " STR)";
                }
                case AGILITY_ELIXIR -> {
                    line1 = "You picked up an Agility Elixir";
                    line2 = "(+" + event.getValue() + " AGI)";
                }
                case MAX_HEALTH_ELIXIR -> {
                    line1 = "You picked up a Health Elixir";
                    line2 = "(+" + event.getValue() + " Max HP)";
                }
                case APPLE -> {
                    line1 = "You picked up an Apple";
                    line2 = "(+" + event.getValue() + " HP)";
                }
                case BREAD -> {
                    line1 = "You picked up some Bread";
                    line2 = "(+" + event.getValue() + " HP)";
                }
                case MEAT -> {
                    line1 = "You picked up some Meat";
                    line2 = "(+" + event.getValue() + " HP)";
                }
                case STRENGTH_SCROLL -> {
                    line1 = "You picked up a Strength Scroll";
                    line2 = "(+" + event.getValue() + " STR permanently)";
                }
                case AGILITY_SCROLL -> {
                    line1 = "You picked up an Agility Scroll";
                    line2 = "(+" + event.getValue() + " AGI permanently)";
                }
                case MAX_HEALTH_SCROLL -> {
                    line1 = "You picked up a Health Scroll";
                    line2 = "(+" + event.getValue() + " Max HP permanently)";
                }
                case STICK -> {
                    line1 = "You picked up a Stick";
                    line2 = "(+" + event.getValue() + " ATK)";
                }
                case KNIFE -> {
                    line1 = "You picked up a Knife";
                    line2 = "(+" + event.getValue() + " ATK)";
                }
                case HAMMER -> {
                    line1 = "You picked up a Hammer";
                    line2 = "(+" + event.getValue() + " ATK)";
                }
                case AXE -> {
                    line1 = "You picked up an Axe";
                    line2 = "(+" + event.getValue() + " ATK)";
                }
                case TREASURE -> {
                    line1 = "You found a Treasure!";
                    line2 = "(Value: " + event.getValue() + ")";
                }
                case HERO_HIT_SUCCESS -> {
                    line1 = "You hit an Enemy!";
                    line2 = "(DMG: " + event.getValue() + ")"; // enemy health? -X hp?
                }
                case HERO_HIT_FAIL -> {
                    line1 = "You missed an Enemy!";
                    line2 = "(DMG: " + event.getValue() + ")";
                }
                case KILL_ENEMY -> {
                    line1 = "You killed an Enemy!";
                    line2 = "(Treasure: " + event.getValue() + ")";
                }
                case ENEMY_HIT_SUCCESS -> {
                    line1 = "Enemy hit you!";
                    line2 = "(DMG: " + event.getValue() + ")";
                }
                case ENEMY_HIT_FAIL -> {
                    line1 = "Enemy missed you!";
                    line2 = "(DMG: " + event.getValue() + ")";
                }
                case ENEMY_IS_STUNNED -> line1 = "Enemy is stunned!";
                case HERO_IS_STUNNED -> line1 = "You are stunned";
                case NONE -> {
                    return;
                }

            }

            int textX = boxX + 2;
            textGraphics.putString(textX, boxY + 1, padRight(line1, boxWidth - 4));
            textGraphics.putString(textX, boxY + 2, padRight(line2, boxWidth - 4));

            String prompt = "Press Space to skip msg or Z to skip all msg";
            int promptX = boxX + (boxWidth - prompt.length()) / 2 - 2;
            textGraphics.putString(promptX, boxY + boxHeight, prompt);
        }
    }


    private String padRight(String text, int length) {
        return text + " ".repeat(Math.max(0, length - text.length()));
    }


    void printHero(int x, int y) {
        TextCharacter currentChar = TextCharacter.fromCharacter('@', TextColor.ANSI.RED, TextColor.ANSI.BLACK)[0];
        screen.setCharacter(x * 2, y + 7, currentChar);
    }

    void printAnimatedExit(int x, int y, MapCell[][] map) {
        if (map[x][y].getVisible()) {
            frame++;

            if (frame % 14 > 7) {
                screen.setCharacter(x * 2, y + 7, TextCharacter.fromCharacter('=', TextColor.ANSI.BLACK, TextColor.ANSI.YELLOW)[0]);
            } else {
                screen.setCharacter(x * 2, y + 7, TextCharacter.fromCharacter(' ', TextColor.ANSI.YELLOW, TextColor.ANSI.YELLOW)[0]);
            }
        }
    }


    void printMap(MapCell[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (!map[i][j].getVisible()) {
                    continue;
                }

                int value = map[i][j].getValue();

                TextCharacter character = switch (value) {
                    case MapCellValues.EMPTY ->
                            TextCharacter.fromCharacter(' ', TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.VERTICAL_WALL ->
                            TextCharacter.fromCharacter('║', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.HORIZONTAL_WALL ->
                            TextCharacter.fromCharacter('═', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.TOP_LEFT_CORNER ->
                            TextCharacter.fromCharacter('╔', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.BOTTOM_LEFT_CORNER ->
                            TextCharacter.fromCharacter('╚', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.BOTTOM_RIGHT_CORNER ->
                            TextCharacter.fromCharacter('╝', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.TOP_RIGHT_CORNER ->
                            TextCharacter.fromCharacter('╗', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.FLOOR ->
                            TextCharacter.fromCharacter('.', TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.GRASS ->
                            TextCharacter.fromCharacter('░', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.DOOR_HORIZONTAL ->
                            TextCharacter.fromCharacter('─', TextColor.ANSI.GREEN, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.DOOR_VERTICAL ->
                            TextCharacter.fromCharacter('│', TextColor.ANSI.GREEN, TextColor.ANSI.BLACK)[0];

                    case MapCellValues.PLAYER ->
                            TextCharacter.fromCharacter('@', TextColor.ANSI.RED, TextColor.ANSI.BLACK)[0];

                    case MapCellValues.NPC ->
                            TextCharacter.fromCharacter('O', TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK)[0];

                    case MapCellValues.AGILITY_ELIXIR ->
                            TextCharacter.fromCharacter('e', TextColor.ANSI.GREEN, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.MAX_HP_ELIXIR ->
                            TextCharacter.fromCharacter('e', TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.STRENGTH_ELIXIR ->
                            TextCharacter.fromCharacter('e', TextColor.ANSI.RED, TextColor.ANSI.BLACK)[0];

                    case MapCellValues.APPLE ->
                            TextCharacter.fromCharacter('a', TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.BREAD ->
                            TextCharacter.fromCharacter('b', TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.MEAT ->
                            TextCharacter.fromCharacter('m', TextColor.ANSI.RED, TextColor.ANSI.BLACK)[0];

                    case MapCellValues.AGILITY_SCROLL ->
                            TextCharacter.fromCharacter('s', TextColor.ANSI.GREEN, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.STRENGTH_SCROLL ->
                            TextCharacter.fromCharacter('s', TextColor.ANSI.RED, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.MAX_HP_SCROLL ->
                            TextCharacter.fromCharacter('s', TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK)[0];

                    case MapCellValues.AXE ->
                            TextCharacter.fromCharacter('A', TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.SWORD ->
                            TextCharacter.fromCharacter('S', TextColor.ANSI.CYAN, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.KNIFE ->
                            TextCharacter.fromCharacter('K', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.HAMMER ->
                            TextCharacter.fromCharacter('H', TextColor.ANSI.BLUE, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.STICK ->
                            TextCharacter.fromCharacter('|', TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.HANDS ->
                            TextCharacter.fromCharacter('F', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK)[0];

                    case MapCellValues.GOLD ->
                            TextCharacter.fromCharacter('$', TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLACK)[0];

                    case MapCellValues.ZOMBIE ->
                            TextCharacter.fromCharacter('Z', TextColor.ANSI.GREEN, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.ORC ->
                            TextCharacter.fromCharacter('O', TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.VAMPIRE ->
                            TextCharacter.fromCharacter('V', TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.GHOST ->
                            TextCharacter.fromCharacter('g', TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.SKELETON ->
                            TextCharacter.fromCharacter('s', TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK)[0];
                    case MapCellValues.MIMIC ->
                            TextCharacter.fromCharacter('m', TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK)[0];

                    default -> TextCharacter.fromCharacter('#', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK)[0];
                };

                screen.setCharacter(i * 2, j + 7, character);

                screen.setCharacter(i * 2 - 1, j + 7,
                        TextCharacter.fromCharacter(' ', TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLACK)[0]);

                if (value == MapCellValues.HORIZONTAL_WALL
                        || value == MapCellValues.BOTTOM_RIGHT_CORNER
                        || value == MapCellValues.TOP_RIGHT_CORNER) {
                    screen.setCharacter(i * 2 - 1, j + 7,
                            TextCharacter.fromCharacter('═', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)[0]);
                } else if (value == MapCellValues.GRASS) {
                    screen.setCharacter(i * 2 - 1, j + 7,
                            TextCharacter.fromCharacter('░', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)[0]);
                } else if (value == MapCellValues.DOOR_HORIZONTAL) {
                    screen.setCharacter(i * 2 - 1, j + 7,
                            TextCharacter.fromCharacter('─', TextColor.ANSI.GREEN, TextColor.ANSI.BLACK)[0]);
                } else if (value == MapCellValues.DOOR_VERTICAL
                        && (map[i - 1][j].getValue() == MapCellValues.GRASS
                        || map[i - 1][j].getValue() == MapCellValues.DOOR_VERTICAL)) {
                    screen.setCharacter(i * 2 - 1, j + 7,
                            TextCharacter.fromCharacter('░', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)[0]);
                }
            }
        }
    }


    void printInventory(GameInfo currentGameInfo) {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        textGraphics.putString(30, 30, "Press esc to return");

        int chooseItemIndex = currentGameInfo.getInventorySelector().getCurrentIndex();

        switch (currentGameInfo.getInventorySelector().getCurrentType()) {
            case ELIXIR -> printElixirs(currentGameInfo.getHero().getInventory().getElixirs(), chooseItemIndex);
            case SCROLL -> printScrolls(currentGameInfo.getHero().getInventory().getScrolls(), chooseItemIndex);
            case FOOD -> printFood(currentGameInfo.getHero().getInventory().getFoods(), chooseItemIndex);
            case WEAPON ->
                    printWeapons(currentGameInfo.getHero().getInventory().getWeapons(), chooseItemIndex, currentGameInfo.getHero().getCurrentWeaponIndex());
        }
    }

    private void printFood(List<Food> foods, int chooseItemIndex) {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        int row = 5;
        int column = 34;

        textGraphics.putString(column, row - 4, "YOUR FOOD");

        for (int i = 0; i < foods.size(); i++, row += 2) {
            switch (foods.get(i).getItemType()) {
                case FoodType.MEAT ->
                        textGraphics.putString(column, row, "Meat (" + foods.get(i).getValue() + " HP, restores)");
                case FoodType.APPLE ->
                        textGraphics.putString(column, row, "Apple (" + foods.get(i).getValue() + " HP, restores)");
                case FoodType.BREAD ->
                        textGraphics.putString(column, row, "Bread (" + foods.get(i).getValue() + " HP, restores)");

                default -> throw new IllegalStateException("Unexpected value: " + foods.get(i).getItemType());
            }

            if (chooseItemIndex == i) {
                textGraphics.putString(column - 3, row, "->");
            }
        }
    }

    private void printWeapons(List<Weapon> weapons, int chooseItemIndex, int currentWeaponIndex) {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        int row = 5;
        int column = 34;

        textGraphics.putString(column, row - 4, "YOUR WEAPONS");

        for (int i = 0; i < weapons.size(); i++, row += 2) {
            switch (weapons.get(i).getItemType()) {
                case WeaponsType.AXE ->
                        textGraphics.putString(column, row, "Axe (" + weapons.get(i).getValue() + " dmg)");
                case WeaponsType.HAMMER ->
                        textGraphics.putString(column, row, "Hammer (" + weapons.get(i).getValue() + " dmg)");
                case WeaponsType.HANDS ->
                        textGraphics.putString(column, row, "Hands (" + weapons.get(i).getValue() + " dmg)");
                case WeaponsType.KNIFE ->
                        textGraphics.putString(column, row, "Knife (" + weapons.get(i).getValue() + " dmg)");
                case WeaponsType.SWORD ->
                        textGraphics.putString(column, row, "Sword (" + weapons.get(i).getValue() + " dmg)");
                case WeaponsType.STICK ->
                        textGraphics.putString(column, row, "Stick (" + weapons.get(i).getValue() + " dmg)");

                default -> throw new IllegalStateException("Unexpected value: " + weapons.get(i).getItemType());
            }

            if (chooseItemIndex == i) {
                textGraphics.putString(column - 3, row, "->");
            }

            if (currentWeaponIndex == i) {
                textGraphics.putString(column + 20, row, "●");
            }
        }
    }

    private void printScrolls(List<Scroll> scrolls, int chooseItemIndex) {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        int row = 5;
        int column = 34;

        textGraphics.putString(column, row - 4, "YOUR SCROLLS");

        for (int i = 0; i < scrolls.size(); i++, row += 2) {
            switch (scrolls.get(i).getItemType()) {
                case ScrollsType.AGILITY_SCROLL ->
                        textGraphics.putString(column, row, "Agility scroll (" + scrolls.get(i).getValue() + ", permanent)");
                case ScrollsType.MAXIMUM_HEALTH_SCROLL ->
                        textGraphics.putString(column, row, "Max health scroll (" + scrolls.get(i).getValue() + ", permanent)");
                case ScrollsType.STRENGTH_SCROLL ->
                        textGraphics.putString(column, row, "Strength scroll (" + scrolls.get(i).getValue() + ", permanent)");

                default -> throw new IllegalStateException("Unexpected value: " + scrolls.get(i).getItemType());
            }

            if (chooseItemIndex == i) {
                textGraphics.putString(column - 3, row, "->");
            }
        }
    }

    private void printElixirs(List<Elixir> elixirs, int chooseItemIndex) {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        int row = 5;
        int column = 34;

        textGraphics.putString(column, row - 4, "YOUR ELIXIRS");

        for (int i = 0; i < elixirs.size(); i++, row += 2) {
            switch (elixirs.get(i).getItemType()) {
                case ElixirType.AGILITY_ELIXIR ->
                        textGraphics.putString(column, row, "Agility elixir (" + elixirs.get(i).getValue() + ", temporary)");
                case ElixirType.MAX_HEALTH_ELIXIR ->
                        textGraphics.putString(column, row, "Max health elixir (" + elixirs.get(i).getValue() + ", temporary)");
                case ElixirType.STRENGTH_ELIXIR ->
                        textGraphics.putString(column, row, "Strength elixir (" + elixirs.get(i).getValue() + ", temporary)");

                default -> throw new IllegalStateException("Unexpected value: " + elixirs.get(i).getItemType());
            }

            if (chooseItemIndex == i) {
                textGraphics.putString(column - 3, row, "->");
            }
        }
    }

    void printStats(Hero hero, int level) {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        int row = 47;
        int column = 10;

        textGraphics.putString(column, row, "Level:" + level + "/" + 21);
        column += 13;
        textGraphics.putString(column, row, "Hits:" + hero.getCurrentHealth() + "/" + hero.getMaxHealth());
        column += 13;
        textGraphics.putString(column, row, "Str:" + hero.getStrength());
        column += 10;
        textGraphics.putString(column, row, "Agl:" + hero.getAgility());
        column += 10;
        textGraphics.putString(column, row, "gold:" + hero.getInventory().getTreasures().getValue());
    }
}
