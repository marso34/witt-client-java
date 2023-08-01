package com.gwnu.witt.Data;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class CircleImageView extends AppCompatImageView {

    private Path clipPath;
    private Paint borderPaint;
    private int borderWidth = 2;
    private Paint backgroundPaint; // 추가: 원 안쪽을 채우기 위한 Paint
    private int borderColor = Color.parseColor("#E3E8EF"); // 이미지 테두리 색깔 여기서 지정


    private int backgroundColor = Color.parseColor("#F3F5F9"); // 테두리 안쪽 색깔을 변경

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        clipPath = new Path();
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 추가: 원 안쪽을 채우기 위한 Paint
        backgroundPaint.setStyle(Paint.Style.FILL); // 추가: 원 안쪽을 채우기 위해 Paint 스타일을 FILL로 설정
        backgroundPaint.setColor(backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        clipPath.reset();
        clipPath.addOval(new RectF(0, 0, width, height), Path.Direction.CW);
        canvas.clipPath(clipPath);

        canvas.drawOval(new RectF(borderWidth, borderWidth, width - borderWidth, height - borderWidth), backgroundPaint);


        super.onDraw(canvas);

        // Draw border
        canvas.drawOval(new RectF(borderWidth / 2f, borderWidth / 2f, width - borderWidth / 2f, height - borderWidth / 2f), borderPaint);
    }
}