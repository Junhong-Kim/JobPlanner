package com.kimjunhong.jobplanner.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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

    private ExpandableAdapter expandableAdapter;
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

        initData();
        initView();

        return view;
    }

    private void initData() {
        // Parent List Contents
        parentList = new ArrayList<>();
        parentList.add("서류");
        parentList.add("필기");
        parentList.add("면접");
        parentList.add("결과");

        // Child - Document Process Contents
        documentProcess = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        RealmResults<Recruit> documentProcessList = Recruit.findAllByProcessWithResult(realm, "서류", "진행중");

        for(Recruit recruit : documentProcessList) {
            Log.v("log", recruit + ": 서류");
            documentProcess.add(recruit);
        }

        // Child - Test Process Contents
        testProcess = new ArrayList<>();
        RealmResults<Recruit> testProcessList = Recruit.findAllByProcessWithResult(realm, "필기", "진행중");

        for(Recruit recruit : testProcessList) {
            Log.v("log", recruit + ": 필기");
            testProcess.add(recruit);
        }

        // Child - Interview Process Contents
        interviewProcess = new ArrayList<>();
        RealmResults<Recruit> interviewProcessList = Recruit.findAllByProcessWithResult(realm, "면접", "진행중");

        for(Recruit recruit : interviewProcessList) {
            Log.v("log", recruit + ": 면접");
            interviewProcess.add(recruit);
        }

        // Child - Process Result Contents
        processResult = new ArrayList<>();
        RealmResults<Recruit> processResultList = realm.where(Recruit.class).notEqualTo("processResult", "진행중").findAll();

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

        expandableAdapter = new ExpandableAdapter(getActivity(), parentList, childList);
        mListView.setAdapter(expandableAdapter);
    }

    private void initView() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RecruitActivity.class));
                getActivity().finish();
            }
        });

        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
                // Toast.makeText(getActivity(), "Group click = " + groupPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                final int childId = childList.get(parentList.get(groupPosition)).get(childPosition).getId();

                try {
                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Intent intent = new Intent(getActivity(), RecruitActivity.class);
                            Recruit recruit = Recruit.findOne(realm, childId);
                            intent.putExtra("id", recruit.getId());

                            startActivity(intent);
                            // getActivity().finish();
                        }
                    });
                } finally {
                    realm.close();
                }

                return true;
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int itemType = ExpandableListView.getPackedPositionType(id);
                int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                int childPosition = ExpandableListView.getPackedPositionChild(id);

                if(itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    // Toast.makeText(getActivity(), "Group long click" , Toast.LENGTH_SHORT).show();
                } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    final int childId = childList.get(parentList.get(groupPosition)).get(childPosition).getId();
                    // Toast.makeText(getActivity(), "Child long click : " + childId, Toast.LENGTH_SHORT).show();

                    // 다이얼로그 생성
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // 커스텀 다이얼로그 가져오기
                    View customLayout = View.inflate(getActivity(), R.layout.dialog_delete, null);
                    // 빌더에 다이얼로그 적용
                    builder.setView(customLayout);

                    final Dialog dialog = builder.create();
                    // 빌더 크기 적용
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = 1000;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.show();

                    // 디폴트 다이얼로그 투명화
                    Window w = dialog.getWindow();
                    w.setAttributes(lp);
                    w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    //삭제, 취소 클릭
                    customLayout.findViewById(R.id.dialog_button_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                realm = Realm.getDefaultInstance();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        Recruit.delete(realm, childId);
                                        initData();
                                    }
                                });
                            } finally {
                                Toast.makeText(getActivity(), "삭제", Toast.LENGTH_SHORT).show();
                                realm.close();
                                dialog.dismiss();

                            }
                        }
                    });

                    customLayout.findViewById(R.id.dialog_button_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "취소", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }

                return true;
            }
        });

        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                // Toast.makeText(getActivity(), "Group collapse = " + groupPosition, Toast.LENGTH_SHORT).show();
                fab.setVisibility(View.VISIBLE);
            }
        });

        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                // Toast.makeText(getActivity(), "Group expand = " + groupPosition, Toast.LENGTH_SHORT).show();
                fab.setVisibility(View.INVISIBLE);
            }
        });
    }
}
