package com.example.project;

import java.util.ArrayList;

public class Utility {
    public static String generateOutputString(ArrayList<String> contents) {
        String openingStr = "{ 'status' : 'ok', 'message' : '[";
        String endingStr = "]'}";

        if (contents.size() == 0) {
            return null;
        }

        openingStr += contents.get(0);

        if (contents.size() == 1) {
            return openingStr + endingStr;
        }

        for (int i = 1; i < contents.size(); i++) {
            openingStr += ", " + contents.get(i);
        }

        return openingStr + endingStr;
    }
}
