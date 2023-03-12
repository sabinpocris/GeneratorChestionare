package com.example.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Authenticator implements Serializable {
    // here I store every (username, password)
    HashMap<String, String> users;

    private static final long serialVersionUID = 222L;

    Authenticator() {
        users = new HashMap<>();
    }

    public static enum AuthenticatorStatus {
        OK, NO_USERNAME, NO_PASSWORD, WRONG_PASSWORD, NO_USER, REFUSED, NO_CREDENTIALS
    }

    // verifies user credentials and that user is in the database
    public AuthenticatorStatus checkUser(ArrayList<String> credentials) {
        if (credentials.get(0) == null && credentials.get(1) == null) {
            return AuthenticatorStatus.NO_CREDENTIALS;
        }

        if (credentials.get(0) == null) {
            return AuthenticatorStatus.NO_USERNAME;
        }

        if (credentials.get(1) == null) {
            return AuthenticatorStatus.NO_PASSWORD;
        }

        if (users.get(credentials.get(0)) == null)
            return AuthenticatorStatus.NO_USER;

        return AuthenticatorStatus.OK;
    }
    
    // verifies that user exists and password is good
    public AuthenticatorStatus login(ArrayList<String> credentials) {
        AuthenticatorStatus temporaryStatus = checkUser(credentials);

        if (temporaryStatus != AuthenticatorStatus.OK)
            return temporaryStatus;

        // verify the password
        if (users.get(credentials.get(0)).equals(credentials.get(1)))
            return AuthenticatorStatus.OK;
        
        return AuthenticatorStatus.REFUSED;
    }
    
    // adds user to the database
    public AuthenticatorStatus addUser(ArrayList<String> credentials) {
        if (checkUser(credentials) != AuthenticatorStatus.NO_USER)
            return AuthenticatorStatus.REFUSED;
        
        users.put(credentials.get(0), credentials.get(1));
        
        return AuthenticatorStatus.OK;
    }
}
