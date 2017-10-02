
import bases.GameObject;
import bases.Utils;
import bases.events.EventManager;
import bases.inputs.CommandListener;
import bases.inputs.InputManager;
import bases.uis.InputText;
import bases.uis.StatScreen;
import bases.uis.TextScreen;
import novels.Choice;
import novels.Story;
import settings.Settings;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static java.lang.System.nanoTime;

/**
 * Created by huynq on 7/28/17.
 * Modified by eddienguyen on 10/1/2017.
 */

//to-do: 
public class GameWindow extends JFrame {

    private BufferedImage backBufferImage;
    private Graphics2D backBufferGraphics;

    private long lastTimeUpdate = -1;
    //map info
    int playerX = 1;
    int playerY = 1;
    int width = 10;                                     //mapWidth
    int height = 10;                                    //mapHeght
    String[][] location = new String[width][height];

    boolean gameOn = false;
    //end map info

    Story currentStory;
    HashMap<String, Story> storyHashMap = new HashMap<>();

    void changeStory(String newStoryID) {
        currentStory = storyHashMap.get(newStoryID);
        EventManager.pushUIMessageNewLine(currentStory.text);
    }


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

        //Story info
       Story story1 = new Story(
                "E000001\n",
                "\"Tiếng kẽo kẹt buông xuống từ trần nhà, kéo theo chùm sáng lờ mờ quanh căn phòng. Không dễ để nhận ra có bao nhiêu người đang ở trong căn phòng này.\n" +
                        "Trong lúc lần mò xung quanh căn phòng, Có tiếng nói vang lên :\n" +
                        "Này chàng trai trẻ, chàng trai tên là gì vậy?\"\n",
                new Choice("sang", "E000002"),
                new Choice("other", "E000003")
        );
        Story story2 = new Story(
                "E000002",
                "\"Được đó quả là 1 cái tên kiên cường và sáng sủa.\n" +
                        "Không biết cậu có nhớ mình đã từng là 1 chiến binh vĩ đại đến từ đế quốc Neflim cổ xưa hay chăng ?\"\n",
                new Choice("yes", "E000010"),
                new Choice("no", "E000005")
        );
        Story story3 = new Story(
                "E000003",
                "\"Cậu có nhầm không ? Chắc các viên đá đã làm cậu mất trí rồi. Cậu cố nhớ lại xem.\n" +
                        "Tên cậu là ?\"\n",
                new Choice("sang", "E000002"),
                new Choice("other", "E000004")
        );
        Story story4 = new Story(
                "E000004",
                "\"Không thể như vậy được. Thần Murk không nói sai, tên cậu không thể là  như vậy được.\n" +
                        "Này chàng trai trẻ tên cậu có phải là \"\"Sang\"\" không ? Hay tên cậu là ? \"\n",
                new Choice("sang", "E000002"),
                new Choice("other", "E000004")
        );
        Story story5 = new Story(
                "E000005",
                "\"Đế quốc Neflim là đế chế hùng mạnh bao phủ toàn cõi lục địa này cách đây hàng ngàn năm trước.\n" +
                        "Thậm chí lực lượng của đế chế còn làm tộc Rồng có vài phần chú ý.\n" +
                        "\n" +
                        "Sức mạnh lớn luôn đi theo hiểm họa tương ứng. Tổng lãnh quỷ sai đã sớm để ý đến sức mạnh to lơn này từ trước đó rất lâu.\n" +
                        "và hắn đã đặt vào nó 1 điếm yếu chí mạng. Đó là lòng tham và sự đố kị.\"\n",
                new Choice("next", "E000006")
        );
        Story story6 = new Story(
                "E000006",
                "\"Hắn biết cốt lõi của sức manh này chính là niềm tin vững chãi vào đấng sáng thế. Do vậy còn ai có thể củng cố niềm tin vào ngài khi mà còn đang mải đấu đá lẫn nhau.\n" +
                        "Dần dần thế giới suy tàn và những thế lực hắc ám nổi dậy. Toàn bộ nhân loại đã gần như bị tiêu diệt nếu như không có sự giúp đợ của :\n" +
                        "Noah và con tàu của ông ấy.\n" +
                        "\n" +
                        "Đó là những gì kinh thánh ghi lại. Thực ra còn 1 người nữa đó chính là LightLord. Ngài đã hi sinh tính mạng để cứu lấy muôn loài và nhường phần chiến công của mình để chuộc lỗi cho Noah.\n" +
                        "\n" +
                        "Chuyện của Noah là 1 câu truyện khác mà cậu có thể tìm hiểu sau.\"\n",
                new Choice("next", "E000007")
        );
        Story story7 = new Story(
                "E000007",
                "\"Uhm! 1 câu hỏi nhỏ cho cậu nhé. Theo cậu với tính cách vẫn như bây giờ thì lúc đó cậu đang là :\n" +
                        "A  - Chiến Binh\n" +
                        "B - Đạo tặc\n" +
                        "C - Pháp sư\n" +
                        "D - Cung thủ\n" +
                        "E - Võ sư\n" +
                        "F - Kiếm sư\n" +
                        "G - Triệu hồi sư\n" +
                        "H - Y sư\n" +
                        "I - Dân thường\"\n",
                new Choice("A", "E000008r"),
                new Choice("B", "E000008r"),
                new Choice("C", "E000008r"),
                new Choice("D", "E000008r"),
                new Choice("E", "E000008r"),
                new Choice("G", "E000008r"),
                new Choice("H", "E000008r"),
                new Choice("I", "E000008w")
        );
        Story story8w = new Story(
                "E000008w",
                "\"Nhầm rồi, cậu đoán lại nhé. Theo cậu với tính cách vẫn như bây giờ thì lúc đó cậu đang là :\n" +
                        "A  - Chiến Binh\n" +
                        "B - Đạo tặc\n" +
                        "C - Pháp sư\n" +
                        "D - Cung thủ\n" +
                        "E - Võ sư\n" +
                        "F - Kiếm sư\n" +
                        "G - Triệu hồi sư\n" +
                        "H - Y sư\n" +
                        "I - Dân thường\"\n",
                new Choice("A", "E000008r"),
                new Choice("B", "E000008r"),
                new Choice("C", "E000008r"),
                new Choice("D", "E000008r"),
                new Choice("E", "E000008r"),
                new Choice("G", "E000008r"),
                new Choice("H", "E000008r"),
                new Choice("I", "E000008w")
        );
        Story story8r = new Story(
                "E000008r",
                "\"Chính xác.! cậu quả là người không ngu thì khôn vô cùng, chả mấy chốc đã đoán ra mình là ai.\n" +
                        "Là dân thường cậu đã thấm nhuần nỗi khổ nhưng lại có đức hi sinh cùng lòng vị tha. Đó là tất cả những phẩm chất cần có của 1 người hùng nắm giữ sức mạnh của ánh sáng.\"\n",
                new Choice("next", "E000009")
        );
        Story story9 = new Story(
                "E000009",
                "Sau hàng tháng chiến đấu liên tiếp không nghỉ cậu đã cầm chân đủ lâu lũ quái vật để cho Noah trốn thoát chỉ với 1 lời \"Xin lỗi!\".\n",
                new Choice("next", "E000010")
        );
        Story story10 = new Story(
                "E000010",
                "\"Chính vì cậu là 1 người anh Hùng như thế nên không biết cậu có ý cứu giúp thế giới này 1 lần nữa hay không ?\n",
                new Choice("yes", "E000020"),
                new Choice("no", "E999999")
        );
        Story story20 = new Story(
                "E000020",
                "Một hành trình mới. ... etc\n"
        );
        Story storyEnd = new Story(
                "E999999",
                "\"\"\"Không Không CLGT\"\" dậy đi sửa mạng đi mày!\n" +
                        "Sáng nửa tỉnh nửa mê lật đật bỏ đi. Không màng bận tâm đến giấc mơ kỳ lạ vừa rồi.\"\n"
        );
        //end Story info

        //put stories to hashMap:
        storyHashMap.put(story1.id, story1);
        storyHashMap.put(story2.id, story2);
        storyHashMap.put(story3.id, story3);
        storyHashMap.put(story4.id, story4);
        storyHashMap.put(story5.id, story5);
        storyHashMap.put(story6.id, story6);
        storyHashMap.put(story7.id, story7);
        storyHashMap.put(story8r.id, story8r);
        storyHashMap.put(story8w.id, story8w);
        storyHashMap.put(story9.id, story9);
        storyHashMap.put(story10.id, story10);
        storyHashMap.put(story20.id, story20);
        storyHashMap.put(storyEnd.id, storyEnd);


        currentStory = story1;
        EventManager.pushUIMessage(currentStory.text);

        InputManager.instance.addCommandListener(new CommandListener() {

            @Override
            public void onCommandFinished(String command) {
                //EventManager.pushUIMessageNewLine("");
                EventManager.pushUIMessage("\n");
                EventManager.pushUIMessageNewLine(command);
                EventManager.pushUIMessageNewLine("");

                for (Choice choice : currentStory.choices) {
                    if (choice.match(command)) {
                        changeStory(choice.to);
                        break;
                    }else if (choice.value.equalsIgnoreCase("other")){
                        changeStory(choice.to);
                        break;
                    }
                }

                //movement for player in map
/*
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
                    EventManager.pushUIMessageNewLine("type ';#0000FFstart;' to start the game");
                    EventManager.pushUIMessage("type ';#0000FFup, down, left, right;' to move your character");
                } else {
                    EventManager.pushHelpMessage();
                }
*/
                //end movement for player in map
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
