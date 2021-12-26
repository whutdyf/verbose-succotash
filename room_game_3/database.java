package cn.edu.whut.sept.zuul;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

public class database {

    private Properties pro;
    private Connection connection;

    public database(){
        pro = new Properties();
        try{
            pro.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行数据库的链接
     */
    public void get_con() {
        try{
            Class.forName(pro.getProperty("MySqlDriver"));
            connection = DriverManager.getConnection(pro.getProperty("url"), pro.getProperty("user"), pro.getProperty("password"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库中读取所有房间的物品
     * @param hashMap 保存了所有的房间
     */
    public void create_room(HashMap<Integer, Room> hashMap) {
        HashMap<Integer, String> has = new HashMap<>();
        has.put(1, "outside");
        has.put(2, "theater");
        has.put(3, "pub");
        has.put(4, "lab");
        has.put(5, "office");
        int i = 1;
        try {
            for(Room room : hashMap.values()) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from " + has.get(i));
                while(resultSet.next()) {
                    String item = resultSet.getString(1);
                    int weight = resultSet.getInt(2);
                    room.set_items(item, weight);
                }
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行玩家的创建并且保存到数据库
     * @param player 创建的玩家
     */
    public void create(Player player) {
        try{
            String sql = "insert into player values(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, player.return_name());
            statement.setString(2, player.Current_room());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行玩家信息的更新
     * @param player 正在进行游戏的玩家
     */
    public void save(Player player) {
        try{
            String sql = "update player set current_room=? where name=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, player.Current_room());
            statement.setString(2, player.return_name());
            statement.executeUpdate();
            System.out.println("保存成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据库连接的关闭
     */
    public void close() {
        try{
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
