package com.gwnu.witt.Data;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.gwnu.witt.R;

public class AdViewHolder extends RecyclerView.ViewHolder {
    public AdView mAdView;

    public AdViewHolder(@NonNull View itemView) {
        super(itemView);
        mAdView = itemView.findViewById(R.id.adView); // 여기서 'adView'는 ad_layout.xml에 정의된 AdView의 ID입니다.
    }
}