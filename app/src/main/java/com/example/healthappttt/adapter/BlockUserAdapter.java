package com.example.healthappttt.adapter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.UserProfile;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class BlockUserAdapter extends RecyclerView.Adapter<BlockUserAdapter.ViewHolder> {

    ArrayList<UserProfile> BlockList;
    Activity activity;
    SQLiteDatabase database;


    public BlockUserAdapter(ArrayList<UserProfile> BlockList, Activity activity) {
        this.BlockList = BlockList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public BlockUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_blockuser,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BlockUserAdapter.ViewHolder holder, int position) {
        UserProfile data = BlockList.get(position);
        holder.Block_NM.setText(data.getUser_NM());
        int BL_PK = data.getUSER_PK();

        // X버튼 누르면 삭제하는 메서드
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BlockList.remove(position);
                    notifyItemRemoved(position);
                    deleteBlockUser(database, BL_PK);
                    Log.d("sqldelete",BL_PK + " 로컬db에서 해당 PK 삭제!");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return BlockList.size();
    }

    public static void deleteBlockUser(SQLiteDatabase database, int pk) {
        database.execSQL("DELETE FROM BLACK_LIST_TB WHERE BL_PK =" + pk);
    }

//  검색할때 해당 검색어가 포함되면 그 목록을 보여주는 매서드드
   public void filterList(ArrayList<UserProfile> filteredList){
        BlockList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Block_NM;
        ImageButton btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.Block_NM = itemView.findViewById(R.id.Block_NM);

            btn_delete = itemView.findViewById(R.id.Block_cancel);


        }
    }
}
