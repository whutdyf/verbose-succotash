package cn.edu.whut.sept.zuul;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private String name;
    private String gender = "";
    private String ID = "";
    private String current_room;
    private int bear;
    private HashMap<String, Integer> items;

    public Player(String name, int bear, Room current_room) {
        items = new HashMap<>();
        this.name = name;
        this.bear = bear;
        this.current_room = current_room.getShortDescription();
    }

    /**
     * @return 返回玩家还能承受的重量
     */
    public int remain_support()
    {
        return bear;
    }

    /**
     * 设置玩家当前所在房间
     * @param current_room 玩家当前所在房间
     */
    public void set_Room(Room current_room)
    {
        this.current_room = current_room.getShortDescription();
    }

    /**
     * @return 返回玩家的姓名
     */
    public String return_name()
    {
        return name;
    }

    /**
     * @return 返回玩家的当前房间
     */
    public String Current_room(){
        return current_room;
    }

    /**
     * 赋值玩家捡起的物体名称和重量
     * @param item 物体的名称
     * @param weight 物体的重量
     */
    public void take_item(String item, int weight)
    {
        bear = bear - weight;
        items.put(item, weight);
    }

    /**
     * 判断玩家是否有这件物品，
     * @param item 需要判断的物品名称
     * @return 有该物品时返回该物品的名字，没有则返回null
     */
    public String get_item(String item)
    {
        if(items.get(item) != null)
        {
            return item;
        }
        else
            return null;
    }

    /**
     * 玩家扔掉物品
     * @param item 玩家需要扔掉物品的名称
     */
    public void drop_item(String item)
    {
        int weight = items.get(item);
        bear = bear + weight;
        items.remove(item);
    }

    /**
     * 玩家吃掉cookie
     */
    public void eat_cookie()
    {
        bear += 10;
        items.remove("cookie");
    }

    /**
     * @param item 带查询物品的名称
     * @return 返回该物品的重量
     */
    public int get_item_weight(String item)
    {
        return items.get(item);
    }

    /**
     * 将所有物品和玩家剩余负重按照一定格式组成字符串
     * @return 返回需要打印的字符串
     */
    public String show_all_items()
    {
        String return_String = "name: " + name + "\n";
        for(Map.Entry<String, Integer> item : items.entrySet())
        {
            return_String += "item: " + item.getKey() + "  weight: " + item.getValue() + "\n";
        }
        return_String += "The weight the player can still carry: " + bear + "\n";

        return return_String;
    }

}
