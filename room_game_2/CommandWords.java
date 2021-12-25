package cn.edu.whut.sept.zuul;

public class CommandWords
{
    private static final String[] validCommands = {
            "go", "quit", "help", "back", "look", "items", "take", "drop", "eat"
    };

    public CommandWords()
    {
        // nothing to do at the moment...
    }

    /**
     *判断该传入的这令是否为有效指令
     * @param aString 待查找的指令
     * @return  返回该命令为有效指令，则返回true，否者返回false
     */
    public boolean isCommand(String aString)
    {
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }
        return false;
    }

    /**
     * 显示所有的有效命令
     */
    public void showAll()
    {
        for(String command: validCommands) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
}
