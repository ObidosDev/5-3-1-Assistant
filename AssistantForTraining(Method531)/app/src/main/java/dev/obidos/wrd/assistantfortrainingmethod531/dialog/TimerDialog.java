package dev.obidos.wrd.assistantfortrainingmethod531.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.BaseActivity;

/**
 * Created by vobideyko on 8/31/15.
 */
public class TimerDialog extends Dialog implements View.OnClickListener {

    private BaseActivity m_Activity;
    private TextView m_tvCancelButton;
    private TextView m_tvTitle;
    private TextView m_tvTimerValue;
    private TextView m_tvNextWeight;
    private TextView m_tvNextReps;
    private TimerRest timer;
    private String m_strNextWeight;
    private String m_strNextReps;

    public TimerDialog(BaseActivity baseActivity, String strNextWeight, String strNextReps) {
        super(baseActivity);
        this.m_Activity = baseActivity;
        m_strNextWeight = strNextWeight;
        m_strNextReps = strNextReps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.setCancelable(false);
        setContentView(R.layout.dialog_timer);

        m_tvTitle = (TextView) findViewById(R.id.tvTitleDialog);

        m_tvTimerValue = (TextView) findViewById(R.id.tvTimerValue);

        m_tvNextWeight = (TextView) findViewById(R.id.tvNextSet);
        m_tvNextWeight.setText(getContext().getResources().getString(R.string.timer_next_set)+"  "+m_strNextWeight + "   " + m_strNextReps);

        m_tvCancelButton = (TextView) findViewById(R.id.btnCancel);
        m_tvCancelButton.setOnClickListener(this);

        m_Activity.setMediumFont(m_tvCancelButton);
        m_Activity.setMediumFont(m_tvNextWeight);
        m_Activity.setRegularFont(m_tvTitle);
        m_Activity.setRegularFont(m_tvTimerValue);

        timer = new TimerRest(m_Activity.getTimeBetweenSets(),500);
        timer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                timer.cancel();
                break;
        }
        dismiss();
    }

    private class TimerRest extends CountDownTimer {

        public TimerRest(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            m_tvTimerValue.setText(R.string.timer_end_phrase);
            if(m_Activity.isVibrationEnable()) {
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(new long[]{0, 300, 200, 300, 200, 1000}, -1);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            millisUntilFinished+=1000;
            long minute = millisUntilFinished/1000/60;
            long sec = (millisUntilFinished-minute*1000*60)/1000;
            long millis = millisUntilFinished - minute*1000*60 - sec*1000;
            String minuteStr = String.valueOf(minute);
            if(minuteStr.length()==1){
                minuteStr = "0"+minuteStr;
            }
            String secStr = String.valueOf(sec);
            if(secStr.length()==1){
                secStr = "0"+secStr;
            }
            String time =minuteStr+":"+secStr;
            m_tvTimerValue.setText(time);

        }
    }
}