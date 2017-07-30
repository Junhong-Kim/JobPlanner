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
import com.kimjunhong.jobplanner.item.RecruitChartItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 6. 19..
 */

public class RecruitChartAdapter extends RecyclerView.Adapter<RecruitChartAdapter.ViewHolder> {
    private Context context;
    private List<RecruitChartItem> items;

    public RecruitChartAdapter(Context context, List<RecruitChartItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recruit_chart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecruitChartItem item = items.get(position);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecruitActivity.class);
                intent.putExtra("id", item.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        Glide.with(context)
             .load(item.getLogo()).asBitmap()
             .into(holder.logo);

        holder.company.setText(item.getCompany());
        holder.position.setText(item.getPosition());
        holder.pattern.setText(item.getPattern());
        holder.result.setText(item.getProcessResult());
        holder.schedule.setText(item.getSchedule());
        holder.process.setText(item.getProcess());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recruit_chart_layout) LinearLayout layout;
        @BindView(R.id.recruit_chart_logo) ImageView logo;
        @BindView(R.id.recruit_chart_company) TextView company;
        @BindView(R.id.recruit_chart_position) TextView position;
        @BindView(R.id.recruit_chart_pattern) TextView pattern;
        @BindView(R.id.recruit_chart_result) TextView result;
        @BindView(R.id.recruit_chart_schedule) TextView schedule;
        @BindView(R.id.recruit_chart_process) TextView process;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
