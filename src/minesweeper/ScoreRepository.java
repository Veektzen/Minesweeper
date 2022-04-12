package minesweeper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the repository class for the scores. This class reads from json and makes an array list with high score elements
 */
public class ScoreRepository {
    public List<Score> scores = new ArrayList<Score>();
    private ScoreDao dao;

    public ScoreRepository(){
        dao = new ScoreDao(scores);
    }

    /**
     * when we call this method, the scores array list gets cleared and then reloads the json elements to this array list from json
     * @return the scores array list
     */
    public List<Score> readJson(){
        scores.clear();
        try{
            JSONArray jsonArray = new JSONArray(new JSONTokener(new FileReader("src\\scores.json")));
            for (int i=0 ; i< 3; i++){
                scores.add(new Score());
                scores.get(i).setTime(jsonArray.getJSONObject(i).getInt("time"));
                scores.get(i).setName(jsonArray.getJSONObject(i).getString("name"));
            }
        }catch(FileNotFoundException e){
            System.out.println("JSON File not found in readJson");
        } catch (JSONException e) {
            System.out.println("JSON Exception in readJson");
        }
        return scores;
    }
}
