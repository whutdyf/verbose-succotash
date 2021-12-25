package cn.edu.whut.sept.zuul;

public class Command
{
    private String commandWord;
    private String secondWord;

    public Command(String firstWord, String secondWord)
    {
        commandWord = firstWord;
        this.secondWord = secondWord;
    }

    /**
     * @return 返回第一个指令单词
     */
    public String getCommandWord()
    {
        return commandWord;
    }

    /**
     * @return 返回第二个指令单词
     */
    public String getSecondWord()
    {
        return secondWord;
    }

    /**
     * @return 返回第一个指令单词是否为null
     */
    public boolean isUnknown()
    {
        return (commandWord == null);
    }

    /**
     * @return 返回是否有第二指令单词
     */
    public boolean hasSecondWord()
    {
        return (secondWord != null);
    }
}
