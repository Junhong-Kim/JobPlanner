package com.kimjunhong.jobplanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.item.RecruitEventItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 6. 5..
 */

public class RecruitEventAdapter extends RecyclerView.Adapter<RecruitEventAdapter.ViewHolder> {
    Context context;
    List<RecruitEventItem> items;

    public RecruitEventAdapter(Context context, List<RecruitEventItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recruit_event, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecruitEventItem item = items.get(position);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "EVENT", Toast.LENGTH_SHORT).show();
            }
        });

        holder.logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_company_logo));
        holder.name.setText(item.getName());
        holder.job.setText(item.getJob());
        holder.time.setText(item.getTime());
        holder.process.setText(item.getProcess());
        holder.division.setText(item.getDivision());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recruit_event_layout) LinearLayout layout;
        @BindView(R.id.recruit_event_logo) ImageView logo;
        @BindView(R.id.recruit_event_name) TextView name;
        @BindView(R.id.recruit_event_job) TextView job;
        @BindView(R.id.recruit_event_time) TextView time;
        @BindView(R.id.recruit_event_process) TextView process;
        @BindView(R.id.recruit_event_division) TextView division;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
