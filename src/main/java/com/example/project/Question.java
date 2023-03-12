package com.example.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Question implements Serializable {
    private static final long serialVersionUID = 588L;
    protected static int questionCount = 0;

    private int id;
    private String text;
    private ArrayList<Answer> answers;
    private QuestionType type;

    public enum QuestionType {
        SINGLE, MULTIPLE;
    }

    Question() {
        this(null, QuestionType.SINGLE);
    }

    Question(String text, QuestionType type) {
        this.text = text;
        id = ++Question.questionCount;
        answers = new ArrayList<>();
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public int getID() {
        return this.id;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public String toStringMinimal() {
        return "{\"question_id\" : \"" + this.id + "\", \"question_name\" : \"" + this.text + "\"}";
    }

    // a more complete version of toStringMinimal()
    public String toString(int index) {
        String result = "{\"question-name\":\"" + this.text
                + "\", \"question_index\":\"" + (index + 1)
                + "\", \"question_type\":\"" + getQuestionTypeToString() + "\", \"answers\":\"[";

        if (answers.size() == 0) {
            return result + "]\"}";
        }

        result += answers.get(0).toString();

        if (answers.size() == 1) {
            return result+ "]\"}";
        }

        for (int i = 1; i < answers.size(); i++) {
            result += ", " + answers.get(i).toString();
        }

        return result + "]\"}";
    }

    public String getQuestionTypeToString() {
        switch (type) {
            case SINGLE:
                return "single";
            case MULTIPLE:
                return "multiple";
            default:
                return null;
        }
    }

    public HashMap<Integer, Double> getHashMapOfAnswers() {
        // usign the id we can get the value of the answer, if it exists
        HashMap<Integer, Double> answersScore = new HashMap<>();
        int countCorrectAnswers = 0;
        int countWrongAnswers = 0;
        int countTotalAnswers = answers.size();

        for (Answer answer : answers) {
            if (answer.isCorrect())
                countCorrectAnswers++;
            else
                countWrongAnswers++;
        }

        double correctAnswerValue = 1.0 / (double) countCorrectAnswers;
        double wrongAnswerValue = 1.0 / (double) countWrongAnswers;
        wrongAnswerValue *= (-1.0);

        for (Answer answer : answers) {
            int answerID = answer.getId();
            double score  = answer.isCorrect() ? correctAnswerValue : wrongAnswerValue;

            answersScore.put(answerID, score);
        }

        return answersScore;
    }
}
