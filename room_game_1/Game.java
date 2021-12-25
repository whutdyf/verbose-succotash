/**
 * 该类是“World-of-Zuul”应用程序的主类。
 * 《World of Zuul》是一款简单的文本冒险游戏。用户可以在一些房间组成的迷宫中探险。
 * 你们可以通过扩展该游戏的功能使它更有趣!.
 *
 * 如果想开始执行这个游戏，用户需要创建Game类的一个实例并调用“play”方法。
 *
 * Game类的实例将创建并初始化所有其他类:它创建所有房间，并将它们连接成迷宫；它创建解析器
 * 接收用户输入，并将用户输入转换成命令后开始运行游戏。
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 1.0
 */
package cn.edu.whut.sept.zuul;

import java.util.*;

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Player player;
    private Random r;
    private List<Room> back_line = new LinkedList<Room>();
    private HashMap<Integer, Room> Rooms;

    /**
     * 创建游戏并初始化内部数据和解析器.
     */
    public Game()
    {
        r = new Random();
        Rooms = new HashMap<>();
        createRooms();
        createPlayer();
        parser = new Parser();
    }

    /**
     * 创建所有房间对象并连接其出口用以构建迷宫.
     * 设置每个房间的物品和重量
     * 并随机将cookie随机放置在一个房间中
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");

        Rooms.put(1, outside);
        Rooms.put(2, theater);
        Rooms.put(3, pub);
        Rooms.put(4, lab);
        Rooms.put(5, office);

        int x = r.nextInt(5) + 1;

        Room temporary_room;
        temporary_room = Rooms.get(x);
        temporary_room.set_items("cookie", 0);

        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.set_items("car", 50);
        outside.set_items("garbage", 3);
        outside.set_items("bicycle", 10);

        theater.setExit("west", outside);
        theater.set_items("chair", 4);
        theater.set_items("table", 8);

        pub.setExit("east", outside);
        pub.set_items("chair", 4);
        pub.set_items("beer", 2);

        lab.setExit("north", outside);
        lab.setExit("east", office);
        lab.set_items("chair", 4);
        lab.set_items("computer", 15);

        office.setExit("west", lab);
        office.set_items("chair", 4);
        office.set_items("computer", 15);
        office.set_items("file", 1);

        currentRoom = outside;  // start game outside
    }

    /**
     * 创建玩家并设置名字，并且初始玩家负重单位为30
     */
    private void createPlayer()
    {
        String name = "";
        System.out.print("create player name: ");
        Scanner word = new Scanner(System.in);
        name = word.next();
        player = new Player(name, 30, currentRoom);
        System.out.println("Player creation success");
    }

    /**
     *  游戏主控循环，直到用户输入退出命令后结束整个程序.
     */
    public void play()
    {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * 向用户输出欢迎信息.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * 执行用户输入的游戏指令.
     * @param command 待处理的游戏指令，由解析器从用户输入内容生成.
     * @return 如果执行的是游戏结束指令，则返回true，否则返回false.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if(commandWord.equals("back")){
            back_Room();
        }
        else if(commandWord.equals("look")) {
            look_Room();
        }
        else if(commandWord.equals("items")) {
            look_items();
        }
        else if(commandWord.equals("take")) {
            take_items(command);
        }
        else if(commandWord.equals("drop")) {
            drop_items(command);
        }
        else if(commandWord.equals("eat")) {
            eat(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * 执行help指令，在终端打印游戏帮助信息.
     * 此处会输出游戏中用户可以输入的命令列表
     */
    private void printHelp()
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * 执行go指令，向房间的指定方向出口移动，如果该出口连接了另一个房间，则会进入该房间，
     * 否则打印输出错误提示信息.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            back_line.add(currentRoom);
            currentRoom = nextRoom;
            player.set_Room(currentRoom);
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * 执行Quit指令，用户退出游戏。如果用户在命令中输入了其他参数，则进一步询问用户是否真的退出.
     * @return 如果游戏需要退出则返回true，否则返回false.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * 执行back指令，能够回到来之前的房间，多次执行能够一直返回，知道来到最开始的房间
     * 并且打印他已经在最开始的房间
     */
    private void back_Room()
    {
        if(back_line.size() == 0) {
            System.out.println("You will be at the starting Room");
        }
        else {
            currentRoom = back_line.get(back_line.size() - 1);
            back_line.remove(back_line.size() - 1);
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * 执行look指令，能够查看该房间的所有物品及其重量
     */
    private void look_Room()
    {
        System.out.println(currentRoom.show_items());
    }

    /**
     * 执行items指令，显示房间和玩家的物品和物品的重量
     * 并且显示玩家剩余的负重能力
     */
    private void look_items()
    {
        System.out.println("room: " + currentRoom.getShortDescription());
        System.out.println(currentRoom.show_items());
        System.out.print("player ");
        System.out.println(player.show_all_items());
    }

    /**
     * 执行take指令，当该房间存在该物品，判断玩家的负重能力是否大于该物品重量，大于，玩家则捡起，并提示成功
     * 否则根据错误信息打印 负重上限或者没有该物品等
     */
    private void take_items(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("take what?");
            return;
        }
        String word = command.getSecondWord();
        String item = currentRoom.get_item(word);
        if(item == null)
        {
            System.out.println("There is no " + word + "!");
        }
        else{
            int weight = currentRoom.get_item_weight(item);
            if(weight > player.remain_support())
            {
                System.out.println("Item weight up to upper limit！");
            }else {
                currentRoom.take_item(item);
                player.take_item(item, weight);
                System.out.println("take successful！");
            }
        }

    }

    /**
     * 执行drop指令，玩家身上有该物品，玩家仍掉，并提示成功
     * 否则根据错误信息打印 扔什么？或者没有该物品等
     */
    private void drop_items(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("drop what?");
            return;
        }
        String word = command.getSecondWord();
        String item = player.get_item(word);
        if(item == null)
        {
            System.out.println("players don't have " + word + "!");
        }
        else{
            int weight = player.get_item_weight(item);
            currentRoom.set_items(item, weight);
            player.drop_item(item);
            System.out.println("drop successful！");
        }
    }

    /**
     * 执行eat指令，当该物品为cookie时，判断玩家身上是否有该物品，有则吃点，并该出提示
     * 否则根据错误信息打印 该物品不能吃或者玩家没有cookie物品
     * 当吃完cookie后，该物品会随机继续出现在一个房间里
     */
    private void eat(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("eat what?");
            return;
        }
        String word = command.getSecondWord();

        if(word.equals("cookie"))
        {
            String item = player.get_item(word);
            if(item == null)
            {
                System.out.println("You don't have cookie!");
            }
            else{
                player.eat_cookie();
                System.out.println("eat successful！\nPlayer: " + player.return_name() + " load capacity increased by 10");
                int x = r.nextInt(5) + 1;
                Room temporary_room = Rooms.get(x);
                temporary_room.set_items("cookie", 0);
            }
        }else
            System.out.println(word + " can't eat");

    }
}