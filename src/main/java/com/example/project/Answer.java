package com.example.project;

import java.io.Serializable;

public class Answer implements Serializable {
    private static final long serialVersionUID = 567L;
    public static int countAnswers = 0;

    private int id;
    String text;
    private boolean isCorrect;

    Answer(String text, String isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect.equals("1");
        id = ++countAnswers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return "{\"answer_name\":\"" + text + "\", \"answer_id\":\"" + id + "\"}";
    }
}
