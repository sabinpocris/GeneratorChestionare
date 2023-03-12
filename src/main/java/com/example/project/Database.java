package com.example.project;


import java.io.Serializable;
import java.util.ArrayList;

public abstract class Database implements Serializable {
    Factory factory;
    public abstract boolean add(String[] args);
    public abstract String get(String[] args);
    public abstract boolean delete(String[] args);
    public abstract boolean update(String[] args);

    public boolean loginSuccessful(String[] args) {
        ArrayList<String> credentials = Parser.getUserCredentials(args);
        switch (factory.authenticator.login(credentials)) {
            case NO_USERNAME:
            case NO_PASSWORD:
            case NO_CREDENTIALS:
                System.out.println("{'status':'error','message':'You need to be authenticated'}");
                return false;
            case OK:
                return true;
            default:
                System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
                return false;
        }
    }
}
