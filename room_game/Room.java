package cn.edu.whut.sept.zuul;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Room
{
    private String description;
    private HashMap<String, Room> exits;
    private HashMap<String, Integer> items;

    public Room(String description)
    {
        this.description = description;
        exits = new HashMap<>();
        items = new HashMap<>();
    }

    /**
     * 设置房间的其他方向有哪些房间
     * @param direction 方向
     * @param neighbor 房间名
     */
    public void setExit(String direction, Room neighbor)
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return 返回该房间的描述
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * @return 返回房间描述和哪些方向还有房间的字符串
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * 按照一定格式将物品名称和重量保存在字符串里
     * @return 返回所有物品的字符串
     */
    public String show_items()
    {
        String return_String = "items:";
        return_String += "\n";
        for(Map.Entry<String, Integer> item : items.entrySet())
        {
            return_String += "item: " + item.getKey() + "  weight: " + item.getValue() + "\n";
        }
        return return_String;
    }

    /**
     * @return 返回所有物品名称，重量的字符串
     */

    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * @param direction 方向
     * @return 返回该方向的下一个房间名称
     */
    public Room getExit(String direction)
    {
        return exits.get(direction);
    }

}


