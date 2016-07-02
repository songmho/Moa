package com.team1.valueupapp.utility;

/**
 * Created by knulps on 16. 7. 2..
 */
public class Utility {
    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
