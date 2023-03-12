package com.example.project;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizDatabase extends Database implements Serializable {
    private static final long serialVersionUID = 953L;
    private ArrayList<Quiz> quizzes;

    public QuizDatabase() {
        quizzes = new ArrayList<>();
        factory = new Factory(Loader.loadAuthenticator());
    }

    public boolean add(String[] args) {
        if (!args[0].equals("-create-quizz") || !loginSuccessful(args))
            return false;

        return add(factory.createQuiz(args, getQuizzesText())); // update this with factory
    }

    public boolean add(Quiz quiz) {
        if (quiz == null) {
            return false;
        }

        quizzes.add(quiz);
        System.out.println("{ 'status' : 'ok', 'message' : 'Quizz added succesfully'}");

        return true;
    }


    public String get(String[] args) {
        if (!loginSuccessful(args))
            return null;

        switch (args[0]) {
            case "-get-quizz-by-name":
                return getQuizByName(args);
            case "-get-all-quizzes":
                return getAllQuizzes(args);
            case "-get-quizz-details-by-id":
                return getQuizDetailsByID(args);
            default:
                return null;
        }
    }

    private ArrayList<String> getQuizzesText() {
        ArrayList<String> tempArray = new ArrayList<>();

        for (Quiz q : quizzes) {
            tempArray.add(q.getText());
        }

        return tempArray;
    }

    private Quiz getQuiz(String text) {
        for (Quiz q : quizzes) {
            if (q.getText().equals(text))
                return q;
        }

        return null;
    }

    private Quiz getQuizByID(int id) {
        for (Quiz q : quizzes) {
            if (q.getId() == id)
                return q;
        }

        return null;
    }

    private String getQuizByName(String[] args) {
        if (args.length <= 3)
            return null;

        String quizText = Parser.getStringBetweenApostrophes(args[3]);
        Quiz tempQuiz = getQuiz(quizText);

        if (tempQuiz == null) {
            return "{ 'status' : 'error', 'message' : 'Quizz does not exist'}";
        }

        return "{ 'status' : 'ok', 'message' : '" + tempQuiz.getId() + "'}";
    }

    private String getAllQuizzes(String[] args) {
        ArrayList<String> quizzesToString = new ArrayList<>();

        for (Quiz q : quizzes) {
            quizzesToString.add(q.toString());
        }

        return Utility.generateOutputString(quizzesToString);
    }

    private String getQuizDetailsByID(String[] args) {
        Quiz tempQuiz = getQuizByID(Integer.parseInt(Parser.getStringBetweenApostrophes(args[3])));
        ArrayList<String> quizDetails = new ArrayList<>();

        if (tempQuiz == null) {
            return null;
        }

        ArrayList<Question> questions = tempQuiz.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            quizDetails.add(questions.get(i).toString(i));
        }

        return Utility.generateOutputString(quizDetails);
    }

    public boolean delete(String[] args) {
        if (!loginSuccessful(args))
            return false;

        if (!args[0].equals("-delete-quizz-by-id"))
            return false;

        if (args.length <= 3 || !args[3].startsWith("-id")) {
            System.out.println("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
            return false;
        }

        if (!deleteQuiz(Integer.parseInt(Parser.getStringBetweenApostrophes(args[3])))) {
            System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
            return false;
        }

        System.out.println("{ 'status' : 'ok', 'message' : 'Quizz deleted successfully'}");
        return true;
    }

    public boolean deleteQuiz(int id) {
        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).getId() == id) {
                quizzes.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean update(String[] args) {
        if (!loginSuccessful(args))
            return false;

        String username = Parser.getStringBetweenApostrophes(args[1]);

        if (args.length <= 3 || !args[3].startsWith("-quiz-id")) {
            System.out.println("{'status':'error','message':'No quizz identifier was provided'}");
            return false;
        }

        int quizID = Integer.parseInt(Parser.getStringBetweenApostrophes(args[3]));
        Quiz quiz = getQuizByID(quizID);

        if (quiz == null) {
            System.out.println("{'status':'error','message':'No quiz was found'}");
            return false;
        }

        if (quiz.getOwnerUsername().equals(username)) {
            System.out.println("{ 'status' : 'error', 'message' : 'You cannot answer your own quizz'}");
            return false;
        }

        for (String user :  quiz.getUsersThatSolvedIt()) {
            if (user.equals(username)) {
                System.out.println("{ 'status' : 'error', 'message' : 'You already submitted this quizz'}");
                return false;
            }
        }

        ArrayList<Integer> answerIDs = Parser.getAnswerIdsForQuiz(args);

        int score = quiz.resolve(answerIDs);

        quiz.addUserThatSolvedIt(username);

        if (!Loader.usersDatabase.exists()) {
            System.out.println("ERROR: The user database is not saved");
            return false;
        }

        // load the user database
        UserDatabase userDatabase = Loader.loadUsersDatabase();
        userDatabase.addSolution(quizID, quiz.getText(), score);

        // save the modified user database
        Loader.saveUsersDatabase(userDatabase);


        System.out.println("{'status':'ok','message':'" + score + " points'}");

        return false;
    }
}
