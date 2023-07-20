package com.example.healthappttt.Profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.User.ReportHistory;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class TextLineAdpater extends RecyclerView.Adapter<TextLineAdpater.ViewHolder>{
    ArrayList<ReportHistory> ReportList;
//        setContentView(R.layout.activity_text_line_adpater);

    //TODO 생성자
    public TextLineAdpater(ArrayList<ReportHistory> ReportList) {
        this.ReportList = ReportList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_text_line_adpater,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TextLineAdpater.ViewHolder holder, int position) {
        ReportHistory data = ReportList.get(position);
        holder.ReportText.setText(String.valueOf(data.getCONT())); //TODO 여기서 CONT 분류 작업
        holder.listCnt.setText("1change");//TODO CONT 개수 분류 작업

    }

    @Override
    public int getItemCount() {
        return ReportList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ReportText, listCnt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ReportText = itemView.findViewById(R.id.ReportText);
            this.listCnt = itemView.findViewById(R.id.listCnt);
        }
    }

    public void selectCONT(ArrayList<ReportHistory> ReportList ){
        for(ReportHistory cont : ReportList ) {
            cont.getCONT();
        }

    }



}