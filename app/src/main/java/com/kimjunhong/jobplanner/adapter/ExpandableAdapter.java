package com.kimjunhong.jobplanner.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kimjunhong.jobplanner.R;

import java.util.ArrayList;

/**
 * Created by INMA on 2017. 6. 2..
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> groupList = null;
    private ArrayList<ArrayList<String>> childList = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;

    public ExpandableAdapter(Context context, ArrayList<String> groupList, ArrayList<ArrayList<String>> childList){
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.groupList = groupList;
        this.childList = childList;
    }

    /* PARENT */

    // Parent position return
    @Override
    public String getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    // Parent size return
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    // Parent ID return
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Parent ROW return
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null ) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_recruit_parent, parent, false);

            viewHolder.layout = (LinearLayout) v.findViewById(R.id.recruit_parent_layout);
            viewHolder.process = (TextView) v.findViewById(R.id.recruit_process);
            viewHolder.indicator = (ImageView) v.findViewById(R.id.recruit_indicator);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)v.getTag();
        }

        // 짝수번째 Parent layout 색상 지정
        if(groupPosition % 2 != 0) {
            viewHolder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.sub));
        }

        // Indicator 이미지 지정
        if(isExpanded) {
            viewHolder.indicator.setImageResource(R.drawable.icon_collapse_arrow);
        } else {
            viewHolder.indicator.setImageResource(R.drawable.icon_expand_arrow);
        }

        viewHolder.process.setText(getGroup(groupPosition));

        return v;
    }

    /* CHILD */

    // Child position return
    @Override
    public String getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    // Child size return
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    // Child ID return
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Child ROW return
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_recruit_child, null);

            viewHolder.name = (TextView) v.findViewById(R.id.company_name);
            viewHolder.logo = (ImageView) v.findViewById(R.id.company_logo);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)v.getTag();
        }


        viewHolder.name.setText(getChild(groupPosition, childPosition));
        viewHolder.logo.setImageResource(R.drawable.icon_company_logo);

        return v;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

    class ViewHolder {
        public LinearLayout layout;
        public TextView process;
        public ImageView indicator;
        public ImageView logo;
        public TextView name;
    }

}
