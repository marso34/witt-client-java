package com.example.healthappttt.adapter;//package com.example.healthappttt.adapter;
//
//import android.app.Activity;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.healthappttt.Data.Exercize;
//import com.example.healthappttt.Data.Routine;
//import com.example.healthappttt.Data.Set;
//import com.example.healthappttt.Data.User;
//import com.example.healthappttt.R;
//
//import java.util.ArrayList;
//
//
//    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MainViewHolder> {
//        private User[] UserList;
//
//        @NonNull
//        @Override
//        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//
//        public class MainViewHolder extends RecyclerView.ViewHolder {
//            public Double Lat;
//            public Double Lon;
//            public String LocationN;
//            public String PreferredTime;
//            public String[] ExerciseArea;
//            public String Name;
//            public String ProfImg;
//            public String LocaName;
//
//            public MainViewHolder(View view) {
//                super(view);
//
//
//            }
//        }
//    }
//}

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final ArrayList<User> mDataset;
    private ArrayList<User> UserList;
    private Activity activity;
    public UserAdapter(Activity activity, ArrayList<User> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;
    }
    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user_ = mDataset.get(position);
//        if(mDataset.get(position).getPhotoUrl() != null){
//            Glide.with(activity).load(mDataset.get(position).getPhotoUrl()).centerCrop().override(500).into(photoImageVIew);
//        }db에서 정보가져오는부분 포지션활용해야함
        holder.onBind(UserList.get(position));
    }

    public void setUserList(ArrayList<User> list){
        this.UserList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public Double Lat;
        public Double Lon;
        public TextView PreferredTime;
        public TextView ExerciseArea;
        public TextView Name;
        public ImageView ProfImg;
        public TextView LocaName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //사진 불러오기profile = (ImageView) itemView.findViewById(R.id.profile);
            Name = (TextView) itemView.findViewById(R.id.UNE);
           LocaName = (TextView) itemView.findViewById(R.id.GT);
            ProfImg = (ImageView) itemView.findViewById(R.id.PRI);
            PreferredTime = (TextView) itemView.findViewById(R.id.GoodTime);
            ExerciseArea = (TextView) itemView.findViewById(R.id.EArea);
        }
        void onBind(User item){
            //profile.setImageResource(item.getResourceId()); 이미지 넣어주는곳
            Name.setText(item.getUserName());
            LocaName.setText(item.getLocationName());
            PreferredTime.setText(item.getGoodTime());
            //ExerciseArea.setText(item.get);루틴 불러서 처리할것. 온데이타체인지로
            //Lat = 디비에서 저장된거 가져오기.
            //Lon = 디비에서 저장된거 가져오기.
        }
    }

}