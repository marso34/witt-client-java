package com.example.healthappttt.adapter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.pkData;

import com.example.healthappttt.Data.BlackListData;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockUserAdapter extends RecyclerView.Adapter<BlockUserAdapter.ViewHolder> {

    private ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);
    ArrayList<BlackListData> BlockList;
    Activity activity;
    SQLiteUtil database = SQLiteUtil.getInstance();


    public BlockUserAdapter(ArrayList<BlackListData> BlockList, Activity activity) {
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
        BlackListData Ddata = BlockList.get(position);//삭제할 위치의 데이터
        int BL_PK = Ddata.getBL_PK();// 삭제할 데이터의 pk값

        if(BlockList.size() > 0) {
            BlackListData data = BlockList.get(position);
            //이름 넣기
            holder.Block_NM.setText(data.getUser_NM());
            //이미지 넣기 - 나중에 처리
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data.getUser_Img(),0,data.getUser_Img().length);
//            holder.Block_Img.setImageBitmap(bitmap);
        }


        // X버튼 누르면 삭제하는 메서드
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BlockList.remove(position);//화면에서 삭제
                    notifyItemRemoved(position);//변경점을 알림
                    database.deleteFromBlackListTable(BL_PK);//로컬db에서 삭제
                    Log.d("sqldelete",BL_PK + " 로컬db에서 해당 PK 삭제완료!");
                    //서버db에서도 삭제해야함
                    deleteFromServer(BL_PK);
                }
            }
        });

    }

    private void deleteFromServer(Integer BL_PK) {
        Call<Integer> call = apiService.deleteFromServer(new pkData(BL_PK));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()) {
                    Integer PK = response.body();
                    Log.d("BlockUserAdapter", "서버 응답 서공: " + PK);
                }else {
                    // 서버 응답이 실패했을때
                    Log.d("BlockUserAdapter", "서버 응답 실패. 상태코드: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("deleteFromServer", "에러메세지: " + t.getMessage());
            }
        });

    }


//  검색할때 해당 검색어가 포함되면 그 목록을 보여주는 매서드드
   public void filterList(ArrayList<BlackListData> filteredList){
        BlockList = filteredList;
        notifyDataSetChanged();
    }

//  adapter_blockuser 구성 요소들을 가리키는 뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder {
        public SQLiteDatabase database;
        TextView Block_NM;
        TextView GYM;
        ImageButton btn_delete;
        ImageView Block_Img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.Block_NM = itemView.findViewById(R.id.Block_NM);
            this.GYM = itemView.findViewById(R.id.GYM);
            btn_delete = itemView.findViewById(R.id.Block_cancel);
            Block_Img = itemView.findViewById(R.id.Block_Img);
        }
    }

    //리스트 개수
    @Override
    public int getItemCount() {
        return BlockList.size();
    }
}