package com.example.project;

import java.io.Serializable;
import java.util.ArrayList;

public class Factory implements Serializable {
    Authenticator authenticator;
    private static final long serialVersionUID = 555L;

    Factory(Authenticator authenticator) {
        this.authenticator = authenticator;
    }


    public User createUser(ArrayList<String> credentials) {
        if (authenticator.login(credentials) != Authenticator.AuthenticatorStatus.NO_USER)
            return null;

        if (authenticator.addUser(credentials) == Authenticator.AuthenticatorStatus.REFUSED)
            return null;

        return new User(credentials.get(0));
    }

    public Question createQuestion(String[] args, ArrayList<String> questionsText) {
        String questionText = Parser.getQuestionText(args);
        int numberOfCorrectAnswers = 0;
        Question.QuestionType questionType = Question.QuestionType.SINGLE;

        if (questionText == null)
            return null;

        // verify if the question has not been added before
        for (var s : questionsText) {
            if (s.equals(questionText)) {
                System.out.println("{ 'status' : 'error', 'message' : 'Question already exists'}");
                return null;
            }
        }

        var answersArray = Parser.getAnswerPairs(args);

        for (int i = 0; i < answersArray.size(); i++) {
            if (answersArray.get(i).get(0) == null) {
                System.out.println("{ 'status' : 'error', 'message' : 'Answer " + (i + 1) + " has no answer description'}");
                return null;
            }

            if (answersArray.get(i).get(1) == null) {
                System.out.println("{ 'status' : 'error', 'message' : 'Answer " + (i + 1) + " has no answer correct flag'}");
                return null;
            }

            if (answersArray.get(i).get(1).equals("1")) {
                numberOfCorrectAnswers++;
            }
        }

        if (answersArray.size() == 0) {
            System.out.println("{ 'status' : 'error', 'message' : 'No answer provided'}");
            return null;
        }

        if (answersArray.size() == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'Only one answer provided'}");
            return null;
        }

        if (answersArray.size() > 5) {
            System.out.println("{ 'status' : 'error', 'message' : 'More than 5 answer were submitted'}");
            return null;
        }


        if (!args[4].equals("-type 'single'"))
            questionType = Question.QuestionType.MULTIPLE;

        if (questionType == Question.QuestionType.SINGLE && numberOfCorrectAnswers != 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'Single correct answer question has more than one correct answer'}");
            return null;
        }


        for (int i = 0; i < answersArray.size(); i++) {
            for (int j = 0; j < answersArray.size(); j++) {
                if (i == j)
                    continue;

                if (answersArray.get(i).get(0).equals(answersArray.get(j).get(0))) {
                    System.out.println("{ 'status' : 'error', 'message' : 'Same answer provided more than once'}");
                    return null;
                }
            }
        }

        return newQuestion(questionText, answersArray, questionType);
    }

    private Question newQuestion(String text, ArrayList<ArrayList<String>> answers, Question.QuestionType type) {
        Question newQuestion = new Question(text, type);

        for (var answerPair : answers) {
            newQuestion.addAnswer(new Answer(answerPair.get(0), answerPair.get(1)));
        }

        return newQuestion;
    }

    public Quiz createQuiz(String[] args, ArrayList<String> quizzesText) {
        String quizText = Parser.getQuizText(args);

        // checking quizz text
        for (String text : quizzesText) {
            if (text.equals(quizText)) {
                System.out.println("{ 'status' : 'error', 'message' : 'Quizz name already exists'}");
                return null;
            }
        }

        if (args.length >= 13) {
            System.out.println("{ 'status' : 'error', 'message' : 'Quizz has more than 10 questions'}");
            return null;
        }

        ArrayList<Question> questions = new ArrayList<>();
        ArrayList<String> questionIDs = Parser.getQuestionIDs(args);

        if (questionIDs.size() == 0)
            return null;

        QuestionsDatabase questionsDatabase = Loader.loadQuestionsDatabase();

        for (String questionID : questionIDs) {
            Question tempQuestion = questionsDatabase.getQuestionByID(questionID);

            if (tempQuestion == null) {
                System.out.println("{ 'status' : 'error', 'message' : 'Question ID for question " + (questions.size() + 1)  + " does not exist'}");
                return null;
            }

            questions.add(tempQuestion);
        }

        return new Quiz(quizText, Parser.getStringBetweenApostrophes(args[1]), questions);
    }
}
