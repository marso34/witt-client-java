package com.example.healthappttt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.healthappttt.R;

import java.util.ArrayList;

public class ExerciseListAdapter extends BaseAdapter {

    Context context = null;
    android.view.LayoutInflater LayoutInflater = null;

    ArrayList<String> data;

    public ExerciseListAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        LayoutInflater = android.view.LayoutInflater.from(context);
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = LayoutInflater.inflate(R.layout.adapter_exercise_list, null);

        TextView textView = (TextView) view.findViewById(R.id.exerciseName);

        textView.setText(data.get(position));

        return view;
    }
}
