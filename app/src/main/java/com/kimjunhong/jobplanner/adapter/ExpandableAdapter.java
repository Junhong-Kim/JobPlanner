package com.kimjunhong.jobplanner.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.model.Recruit;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by INMA on 2017. 6. 2..
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> parentList;
    private HashMap<String, ArrayList<Recruit>> childHashMap;
    private ParentViewHolder parentVH;
    private ChildViewHolder childVH;

    public ExpandableAdapter(Context context, ArrayList<String> parentList, HashMap<String, ArrayList<Recruit>> childHashMap){
        super();
        this.context = context;
        this.parentList = parentList;
        this.childHashMap = childHashMap;
    }

    // Parent - position return
    @Override
    public String getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    // Parent - size return
    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    // Parent - ID return
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Parent - ROW return
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater groupInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = groupInflater.inflate(R.layout.item_recruit_parent, parent, false);

            parentVH = new ParentViewHolder();
            parentVH.layout = (LinearLayout) view.findViewById(R.id.recruit_parent_layout);
            parentVH.process = (TextView) view.findViewById(R.id.recruit_parent_process);
            parentVH.count = (TextView) view.findViewById(R.id.recruit_parent_count);
            parentVH.indicator = (ImageView) view.findViewById(R.id.recruit_parent_indicator);

            view.setTag(parentVH);

        } else {
            parentVH = (ParentViewHolder)view.getTag();
        }

        // Set - Even parent layout color
        if(groupPosition % 2 != 0) {
            parentVH.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.sub));
        }

        // Set - Indicator image
        if(isExpanded) {
            parentVH.indicator.setImageResource(R.drawable.icon_collapse_arrow);
        } else {
            parentVH.indicator.setImageResource(R.drawable.icon_expand_arrow);
        }

        // Set - Process & Count
        parentVH.process.setText(getGroup(groupPosition));
        parentVH.count.setText("(" + getChildrenCount(groupPosition) + ")");

        return view;
    }

    // Child - position return
    @Override
    public Recruit getChild(int groupPosition, int childPosition) {
        return childHashMap.get(parentList.get(groupPosition)).get(childPosition);
    }

    // Child - size return
    @Override
    public int getChildrenCount(int groupPosition) {
        return childHashMap.get(parentList.get(groupPosition)).size();
    }

    // Child - ID return
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Child - ROW return
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater childInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = childInflater.inflate(R.layout.item_recruit_child, null);

            childVH = new ChildViewHolder();
            childVH.logo = (ImageView) view.findViewById(R.id.recruit_child_logo);
            childVH.company = (TextView) view.findViewById(R.id.recruit_child_company);
            childVH.pattern = (TextView) view.findViewById(R.id.recruit_child_pattern);
            childVH.position = (TextView) view.findViewById(R.id.recruit_child_position);
            childVH.schedule = (TextView) view.findViewById(R.id.recruit_child_schedule);
            childVH.result = (TextView) view.findViewById(R.id.recruit_child_result);

            view.setTag(childVH);

        } else {
            childVH = (ChildViewHolder) view.getTag();
        }

        // Set - Child View
        Log.v("log", "로고: " + getChild(groupPosition, childPosition).getLogo());

        if(getChild(groupPosition, childPosition).getLogo() == null) {
            childVH.logo.setImageResource(R.drawable.icon_company_logo);
        } else {
            Glide.with(context)
                 .load(getChild(groupPosition, childPosition).getLogo()).asBitmap()
                 // .transform(new CropCircleTransformation(context))
                 .placeholder(R.drawable.icon_picture)
                 .into(childVH.logo);
        }
        childVH.company.setText(getChild(groupPosition, childPosition).getCompany());
        childVH.pattern.setText(getChild(groupPosition, childPosition).getPattern());
        childVH.position.setText(getChild(groupPosition, childPosition).getPosition());
        childVH.schedule.setText(getChild(groupPosition, childPosition).getSchedule());
        childVH.result.setText(getChild(groupPosition, childPosition).getProcessResult());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

    class ParentViewHolder {
        public LinearLayout layout;
        public TextView process;
        public TextView count;
        public ImageView indicator;
    }

    class ChildViewHolder {
        public ImageView logo;
        public TextView company;
        public TextView pattern;
        public TextView position;
        public TextView schedule;
        public TextView result;
    }
}
