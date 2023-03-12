package com.example.project;

import java.io.File;

public class Cleaner {
    public static void deleteFolder(File folder) {
        if (folder == null)
            return;

        if (!folder.isDirectory())
            return;

        String[] files = folder.list();

        if (files == null) {
            if (folder.delete())
                System.out.println("{ 'status' : 'ok', 'message' : 'Cleanup finished successfully'}");

            return;
        }

        for (var file : files) {
            if (!deleteFile(new File(folder.getPath(), file)))
                System.out.println("{ 'status' : 'error', 'message' : 'Cleanup failed.'}");
        }

        if (folder.delete())
            System.out.println("{ 'status' : 'ok', 'message' : 'Cleanup finished successfully'}");
    }

    public static boolean deleteFile(File file) {
        return file.delete();
    }

    public static void resetStaticFields() {
        Question.questionCount = 0;
        Quiz.quizCount = 0;
        Answer.countAnswers = 0;
    }
}
