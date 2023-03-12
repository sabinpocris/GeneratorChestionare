package com.example.project;

import java.io.*;

public class Loader {
    public static final File dataFolder = new File("data/");
    public static final File usersDatabase = new File("data/usersDB");
    public static final File questionsDatabase = new File("data/questionsDB");
    public static final File quizDatabase = new File("data/quizDB");
    public static final File authenticatorFile = new File("data/authenticator");

    private static Object loadObject(File file) {
        Object obj;

        // if folder/file does not exist
        if (!dataFolder.isDirectory() || !file.exists())
            return null;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            obj = objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return obj;
    }

    private static boolean saveObject(Object obj, File path) {
        // create the folder that will store the database if it does not exist
        if (!dataFolder.isDirectory() && !dataFolder.mkdir()) {
            System.out.println("Error: can't create the data folder!");
            return false;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(obj);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean saveObject(Object obj) {
        if (obj instanceof UserDatabase)
            return saveUsersDatabase((UserDatabase) obj);

        if (obj instanceof QuestionsDatabase)
            return saveQuestionsDatabase((QuestionsDatabase) obj);

        if (obj instanceof QuizDatabase)
            return saveQuizDatabase((QuizDatabase) obj);

        return false;
    }


    public static UserDatabase loadUsersDatabase() {
        UserDatabase db = (UserDatabase) loadObject(usersDatabase);

        // database does not exist
        if (db == null) {
            db = new UserDatabase();
        }

        return db;
    }

    public static QuestionsDatabase loadQuestionsDatabase() {
        QuestionsDatabase db = (QuestionsDatabase) loadObject(questionsDatabase);

        // database does not exist
        if (db == null) {
            db = new QuestionsDatabase();
        }

        // we need to check if there is an authenticator saved, so we can load a newer version
        if (authenticatorFile.exists()) {
            Authenticator tempAuthenticator = (Authenticator) loadObject(authenticatorFile);

            if (tempAuthenticator != null) {
                db.factory.authenticator = tempAuthenticator;
            }
        }

        return db;
    }

    public static QuizDatabase loadQuizDatabase() {
        QuizDatabase db = (QuizDatabase) loadObject(quizDatabase);

        // database does not exist
        if (db == null) {
            db = new QuizDatabase();
        }

        // we need to check if there is an authenticator saved, so we can load a newer version
        if (authenticatorFile.exists()) {
            Authenticator tempAuthenticator = (Authenticator) loadObject(authenticatorFile);

            if (tempAuthenticator != null) {
                db.factory.authenticator = tempAuthenticator;
            }
        }

        return db;
    }

    public static boolean saveUsersDatabase(UserDatabase db) {
        return saveObject(db, usersDatabase) & saveObject(db.factory.authenticator, authenticatorFile);
    }

    public static boolean saveQuestionsDatabase(QuestionsDatabase db) {
        return saveObject(db, questionsDatabase);
    }

    public static boolean saveQuizDatabase(QuizDatabase db) {
        return saveObject(db, quizDatabase);
    }

    public static Authenticator loadAuthenticator() {
        Authenticator authenticator = (Authenticator) loadObject(authenticatorFile);

        // database does not exist
        if (authenticator == null) {
            authenticator = new Authenticator();
        }

        return authenticator;
    }

    public static boolean saveAuthenticator(Authenticator authenticator) {
        return saveObject(authenticator, authenticatorFile);
    }
}
