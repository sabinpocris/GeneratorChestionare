package com.example.project;

public class QuizGenerator {
    private String[] args;
    Database database;

    public QuizGenerator(String[] args) {
        this.args = args;

        switch (Parser.getDatabaseType(args)) {
            case QUIZ:
                this.database = Loader.loadQuizDatabase();
                break;
            case USER:
                this.database = Loader.loadUsersDatabase();
                break;
            case QUESTION:
                this.database = Loader.loadQuestionsDatabase();
                break;
            default:
                this.database = null;
                break;
        }
    }

    public void start() {
        if (args[0].equals("-cleanup-all")) {
            Cleaner.deleteFolder(Loader.dataFolder);
            Cleaner.resetStaticFields();
            return;
        }

        switch (Parser.getActionType(args)) {
            case ADD:
                // there is no need to overwrite if nothing was added
                if (!database.add(args))
                    return;
                exit();
                break;
            case GET:
                String result = database.get(args);

                if (result != null) {
                    System.out.println(result);
                }
                break;
            case DELETE:
                if (!database.delete(args))
                    return;

                exit();
                break;
            case UPDATE:
                database.update(args);

                break;
            default:
                return;
        }
    }

    private void exit() {
        if (!Loader.saveObject(database)) {
            System.out.println("ERROR: Couldn't save the database!");
        }
    }
}
