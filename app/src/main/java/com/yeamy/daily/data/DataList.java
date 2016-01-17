package com.yeamy.daily.data;

import com.yeamy.daily.Adapter;

import java.util.ArrayList;
import java.util.Calendar;

public class DataList {
    private ArrayList<Mission> data = new ArrayList<>();
    private Calendar calendar = Calendar.getInstance();

    public boolean isFirst(int position) {
        if (position == 0) {
            return true;
        }
        Mission before = data.get(position - 1);
        Mission current = data.get(position);
        return before.finishTime != current.finishTime;
    }

    public Mission get(int position) {
        return data.get(position);
    }

    public int size() {
        return data.size();
    }

    public void addAll(ArrayList<Mission> list) {
        data.addAll(list);
    }

    public void add(Adapter adapter, Mission mission) {
        int i = 0, first = 0;
        for (Mission tmp : data) {
            if (tmp.finishTime > mission.finishTime) {
                i++;
                continue;
            }
            if (tmp.finishTime == mission.finishTime) {
                first |= 1;
//                if (tmp._id < mission._id) {//ASC by ID
                if (tmp._id > mission._id) {//DESC by ID
                    first |= 2;
                    i++;
                    continue;
                }
            }
            break;
        }
        data.add(i, mission);
        adapter.notifyItemInserted(i);
        if ((first & 3) == 1 && isFirst(i)) {
            adapter.notifyItemChanged(i + 1);
        }
    }

    public void invalidate(Adapter adapter, Mission mission) {
        ArrayList<Mission> data = this.data;
        Mission old = null;
        int position = 0;
        for (Mission item : data) {
            if (item._id == mission._id) {
                old = item;
                break;
            }
            position++;
        }
        if (old == null) {
            return;
        }
        if (old.finishTime == mission.finishTime) {
            old.copy(mission);
            adapter.notifyItemChanged(position);
        } else {
            old.copy(mission);
            data.remove(position);//remove
            adapter.notifyItemRemoved(position);
            if (data.size() > position && isFirst(position)) {//if has next set it to be first
                adapter.notifyItemChanged(position);
            }
            mission.finishDate = null;
            add(adapter, mission);//add back
        }
    }

    public void remove(Adapter adapter, Mission mission) {
        ArrayList<Mission> data = this.data;
        int position = 0;
        for (Mission item : data) {
            if (item._id == mission._id) {
                break;
            }
            position++;
        }
        data.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void change(Adapter adapter, Mission mission) {
        ArrayList<Mission> data = this.data;
        int position = data.indexOf(mission);
        if (position != -1) {
            data.remove(position);//remove
            adapter.notifyItemRemoved(position);
            if (mission.isFinish()) {//set time
                mission.finishTime = Long.MAX_VALUE;
            } else {
                Calendar calendar = this.calendar;
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                mission.finishTime = calendar.getTimeInMillis();
            }
            mission.finishDate = null;
            if (data.size() > position && isFirst(position)) {//if has next set it to be first
                adapter.notifyItemChanged(position);
            }
            add(adapter, mission);//add back
        }
    }
}
