package com.example.androidapplication;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.aiunit.common.protocol.gesture.GestureLandmark;
import com.aiunit.common.protocol.gesture.GestureResult;
import com.aiunit.common.protocol.gesture.GestureResultList;
import com.aiunit.common.protocol.gesture.GestureType;
import com.aiunit.common.protocol.types.Rect2Pt;
import com.aiunit.vision.common.ConnectionCallback;
import com.aiunit.vision.gesture.HandInputSlot;
import com.aiunit.vision.gesture.HandOutputSlot;
import com.coloros.ocs.ai.cv.CVUnit;
import com.coloros.ocs.ai.cv.CVUnitClient;
import com.coloros.ocs.base.common.ConnectionResult;
import com.coloros.ocs.base.common.api.OnConnectionFailedListener;
import com.coloros.ocs.base.common.api.OnConnectionSucceedListener;

import java.util.List;

public class Recognize extends Activity {
    CVUnitClient mCVClient;

    public void initial(){
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
            }

            @Override
            public void onServiceDisconnect() {
                Log.e("TAG", "initService: onServiceDisconnect: ");
            }
        });
        HandInputSlot inputSlot = (HandInputSlot) mCVClient.createInputSlot();
        //inputSlot.setTargetBitmap(bitmap);
        HandOutputSlot outputSlot = (HandOutputSlot) mCVClient.createOutputSlot();
        mCVClient.process(inputSlot, outputSlot);
        GestureResultList gestureResultList = outputSlot.getHandList();
        /*
        for (GestureResult gestureResult : gestureResultList) {
            GestureLandmark landmark = gestureResult.getLandmark();
            List<Float> lmkList =landmark.getLmk();
        }

         */
        if (mCVClient != null) {
            mCVClient.stop();
        }
        mCVClient.releaseService();
        mCVClient = null;
    }


}
