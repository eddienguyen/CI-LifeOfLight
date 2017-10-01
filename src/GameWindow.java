
import bases.GameObject;
import bases.events.EventManager;
import bases.inputs.CommandListener;
import bases.inputs.InputManager;
import bases.uis.InputText;
import bases.uis.StatScreen;
import bases.uis.TextScreen;
import settings.Settings;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import static java.lang.System.nanoTime;

/**
 * Created by huynq on 7/28/17.
 */

//to-do: 
public class GameWindow extends JFrame {

    private BufferedImage backBufferImage;
    private Graphics2D backBufferGraphics;

    private long lastTimeUpdate = -1;

    int playerX = 1;
    int playerY = 1;

    int width = 10;
    int height = 10;
    String[][] location = new String[width][height];

    boolean gameOn = false;

    public void loadMap() {
        for (int y = 0; y <= height - 1; y++) {
            for (int x = 0; x <= width - 1; x++) {
                if (0 == x || 0 == y || height - 1 == y || width - 1 == x) {
                    location[x][y] = " #";
                } else if (x == playerX && y == playerY) {
                    location[x][y] = " @";
                } else {
                    location[x][y] = " .";
                }
            }//width
        }//height
        location[1][0] = " s";                          //start
        location[8][9] = " e";                          //end
        String line;
        //print Map:
        for (int y = 0; y <= height - 1; y++) {
            line = "";
            for (int x = 0; x <= width - 1; x++) {
                line += location[x][y];
            }
            EventManager.pushUIMessage(line);
        }//end print map
    }//loadMap


    public GameWindow() {
        setupFont();
        setupPanels();
        setupWindow();

        InputManager.instance.addCommandListener(new CommandListener() {
            @Override
            public void onCommandFinished(String command) {

                if (command.equals("start")) {
                    EventManager.pushClearUI();
                    gameOn = true;
                    loadMap();
                    EventManager.pushUIMessage("next move ?");
                } else if (command.equals("up")) {
                    if (gameOn == true) {
                        if (playerY > 1) {
                            EventManager.pushClearUI();
                            EventManager.pushUIMessage("you moved up");
                            playerY -= 1;
                            loadMap();
                        } else EventManager.pushUIMessage("You hit the wall");
                    } else EventManager.pushUIMessage("unable to move right now");
                } else if (command.equals("down")) {
                    if (gameOn == true) {
                        if (playerY < height - 2) {
                            EventManager.pushClearUI();
                            EventManager.pushUIMessage("you moved down");
                            playerY += 1;
                            loadMap();
                        } else EventManager.pushUIMessage("You hit the wall");
                    } else EventManager.pushUIMessage("unable to move right now");
                } else if (command.equals("left")) {
                    if (gameOn == true) {
                        if (playerX > 1) {
                            EventManager.pushClearUI();
                            EventManager.pushUIMessage("you moved left");
                            playerX -= 1;
                            loadMap();
                        } else EventManager.pushUIMessage("You hit the wall");
                    } else EventManager.pushUIMessage("unable to move right now");
                } else if (command.equals("right")) {
                    if (gameOn == true) {
                        if (playerX < width - 2) {
                            EventManager.pushClearUI();
                            EventManager.pushUIMessage("you moved right");
                            playerX += 1;
                            loadMap();
                        } else EventManager.pushUIMessage("You hit the wall");
                    } else EventManager.pushUIMessage("unable to move right now");
                } else if (command.equals("help")) {
                    EventManager.pushUIMessage("type ';#0000FFstart;' to start the game");
                    EventManager.pushUIMessage("type ';#0000FFup, down, left, right;' to move your character");
                } else {
                    EventManager.pushUIMessage("Unknown command, type ';#ff0000help;' to actually get help");
                }

            }

            @Override
            public void commandChanged(String command) {

            }
        });
    }

    private void setupFont() {

    }

    private void setupPanels() {
        TextScreen textScreenPanel = new TextScreen();
        textScreenPanel.setColor(Color.BLACK);
        textScreenPanel.getSize().set(
                Settings.TEXT_SCREEN_SCREEN_WIDTH,
                Settings.TEXT_SCREEN_SCREEN_HEIGHT);
        pack();
        textScreenPanel.getOffsetText().set(getInsets().left + 20, getInsets().top + 20);
        GameObject.add(textScreenPanel);


        InputText commandPanel = new InputText();
        commandPanel.getPosition().set(
                0,
                Settings.SCREEN_HEIGHT
        );
        commandPanel.getOffsetText().set(20, 20);
        commandPanel.getSize().set(
                Settings.CMD_SCREEN_WIDTH,
                Settings.CMD_SCREEN_HEIGHT
        );
        commandPanel.getAnchor().set(0, 1);
        commandPanel.setColor(Color.BLACK);
        GameObject.add(commandPanel);


        StatScreen statsPanel = new StatScreen();
        statsPanel.getPosition().set(
                Settings.SCREEN_WIDTH,
                0
        );

        statsPanel.getAnchor().set(1, 0);
        statsPanel.setColor(Color.BLACK);
        statsPanel.getSize().set(
                Settings.STATS_SCREEN_WIDTH,
                Settings.STATS_SCREEN_HEIGHT
        );
        GameObject.add(statsPanel);

        InputManager.instance.addCommandListener(textScreenPanel);
    }


    private void setupWindow() {
        this.setSize(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        this.setVisible(true);
        this.setTitle(Settings.GAME_TITLE);
        this.addKeyListener(InputManager.instance);
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        backBufferImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        backBufferGraphics = (Graphics2D) backBufferImage.getGraphics();
    }

    public void gameLoop() {
        while (true) {
            if (-1 == lastTimeUpdate) lastTimeUpdate = nanoTime();

            long currentTime = nanoTime();

            if (currentTime - lastTimeUpdate > 17000000) {
                lastTimeUpdate = currentTime;
                GameObject.runAll();
                InputManager.instance.run();
                render(backBufferGraphics);
                repaint();
            }
        }
    }

    private void render(Graphics2D g2d) {
        g2d.setFont(Settings.DEFAULT_FONT);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);

        GameObject.renderAll(g2d);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(backBufferImage, 0, 0, null);
    }
}
