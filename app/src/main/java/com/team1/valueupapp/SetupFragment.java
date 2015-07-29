package com.team1.valueupapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * Created by songmho on 2015-07-04.
 */
public class SetupFragment extends Fragment {
    FrameLayout setup_container;
    LinearLayout logout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setup_container = (FrameLayout) inflater.inflate(R.layout.fragment_setup, container, false);
        logout = (LinearLayout) setup_container.findViewById(R.id.logout);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    currentUser.logOut();
                    Toast.makeText(getActivity().getApplicationContext(), "로그아웃되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "로그인정보를 확인하세요", Toast.LENGTH_SHORT).show();
                }//end else
            }
        });//OnClickListener

        return setup_container;
    }

}//class
