package model;

import java.util.List;

public class Question {
    private int id;
    private String difficulty;

    private String text;
    private List<String> options;
    private int correctIndex;

     public Question(int id, String text, List<String> options, int correctIndex, String difficulty) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.difficulty = difficulty;
    }

     public Question(String text,
                    List<String> options,
                    int correctIndex) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
