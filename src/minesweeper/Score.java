package minesweeper;

/**
 * This is the score model which holds a name and the time of the player
 */
public class Score {

    private String name;
    private int time;

    public void setName(String name){
        this.name=name;
    }

    public void setTime(int time){
        this.time=time;
    }

    public String getName(){
        return name;
    }

    public int getTime(){
        return time;
    }
}
