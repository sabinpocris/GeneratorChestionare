package com.example.project;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Tema1 {
    public static void main(final String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("Hello world!");
            return;
        }


        QuizGenerator quizGenerator = new QuizGenerator(args);
        quizGenerator.start();
    }
}
