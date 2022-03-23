package com.cs310.covider.database;

import com.cs310.covider.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

public class Database {
    public static User getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
        {
            return null;
        }
        return new User(User.UserType.STUDENT, new ArrayList<>(),"apple",new Date(), user.getEmail());
    }
}
