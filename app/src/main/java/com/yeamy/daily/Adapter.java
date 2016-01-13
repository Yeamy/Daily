package com.yeamy.daily;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yeamy.daily.data.DataList;
import com.yeamy.daily.data.Mission;
import com.yeamy.daily.view.SlideLayout;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private DataList data;
    private SlideLayout.OnSlideListener listener;

    public Adapter() {
    }

    public void setData(DataList data, SlideLayout.OnSlideListener listener) {
        this.data = data;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SlideLayout slide;
        TextView content;
        TextView date;
        View point;

        public ViewHolder(View itemView) {
            super(itemView);
            slide = (SlideLayout) itemView.findViewById(R.id.slide);
            content = (TextView) slide.findViewById(R.id.content);
            date = (TextView) itemView.findViewById(R.id.date);
            point = itemView.findViewById(R.id.point);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.li_mission, null);
        ViewHolder holder = new ViewHolder(view);
        holder.slide.setListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mission mission = data.get(position);
        holder.slide.setTag(mission);
        holder.slide.reset();
        holder.content.setText(mission.content);
        if (data.isFirst(position)) {
            holder.date.setVisibility(View.VISIBLE);
            holder.point.setVisibility(View.VISIBLE);
            if (mission.finishDate == null) {
                mission.finishDate = Mission.getDateList(holder.itemView.getContext(),
                        mission.finishTime);
            }
            holder.date.setText(mission.finishDate);
        } else {
            holder.date.setVisibility(View.GONE);
            holder.point.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
