package minesweeper;

/**
 * This class converts milliseconds to seconds using the calculateTime method
 */
public class TimeConverter
{
    /**
     * @param time in ms
     * @return ms to sec in string value
     */
    static String calculateTime(long time)
    {
        int ms = (int) time;
        int sec = ms / 1000;
                    return String.valueOf(sec);
    }
}
