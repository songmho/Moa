package com.team1.valueupapp.utility;

/**
 * 유틸리티 모음
 * Created by knulps on 16. 7. 2.
 */
public class Utility {

    //유효한 이메일 폼인지 패턴 체크
    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
