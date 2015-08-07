package com.team1.valueupapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import android.widget.Switch;

import android.widget.Toast;

import com.parse.ParseUser;

/**
 * Created by songmho on 2015-07-04.
 */
public class SetupFragment extends Fragment {
    FrameLayout setup_container;
    LinearLayout logout;
    Switch swc_login;
    Switch swc_teambuild;
    Switch swc_notice;
    Button btn_aboutpage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setup_container = (FrameLayout) inflater.inflate(R.layout.fragment_setup, container, false);
        logout = (LinearLayout) setup_container.findViewById(R.id.logout);
        swc_login = (Switch) setup_container.findViewById(R.id.swc_login);
        swc_teambuild = (Switch) setup_container.findViewById(R.id.swc_teambuild);
        swc_notice = (Switch) setup_container.findViewById(R.id.swc_notice);
        btn_aboutpage = (Button) setup_container.findViewById(R.id.btn_aboutpage);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    Toast.makeText(getActivity().getApplicationContext(), "로그아웃되었습니다", Toast.LENGTH_SHORT).show();
                    currentUser.logOut();
                    startActivity(new Intent(getActivity(), SplashActivity.class));
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "로그인정보를 확인하세요", Toast.LENGTH_SHORT).show();
                }//end else
            }
        });//OnClickListener

        swc_login.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {

                if (isChecking)
                    Toast.makeText(getActivity(), "자동로그인 설정", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "자동로그인 해제", Toast.LENGTH_SHORT).show();
            }
        });

        swc_teambuild.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {

                if (isChecking)
                    Toast.makeText(getActivity(), "팀빌딩알림 설정", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "팀빌딩알림 해제", Toast.LENGTH_SHORT).show();
            }
        });

        swc_notice.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {

                if (isChecking)
                    Toast.makeText(getActivity(), "교육일정알림 설정", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "교육일정알림 해제", Toast.LENGTH_SHORT).show();
            }
        });

        btn_aboutpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://m.naver.com"));
                startActivity(intent);
            }
        });

        return setup_container;
    }

}//class
