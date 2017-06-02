package com.kimjunhong.jobplanner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.adapter.ExpandableAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 5. 31..
 */

public class ListFragment extends Fragment {
    @BindView(R.id.expandableListView) ExpandableListView mListView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        mGroupList = new ArrayList<String>();
        mChildList = new ArrayList<ArrayList<String>>();
        mChildListContent = new ArrayList<String>();

        mGroupList.add("서류");
        mGroupList.add("필기");
        mGroupList.add("면접");
        mGroupList.add("결과");

        mChildListContent.add("A Company");
        mChildListContent.add("B Company");
        mChildListContent.add("C Company");

        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);

        mListView.setAdapter(new ExpandableAdapter(getActivity(), mGroupList, mChildList));
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
                Toast.makeText(getActivity(), "PLUS", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
