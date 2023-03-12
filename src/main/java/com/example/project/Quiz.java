package com.example.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Quiz implements Serializable {
    private static final long serialVersionUID = 598L;
    protected static int quizCount = 0;

    private int id;
    private String text;
    private String ownerUsername;
    private ArrayList<Question> questions;
    private boolean isCompleted = false;
    private ArrayList<String> tookByTheseUsers;

    public Quiz(String text, String ownerUsername, ArrayList<Question> questions) {
        id = ++quizCount;
        this.text = text;
        this.ownerUsername = ownerUsername;
        this.questions = questions;
        this.tookByTheseUsers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public String isCompleted() {
        if (!isCompleted)
            return "False";

        return "True";
    }

    public void complete() {
        if (!isCompleted)
            this.isCompleted = true;
    }

    public String toString() {
        return "{\"quizz_id\" : \"" + this.id +
                "\", \"quizz_name\" : \"" + this.text +
                "\", \"is_completed\" : \"" + isCompleted() + "\"}";
    }

    public ArrayList<String> getUsersThatSolvedIt() {
        return tookByTheseUsers;
    }

    public void addUserThatSolvedIt(String username) {
        tookByTheseUsers.add(username);
    }

    public int resolve(ArrayList<Integer> answerIDs) {
        double total = 0.0;
        int indexCurrentAnswer = 0;

        for (Question question : questions) {
            double questionScore = 0;
            HashMap<Integer, Double> answersScore = question.getHashMapOfAnswers();

            for (; indexCurrentAnswer < answerIDs.size(); indexCurrentAnswer++) {
                if (answersScore.containsKey(answerIDs.get(indexCurrentAnswer))) {
                    questionScore += answersScore.get(answerIDs.get(indexCurrentAnswer));
                    continue;
                }
                break;
            }
            total += questionScore;
        }

        total *= (100.0 / questions.size());

        total = total < 0 ? 0 : total;

        return (int) Math.round(total);
    }
}
