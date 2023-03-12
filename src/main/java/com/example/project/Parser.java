package com.example.project;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    enum DatabaseType {
        USER, QUESTION, QUIZ, UNDEFINED
    }

    enum ActionType {
        ADD, GET, DELETE, UPDATE, OTHER
    }

    public static ArrayList<String> getUserCredentials(String[] args) {
        // user and password
        ArrayList<String> credentials = new ArrayList<>(Arrays.asList((String) null, (String) null));

        // args is <= 1 only if we have only one parameter or less
        if (args.length <= 1) {
            return credentials;
        }

        for (int i = 1; i < args.length && i <= 2; i++) {
            switch (args[i].substring(0, 2)) {
                // username index: 0
                case "-u":
                    credentials.set(0, args[i].substring(args[i].indexOf("'") + 1, args[i].lastIndexOf("'")));
                    break;
                // password index: 1
                case "-p":
                    credentials.set(1, args[i].substring(args[i].indexOf("'") + 1, args[i].lastIndexOf("'")));
                    break;
            }
        }

        return credentials;
    }

    public static DatabaseType getDatabaseType(String[] args) {
        switch (args[0]) {
            case "-create-user":
            case "-get-my-solutions":
                return DatabaseType.USER;
            case "-create-question":
            case "-get-question-id-by-text":
            case "-get-all-questions":
                return DatabaseType.QUESTION;
            case "-create-quizz":
            case "-get-quizz-by-name":
            case "-get-all-quizzes":
            case "-get-quizz-details-by-id":
            case "-submit-quizz":
            case "-delete-quizz-by-id":
                return DatabaseType.QUIZ;
            default:
                return DatabaseType.UNDEFINED;
        }
    }

    public static ActionType getActionType(String[] args) {
        switch (args[0]) {
            case "-create-user":
            case "-create-quizz":
            case "-create-question":
                return ActionType.ADD;
            case "-get-my-solutions":
            case "-get-question-id-by-text":
            case "-get-all-questions":
            case "-get-quizz-by-name":
            case "-get-all-quizzes":
            case "-get-quizz-details-by-id":
                return ActionType.GET;
            case "-delete-quizz-by-id":
                return ActionType.DELETE;
            case "-submit-quizz":
                return ActionType.UPDATE;
            default:
                return ActionType.OTHER;
        }
    }

    public static String getQuestionText(String[] args) {
        if (args == null)
            return null;

        try {
            if (!args[3].startsWith("-text")) {
                System.out.println("{ 'status' : 'error', 'message' : 'No question text provided'}");
                return null;
            }

            return args[3].substring(args[3].indexOf("'") + 1, args[3].lastIndexOf("'"));
        } catch (Exception e) {
            System.out.println("{ 'status' : 'error', 'message' : 'No question text provided'}");
            return null;
        }
    }

    // an array of the pair: answer text, correct: yes/no
    public static ArrayList<ArrayList<String>> getAnswerPairs(String[] args) {
        ArrayList<ArrayList<String>> answers = new ArrayList<>(5);

        for (int i = 5, j = 1; i < args.length; i += 2, j++) {
            ArrayList<String> tempAnswer = new ArrayList<>(Arrays.asList((String) null, (String) null));

            // get the answer text
            if (args[i].substring(0, 10).equals("-answer-" + j + " ")) {
                tempAnswer.set(0, getStringBetweenApostrophes(args[i]));
            }

            if (i + 1 < args.length && args[i + 1].length() > 20 && args[i + 1].substring(0, 20).equals("-answer-" + j + "-is-correct")) {
                tempAnswer.set(1, getStringBetweenApostrophes(args[i + 1]));
            }

            answers.add(tempAnswer);
        }

        return answers;
    }

    public static String getStringBetweenApostrophes(String str) {
        if (str == null)
            return null;

        return str.substring(str.indexOf("'") + 1, str.lastIndexOf("'"));
    }

    public static String getQuizText(String[] args) {
        if (args == null || args.length <= 3)
            return null;

        return getStringBetweenApostrophes(args[3]);
    }

    public static ArrayList<String> getQuestionIDs(String[] args) {
        ArrayList<String> ids = new ArrayList<>();

        for (int i = 4; i < args.length; i++) {
            ids.add(getStringBetweenApostrophes(args[i]));
        }

        return ids;
    }

    public static ArrayList<Integer> getAnswerIdsForQuiz(String[] args) {
        ArrayList<Integer> answers = new ArrayList<>();

        for (int i = 4; i < args.length; i++) {
            if (!args[i].startsWith("-answer-id"))
                continue;

            answers.add(Integer.parseInt(Parser.getStringBetweenApostrophes(args[i])));
        }

        return answers;
    }
}
