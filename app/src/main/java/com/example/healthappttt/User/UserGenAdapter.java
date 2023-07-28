package com.example.healthappttt.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.R;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserGenAdapter extends RecyclerView.Adapter<UserGenAdapter.MainViewHolder> {
    private Context context;

    private ArrayList<UserChat> chatUsers;
    private ArrayList<BlockUserAdapter> BlockUsers;
    private ArrayList<WittListData> WittUsers;
    private ArrayList<UserChat> SelectUsers;

    private boolean[] Outline;
    private int Type;

    public UserGenAdapter(Context context, ArrayList<?> users, int Type) {
        this.context = context;
        this.Type = Type;
        switch (Type) {
            case 0: this.BlockUsers  = (ArrayList<BlockUserAdapter>) users; break;
            case 1: this.chatUsers   = (ArrayList<UserChat>)         users; break;
            case 2: this.WittUsers   = (ArrayList<WittListData>)     users; break;
            case 3: this.SelectUsers = (ArrayList<UserChat>)         users; break;
            default: break;
        }

        this.Outline = new boolean[users.size()];
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public MaterialCardView CardView;
        public View NotifyView;
        public CircleImageView UserImg;
        public TextView UserName, UserInfo, ChatTime;

        public MainViewHolder(@NonNull View view) {
            super(view);

            this.CardView   = view.findViewById(R.id.userCard);
            this.UserName   = view.findViewById(R.id.userName);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_gen, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        mainViewHolder.CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mainViewHolder.getAbsoluteAdapterPosition();

                if (Type == 3) {
                    Outline[position] = !Outline[position];
                    setOutline(position, Outline[position]);
                }
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (Outline[position])  holder.CardView.setStrokeWidth(2); // 아웃라인 키고 끄기
        else                    holder.CardView.setStrokeWidth(0);

        // type에 따라서 조절
//        holder.UserImg. // 이미지 할당
//        holder.UserName.setText(SelectUsers.get(position).getUserNM());
//        holder.UserInfo.setText(users.get(position).getGYM());
    }

    @Override
    public int getItemCount() {
        switch (Type) {
            case 0: return this.BlockUsers.size();
            case 1: return this.chatUsers.size();
            case 2: return this.WittUsers.size();
            case 3: return this.SelectUsers.size();

        }

        return  0;
    }

    public void setOutline(int position, boolean isChecked) {
        this.Outline[position] = isChecked;
        notifyDataSetChanged();
    }
}
