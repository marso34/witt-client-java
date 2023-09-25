package com.gwnu.witt.Data.Exercise;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;

import java.util.List;

public class AiTrainer extends Thread {
    private volatile boolean isFrameReady = false;
    private volatile boolean isProcessingDone = true;
    private Bitmap currentFrame;
    private Module pytorchModel;
    private Fragment poseCorrection;
    private List<TrainingResult> Result;
    private AiTrainer(){}

    public void setModel(Module model) {
        if(this.pytorchModel == null)
            this.pytorchModel = model;
    }

    public void setFrame(Bitmap frame) { // 이미지 넣기
        while (!isProcessingDone) {
            // 스핀락: 처리가 완료될 때까지 대기
        }
        currentFrame = frame;
        isFrameReady = true;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            if (isFrameReady) {
                isProcessingDone = false;
                try {
                    // 1. 이미지 전처리
                    Bitmap preprocessedImage = preprocessImage(currentFrame);

                    // 2. 모델 실행
                    Tensor inputTensor = convertBitmapToTensor(preprocessedImage);
                    IValue outputTensor = pytorchModel.forward(IValue.from(inputTensor));

                    // 3. 결과 처리 (필요한 경우)
                    handleModelOutput(outputTensor);
                }
                finally{
                    isFrameReady = false;
                    isProcessingDone = true;
                }
            }
        }
    }

    private Bitmap preprocessImage(Bitmap image) {
        // 이미지 전처리 로직 구현
        return image;
    }

    private Tensor convertBitmapToTensor(Bitmap image) {
        // Bitmap을 Tensor로 변환하는 로직 구현
        return Tensor.fromBlob(new float[]{}, new long[]{1, 3, image.getWidth(), image.getHeight()});
    }

    private void handleModelOutput(IValue output) {
        // 모델의 출력을 처리하는 로직 구현
        // 리스트에값 담고 대기....
        // 메인에서 꺼내가기 기다리기...
    }
    private List<TrainingResult> popResult(Fragment poseCorrection){
        if(poseCorrection != null){
            return Result;
        }
        else return null;
    }
}
