package com.kimjunhong.jobplanner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.activity.RecruitActivity;
import com.kimjunhong.jobplanner.adapter.ExpandableAdapter;
import com.kimjunhong.jobplanner.model.Recruit;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by INMA on 2017. 5. 31..
 */

public class ListFragment extends Fragment {
    @BindView(R.id.expandableListView) ExpandableListView mListView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private ArrayList<String> parentList;
    private ArrayList<Recruit> documentProcess;
    private ArrayList<Recruit> testProcess;
    private ArrayList<Recruit> interviewProcess;
    private ArrayList<Recruit> processResult;
    private HashMap<String, ArrayList<Recruit>> childList;

    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        // Parent List Contents
        parentList = new ArrayList<>();
        parentList.add("서류");
        parentList.add("필기");
        parentList.add("면접");
        parentList.add("결과");

        // Child - Document Process Contents
        documentProcess = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        RealmResults<Recruit> documentProcessList = realm.where(Recruit.class).equalTo("processResult", "진행중")
                                                                              .equalTo("process", "서류")
                                                                              .findAll();
        for(Recruit recruit : documentProcessList) {
            Log.v("log", recruit + ": 서류");
            documentProcess.add(recruit);
        }

        // Child - Test Process Contents
        testProcess = new ArrayList<>();
        RealmResults<Recruit> testProcessList = realm.where(Recruit.class).equalTo("processResult", "진행중")
                                                                          .equalTo("process", "인/적성")
                                                                          .or()
                                                                          .equalTo("processResult", "진행중")
                                                                          .equalTo("process", "TEST")
                                                                          .findAll();
        for(Recruit recruit : testProcessList) {
            Log.v("log", recruit + ": 시험");
            testProcess.add(recruit);
        }

        // Child - Interview Process Contents
        interviewProcess = new ArrayList<>();
        RealmResults<Recruit> interviewProcessList = realm.where(Recruit.class).equalTo("processResult", "진행중")
                                                                               .equalTo("process", "1차면접")
                                                                               .or()
                                                                               .equalTo("processResult", "진행중")
                                                                               .equalTo("process", "2차면접")
                                                                               .or()
                                                                               .equalTo("processResult", "진행중")
                                                                               .equalTo("process", "최종면접")
                                                                               .findAll();
        for(Recruit recruit : interviewProcessList) {
            Log.v("log", recruit + ": 면접");
            interviewProcess.add(recruit);
        }

        // Child - Process Result Contents
        processResult = new ArrayList<>();
        RealmResults<Recruit> processResultList = realm.where(Recruit.class).notEqualTo("processResult", "진행중")
                                                                            .findAll();
        for(Recruit recruit : processResultList) {
            Log.v("log", recruit + ": 결과");
            processResult.add(recruit);
        }

        // Mapping - parent with child list
        childList = new HashMap<>();
        childList.put(parentList.get(0), documentProcess);
        childList.put(parentList.get(1), testProcess);
        childList.put(parentList.get(2), interviewProcess);
        childList.put(parentList.get(3), processResult);

        mListView.setAdapter(new ExpandableAdapter(getActivity(), parentList, childList));
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
                Toast.makeText(getActivity(), "Group click = " + groupPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                Toast.makeText(getActivity(), "Child click = " + childPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(), "Group collapse = " + groupPosition, Toast.LENGTH_SHORT).show();
                fab.setVisibility(View.VISIBLE);
            }
        });

        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(), "Group expand = " + groupPosition, Toast.LENGTH_SHORT).show();
                fab.setVisibility(View.INVISIBLE);
            }
        });

        initView();

        return view;
    }

    private void initView() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RecruitActivity.class));
                getActivity().finish();
            }
        });
    }
}
