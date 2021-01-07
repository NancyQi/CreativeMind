package com.example.androidapplication;

import com.aiunit.common.protocol.types.Rect2Pt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.aiunit.common.protocol.gesture.GestureResult;
import com.aiunit.common.protocol.gesture.GestureResultList;
import com.aiunit.vision.common.ConnectionCallback;
import com.aiunit.vision.gesture.HandInputSlot;
import com.aiunit.vision.gesture.HandOutputSlot;
import com.coloros.ocs.ai.cv.CVUnit;
import com.coloros.ocs.ai.cv.CVUnitClient;
import com.coloros.ocs.base.common.ConnectionResult;
import com.coloros.ocs.base.common.api.OnConnectionFailedListener;
import com.coloros.ocs.base.common.api.OnConnectionSucceedListener;

@SuppressLint("SdCardPath")
public class Recognize extends Activity {
    private ImageView view;
    private final int TAKE_PHOTOS_CODE = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognize);
        /**
         * Called when the activity is first created.
         */
        Button button = (Button) findViewById(R.id.button1);
        view = (ImageView) findViewById(R.id.imageView1);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTOS_CODE);
            }
        });
    }

    @SuppressLint("SdCardPath")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == TAKE_PHOTOS_CODE && resultCode == RESULT_OK) {
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            view.setImageBitmap(bm);
            //initial();

            mCVClient = CVUnit.getGestureLandmarkDetectorClient
                    (this.getApplicationContext()).addOnConnectionSucceedListener(new OnConnectionSucceedListener() {
                @Override
                public void onConnectionSucceed() {
                    Log.i("TAG", " authorize connect: onConnectionSucceed");
                }
            }).addOnConnectionFailedListener(new OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.e("TAG", " authorize connect: onFailure: " + connectionResult.getErrorCode());
                }
            });
            mCVClient.initService(this, new ConnectionCallback() {
                @Override
                public void onServiceConnect() {
                    Log.i("TAG", "initService: onServiceConnect");
                    int startCode = mCVClient.start();
                    if(startCode!=0){
                        System.out.println("there's false startCode");
                        System.exit(0);
                    }
                }

                @Override
                public void onServiceDisconnect() {
                    Log.e("TAG", "initService: onServiceDisconnect: ");
                }
            });

            //recognize
            HandInputSlot inputSlot = (HandInputSlot) mCVClient.createInputSlot();
            inputSlot.setTargetBitmap(bm);


            HandOutputSlot outputSlot = (HandOutputSlot) mCVClient.createOutputSlot();
            mCVClient.process(inputSlot, outputSlot);

            GestureResultList handList = outputSlot.getHandList();

            for (int handOrder = 0; handOrder < handList.getList().size(); ++handOrder) {
                GestureResult hand = handList.getList().get(handOrder);
                Rect2Pt boundingBox = hand.getBoundingBox();
            }

            /*
            GestureResultList gestureResultList = outputSlot.getHandList();

            for (GestureResult gestureResult : gestureResultList.a) {
                GestureLandmark landmark = gestureResult.getLandmark();
                List<Float> lmkList = landmark.getLmk();
            }
            */
            System.out.println(handList.getList().get(0));
            if (mCVClient != null) {
                mCVClient.stop();
                mCVClient.releaseService();
                mCVClient = null;
            }

        }
    }


    //recognize
    CVUnitClient mCVClient;

    public void initial() {


    }
}