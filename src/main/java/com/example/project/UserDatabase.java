package com.example.project;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDatabase extends Database implements Serializable {
    private static final long serialVersionUID = 111L;
    private ArrayList<User> users;
    private ArrayList<QuizSolution> solutions;

    public UserDatabase() {
        users = new ArrayList<>();
        factory = new Factory(Loader.loadAuthenticator());
        solutions = new ArrayList<>();
    }


    private boolean add(User user) {
        if (user == null)
            return false;

        users.add(user);
        System.out.println("{ 'status' : 'ok', 'message' : 'User created successfully'}");

        return true;
    }

    public boolean loginSuccessful(String[] args) {
        ArrayList<String> credentials = Parser.getUserCredentials(args);
        switch (factory.authenticator.checkUser(credentials)) {
            case NO_USERNAME:
            case NO_CREDENTIALS:
                System.out.println("{ 'status' : 'error', 'message' : 'Please provide username'}");
                return false;
            case NO_PASSWORD:
                System.out.println("{ 'status' : 'error', 'message' : 'Please provide password'}");
                return false;
            case OK:
                System.out.println("{ 'status' : 'error', 'message' : 'User already exists'}");
                return false;
            case NO_USER:
                return true;
            default:
                return false;
        }
    }

    public boolean add(String[] args) {
        if (!args[0].equals("-create-user"))
            return false;

        if (loginSuccessful(args)) {
            return add(factory.createUser(Parser.getUserCredentials(args)));
        }

        return false;
    }

    public String get(String[] args) {
        if(!super.loginSuccessful(args)) {
            return null;
        }

        if(!args[0].startsWith("-get-my-solutions"))
            return null;

        return getMySolutions(args);
    }

    public boolean delete(String[] args) {
        return false;
    }

    public boolean update(String[] args) {
        return false;
    }

    public boolean addSolution(int quizID, String quizName, int score) {
        return solutions.add(new QuizSolution(quizName, quizID, score));
    }


    private String getMySolutions(String[] args) {
        ArrayList<String> solutionsToString = new ArrayList<>();

        for (int i = 0; i < solutions.size(); i++) {
            solutionsToString.add(solutions.get(i).toString(i));
        }

        return Utility.generateOutputString(solutionsToString);
    }

}
