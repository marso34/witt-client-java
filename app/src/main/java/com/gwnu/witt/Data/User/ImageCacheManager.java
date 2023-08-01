package com.gwnu.witt.Data.User;

import android.graphics.Bitmap;

public class ImageCacheManager {
    private static ImageCacheManager instance;
    private Bitmap downloadedBitmap;
    private String imageUri;

    private ImageCacheManager() {
        // 외부에서 인스턴스를 생성하지 못하도록 private 생성자
    }

    public static ImageCacheManager getInstance() {
        if (instance == null) {
            instance = new ImageCacheManager();
        }
        return instance;
    }

    public Bitmap getDownloadedBitmap() {
        return downloadedBitmap;
    }

    public void setDownloadedBitmap(Bitmap bitmap) {
        downloadedBitmap = bitmap;
    }
    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String uri) {
        imageUri = uri;
    }

    public void clearCache() {
        downloadedBitmap = null;
        imageUri = null;
    }

}