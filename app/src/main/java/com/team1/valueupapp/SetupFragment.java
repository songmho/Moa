package com.team1.valueupapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by songmho on 2015-07-04.
 */
public class SetupFragment extends Fragment {
    LinearLayout cur_container;
    TextView logout;
    ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        cur_container = (LinearLayout) inflater.inflate(R.layout.fragment_setup, container, true);


        logout = (TextView) cur_container.findViewById(R.id.logout);
        progressBar = (ProgressBar) cur_container.findViewById(R.id.progressbar);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);

                        ParseUser.logOut();
                        ParseUser logout = ParseUser.getCurrentUser();

                    }
                }).start();


            }
        });
        return cur_container;
    }

}
