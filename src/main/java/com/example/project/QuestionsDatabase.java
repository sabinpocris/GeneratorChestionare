package com.example.project;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionsDatabase extends Database implements Serializable {
    private static final long serialVersionUID = 88L;
    private ArrayList<Question> questions;

    public QuestionsDatabase() {
        questions = new ArrayList<>();
        factory = new Factory(Loader.loadAuthenticator());
    }

    public boolean add(Question question) {
        if (question == null) {
            return false;
        }

        // verify if the question we want to add already exists
        for (Question q : questions) {
            if (q.getText().equals(question.getText())) {
                System.out.println("{ 'status' : 'error', 'message' : 'Question already exists'}");
                return false;
            }
        }

        questions.add(question);
        System.out.println("{ 'status' : 'ok', 'message' : 'Question added successfully'}");

        return true;
    }

    public boolean add(String[] args) {
        if (!args[0].equals("-create-question") || !loginSuccessful(args))
            return false;


        return add(factory.createQuestion(args, getQuestionsText()));
    }

    private ArrayList<String> getQuestionsText() {
        ArrayList<String> tempArray = new ArrayList<>(5);

        for (var q : questions) {
            tempArray.add(q.getText());
        }

        return tempArray;
    }

    @Override
    public String get(String[] args) {
        if (!loginSuccessful(args))
            return null;

        switch (args[0]) {
            case "-get-question-id-by-text":
                return getQuestionID(Parser.getStringBetweenApostrophes(args[3]));
            case "-get-all-questions":
                return getAllQuestions();
            default:
                return null;
        }
    }

    private String getQuestionID(String text) {
        for (var q : questions) {
            if (q.getText().equals(text)) {
                return "{ 'status' : 'ok', 'message' : '" + Integer.toString(q.getID()) + "'}";
            }
        }

        return "{ 'status' : 'error', 'message' : 'Question does not exist'}";
    }

    private String getAllQuestions() {
        if (questions.size() == 0)
            return null;

        String openingStr = "{ 'status' : 'ok', 'message' : '[";
        String endingStr = "]'}";

        if (questions.size() == 1) {
            return openingStr + questions.get(0).toStringMinimal() + endingStr;
        }

        openingStr += questions.get(0).toStringMinimal();

        for (int i = 1; i < questions.size(); i++) {
            openingStr += ", " + questions.get(i).toStringMinimal();
        }

        return openingStr + endingStr;
    }

    public Question getQuestionByID(String id) {
        for (Question q : questions) {
            if (q.getID() == Integer.parseInt(id)) {
                return q;
            }
        }

        return null;
    }

    public boolean delete(String[] args) {
        return false;
    }

    public boolean update(String[] args) {
        return false;
    }
}
