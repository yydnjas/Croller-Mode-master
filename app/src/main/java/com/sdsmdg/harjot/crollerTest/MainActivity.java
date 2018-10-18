package com.sdsmdg.harjot.crollerTest;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener,
    CompoundButton.OnCheckedChangeListener, TimePickerDialog.OnTimeSetListener {

    Croller croller;

    public int[] modeArray = new int[]{
        0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0, 1, 0, 1, 0, 1};
    private TextView mTv;
    private int mPossion = -1;
    private int mStartPossion = -1;
    private int mLastPossion = -1;
    private int mSelectMode = -1;
    private boolean mSwitchOn = false;


    public int[] mHourArray = new int[]{
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
        10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
        20, 21, 22, 23};
    public int[] mMinuteArray = new int[]{
        0, 30};
    public int[] mChangeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        croller = (Croller) findViewById(R.id.croller);
        mTv = findViewById(R.id.textView);
        RadioGroup rg0 = findViewById(R.id.rg_mode);
        rg0.check(R.id.rg_switch);
        rg0.setOnCheckedChangeListener(this);

//        croller.setIndicatorWidth(10);
//        croller.setBackCircleColor(Color.parseColor("#EDEDED"));
//        croller.setMainCircleColor(Color.WHITE);
//        croller.setMax(50);
//        croller.setStartOffset(45);
//        croller.setIsContinuous(false);
//        croller.setLabelColor(Color.BLACK);
//        croller.setProgressPrimaryColor(Color.parseColor("#0B3C49"));
//        croller.setIndicatorColor(Color.parseColor("#0B3C49"));
//        croller.setProgressSecondaryColor(Color.parseColor("#EEEEEE"));
//        croller.setProgressRadius(380);
//        croller.setBackCircleRadius(300);
        croller.setModeArray(modeArray);

        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                mPossion = progress;
                mLastPossion = progress;
//                Toast.makeText(MainActivity.this, "" + progress, Toast.LENGTH_SHORT).show();
                int hour = progress * 30 / 60;
                int minute = progress * 30 % 60;
                String hourstr;
                String minutestr;
                if (hour >= 10) {
                    hourstr = "" + hour;
                } else {
                    hourstr = "0" + hour;

                }
                if (minute == 0) {
                    minutestr = "0" + minute;
                } else {
                    minutestr = "" + minute;

                }

                mTv.setText(hourstr + ":" + minutestr);

            }

            @Override
            public void onStartTrackingTouch(Croller croller) {
                mStartPossion = mPossion;
                Toast.makeText(MainActivity.this, "mStartPossion:" + mStartPossion, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(Croller croller) {

                Toast.makeText(MainActivity.this, "mLastPossion:" + mLastPossion, Toast.LENGTH_SHORT).show();
               if (mSelectMode!=-1 && mSwitchOn){

                chageArrayData();
               }

            }
        });
    }

    private void chageArrayData() {

        if (mLastPossion - mStartPossion > 0) {
            mChangeArray = new int[mLastPossion - mStartPossion+1];
            for (int i = 0;i<mChangeArray.length;i++){
                mChangeArray[i]=mSelectMode;
            }
            System.arraycopy(mChangeArray, 0, modeArray, mStartPossion-1, mLastPossion - mStartPossion+1);

        }
        else if (mLastPossion - mStartPossion < 0) {
            mChangeArray = new int[mStartPossion - mLastPossion+1];
            for (int i = 0;i<mChangeArray.length;i++){
                mChangeArray[i]=mSelectMode;
            }
            System.arraycopy(mChangeArray, 0, modeArray, mLastPossion - 1, mStartPossion - mLastPossion+1);

        }



//        for (int i:modeArray){
//            Log4j.e("modeArray:"+i);
//        }

        croller.setModeArray(modeArray);

    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.textView:
//                Calendar calendar = Calendar.getInstance();
//                TimePickerDialog dialog = new TimePickerDialog(this, this,
//                    calendar.get(mHourArray[mPossion]), mMinuteArray[0], true);
//                dialog.show();
//                break;
//
//
//            default:
//                return;
//
//
//        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//        switch (buttonView.getId()) {
//            case R.id.switch1:
//                Toast.makeText(MainActivity.this, "switch1:" + isChecked, Toast.LENGTH_SHORT).show();
//                mSwitchOn = isChecked;
//                if (mSwitchOn){
//                    croller.setIndicatorColor(mSelectMode);
//                }else{
//                    croller.setIndicatorColor(-1);
//                }
//                break;
//
//            default:
//                return;
//
//
//        }


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.economy:

                mSelectMode = 0;
                mSwitchOn =true;
                croller.setIndicatorColor(0);
                break;

            case R.id.comfort:

                mSelectMode = 1;
                mSwitchOn =true;
                if (mSwitchOn)
                croller.setIndicatorColor(1);
                break;

            case R.id.rg_switch:


                mSwitchOn =false;
                mSelectMode = 1;
                croller.setIndicatorColor(-1);



                break;

            default:
                return;


        }
    }
}
