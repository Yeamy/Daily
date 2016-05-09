package com.yeamy.daily;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yeamy.daily.data.DataBase;
import com.yeamy.daily.data.DataList;
import com.yeamy.daily.data.HandleTask;
import com.yeamy.daily.data.Mission;
import com.yeamy.daily.view.DividerDecoration;
import com.yeamy.daily.view.ScrollBottomListener;
import com.yeamy.daily.view.SlideLayout;

import java.util.ArrayList;

public class TimelineFragment extends Fragment implements SlideLayout.OnSlideListener {
    public static final int RESULT_EDIT = -1;
    public static final int RESULT_DEL = -2;
    public static final int RESULT_ADD = -3;
    public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
    public static final String EXTRA_MISSION = "mission";

    private RecyclerView recycler;
    private TimelineAdapter adapter = new TimelineAdapter();
    private DataList data = new DataList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);

        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recycler.addItemDecoration(new DividerDecoration(getContext()));
        adapter.setData(data, this);
        AsyncTaskCompat.executeParallel(new LoadTask());
        recycler.addOnScrollListener(new ScrollBottomListener() {

            @Override
            public void onScrollBottom(RecyclerView recyclerView) {
                if (data.size() % LoadTask.LIMIT == 0) {
                    AsyncTaskCompat.executeParallel(new LoadTask());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivityForResult(new Intent(v.getContext(), AddActivity.class), 100);
                break;
            case R.id.slide:
                Mission mission = (Mission) v.getTag();
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra(EXTRA_MISSION, mission);
                startActivityForResult(intent, 101);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        System.out.println("requestCode " + requestCode + " resultCode " + resultCode);
        switch (resultCode) {
            case RESULT_EDIT: {
                Mission mission = data.getParcelableExtra(EXTRA_MISSION);
                this.data.invalidate(adapter, mission);
                break;
            }
            case RESULT_ADD: {
                Mission mission = data.getParcelableExtra(EXTRA_MISSION);
                this.data.add(adapter, mission);
                recycler.smoothScrollToPosition(0);
                break;
            }
            case RESULT_DEL: {
                Mission mission = data.getParcelableExtra(EXTRA_MISSION);
                this.data.remove(adapter, mission);
                break;
            }
            case RESULT_CANCELED:
                break;
        }
    }

    @Override
    public void onSlide(SlideLayout layout) {
        Mission mission = (Mission) layout.getTag();
        data.change(adapter, mission);
        ContentValues values = new ContentValues();
        values.put(DataBase.FINISH, mission.finishTime);
        HandleTask.update(layout.getContext(), mission, values);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recycler = null;
        adapter = null;
    }

    private class LoadTask extends AsyncTask<Object, Object, ArrayList<Mission>> {
        private final static int LIMIT = 100;

        @Override
        protected ArrayList<Mission> doInBackground(Object... params) {
            DataBase db = new DataBase(getContext());
            int size = data.size();
            long from = 0;
            if (size > 0) {
                from = data.get(size - 1).finishTime;
            }
            return db.get(from, LIMIT);
        }

        @Override
        protected void onPostExecute(ArrayList<Mission> list) {
            data.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

}