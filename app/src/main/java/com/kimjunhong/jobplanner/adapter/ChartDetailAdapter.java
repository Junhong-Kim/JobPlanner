package com.kimjunhong.jobplanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.item.ChartDetailItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 6. 19..
 */

public class ChartDetailAdapter extends RecyclerView.Adapter<ChartDetailAdapter.ViewHolder> {
    Context context;
    List<ChartDetailItem> items;

    public ChartDetailAdapter(Context context, List<ChartDetailItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_detail, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChartDetailItem item = items.get(position);

        holder.logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_company_logo));
        holder.name.setText(item.getName());
        holder.job.setText(item.getJob());
        holder.result.setText(item.getResult());
        holder.date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chart_detail_logo) ImageView logo;
        @BindView(R.id.chart_detail_name) TextView name;
        @BindView(R.id.chart_detail_job) TextView job;
        @BindView(R.id.chart_detail_result) TextView result;
        @BindView(R.id.chart_detail_date) TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
