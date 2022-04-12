package minesweeper;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * This is the DAO class that writes on our database(JSON) our high scores.
 */
public class ScoreDao {
    List<Score> scores;

    public ScoreDao(List<Score> scores) {

        this.scores = scores;

    }

    public void setNewScore(Score score, int index) throws IOException {
        this.scores.set(index, score);
        save();
    }

    public void save() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter file = new FileWriter("src\\scores.json");
            gson.toJson(scores, file);
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.println("Problem in save function");
        }
    }
}

