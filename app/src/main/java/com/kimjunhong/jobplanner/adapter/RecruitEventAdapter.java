package com.kimjunhong.jobplanner.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.activity.RecruitActivity;
import com.kimjunhong.jobplanner.item.RecruitEventItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 6. 5..
 */

public class RecruitEventAdapter extends RecyclerView.Adapter<RecruitEventAdapter.ViewHolder> {
    private Context context;
    private List<RecruitEventItem> items;

    public RecruitEventAdapter(Context context, List<RecruitEventItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recruit_event, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecruitEventItem item = items.get(position);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecruitActivity.class);
                intent.putExtra("id", item.getId());
                context.startActivity(intent);
            }
        });

        Glide.with(context)
             .load(item.getLogo()).asBitmap()
             .into(holder.logo);

        holder.name.setText(item.getCompany());
        holder.job.setText(item.getPosition());
        holder.pattern.setText(item.getPattern());
        holder.time.setText(item.getScheduleTime());
        holder.process.setText(item.getProcess());
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
        @BindView(R.id.recruit_event_pattern) TextView pattern;
        @BindView(R.id.recruit_event_time) TextView time;
        @BindView(R.id.recruit_event_process) TextView process;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
