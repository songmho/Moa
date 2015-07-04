package com.team1.valueupapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 2015-06-30.
 */
public class ListFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout cur_container;
    int cur_fragment;
    public ListFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cur_container=(LinearLayout)inflater.inflate(R.layout.fragment_list,container,false);
        recyclerView=(RecyclerView)cur_container.findViewById(R.id.recyclerview);

        Bundle bundle=this.getArguments();
        cur_fragment=bundle.getInt("cur_fragment",0);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<ListRecyclerItem> items=new ArrayList<>();
        ListRecyclerItem[] item = new ListRecyclerItem[6];
        switch (cur_fragment){
            case 0:
                item[0]=new ListRecyclerItem(R.drawable.test,"팀빌딩어플","서완규",false,"팀빌딩을 위한 어플");
                item[1]=new ListRecyclerItem(R.drawable.test2,"핼스어플","최에스더",false,"건강을 위한 물먹기 알람 어플");
                item[2]=new ListRecyclerItem(R.drawable.test3,"가계부어플","황의찬",false,"모르는 사이에 빠져나가는 돈!");
                item[3]=new ListRecyclerItem(R.drawable.test4,"밥먹기어플","송명호",false,"같이 밥먹어요!");
                item[4]=new ListRecyclerItem(R.drawable.test,"여행어플","김유진",false,"사진과 함께 여행을!!");
                item[5]=new ListRecyclerItem(R.drawable.test2,"고민어플","한혜미",false,"고민이 많으신가요?");
                for(int i=0;i<6;i++)
                items.add(item[i]);
                break;
            case 1:
                item[0]=new ListRecyclerItem(R.drawable.test,"","송명호",false,"c, c++, 안드로이드,java, 포토샵 쪼금");
                item[1]=new ListRecyclerItem(R.drawable.test3,"","김유진",false,"c, c++, java, html, css");
                item[2]=new ListRecyclerItem(R.drawable.test4,"","한혜미",false,"c, c++, java, mysql");
                for(int i=0;i<3;i++)
                    items.add(item[i]);
                break;
            case 2:
                item[0]=new ListRecyclerItem(R.drawable.test2,"","최에스더",false,"포토샵, 일러스트레이터, UI, UX, 인디자인");
                item[1]=new ListRecyclerItem(R.drawable.test4,"","황의찬",false,"포토샵, 일러스트레이터, HTML, CSS");
                for(int i=0;i<2;i++)
                    items.add(item[i]);
                break;
            default:
                break;
        }
        recyclerView.setAdapter(new RecyclerAdpater(getActivity(),items,R.layout.item_listrecycler,0));

        return cur_container;
    }
}