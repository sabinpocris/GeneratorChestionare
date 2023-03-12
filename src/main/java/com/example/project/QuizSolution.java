package com.example.project;

import java.io.Serializable;

public class QuizSolution implements Serializable {
    private static final long serialVersionUID = 27L;
    String quizName;
    int quizID;
    int score;

    public QuizSolution(String quizName, int quizID, int score) {
        this.quizName = quizName;
        this.quizID = quizID;
        this.score = score;
    }

    public String getQuizName() {
        return quizName;
    }

    public int getQuizID() {
        return quizID;
    }

    public int getScore() {
        return score;
    }


    public String toString(int index) {
        return "{\"quiz-id\" : \"" + this.quizID
                + "\", \"quiz-name\" : \"" + this.quizName
                + "\", \"score\" : \"" + this.score
                +"\", \"index_in_list\" : \"" + (index + 1) + "\"}";
    }
}
