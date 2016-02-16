package dev.obidos.wrd.assistantfortrainingmethod531.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.BaseActivity;

/**
 * Created by vobideyko on 8/18/15.
 */
public class EnterTimeDialog extends Dialog implements View.OnClickListener {

    private BaseActivity m_Activity;
    private TextView m_tvSaveButton, m_tvCancelButton;
    private TextView m_tvTitle;
    private EditText m_edtMinutes, m_edtSeconds;

    public EnterTimeDialog(BaseActivity baseActivity) {
        super(baseActivity);
        this.m_Activity = baseActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.setCancelable(false);
        setContentView(R.layout.dialog_enter_time);

        m_tvTitle = (TextView) findViewById(R.id.tvTitleDialog);

        m_edtMinutes = (EditText) findViewById(R.id.edtMinutes);
        m_edtMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String strMinutes = s.toString();
                if(strMinutes.length()>0 && Integer.valueOf(strMinutes)>=60)
                {
                    m_edtMinutes.setText(strMinutes.substring(0,1));
                }
            }
        });

        m_edtSeconds = (EditText) findViewById(R.id.edtSeconds);
        m_edtSeconds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String strSeconds = s.toString();
                if(strSeconds.length()>0 && Integer.valueOf(strSeconds)>=60)
                {
                    m_edtSeconds.setText(strSeconds.substring(0,1));
                }
            }
        });

        m_tvSaveButton = (TextView) findViewById(R.id.btnSave);
        m_tvCancelButton = (TextView) findViewById(R.id.btnCancel);
        m_tvSaveButton.setOnClickListener(this);
        m_tvCancelButton.setOnClickListener(this);

        m_Activity.setMediumFont(m_tvSaveButton);
        m_Activity.setMediumFont(m_tvCancelButton);
        m_Activity.setRegularFont(m_tvTitle);
        m_Activity.setMediumFont(m_edtMinutes);
        m_Activity.setMediumFont(m_edtSeconds);
        m_Activity.setMediumFont(findViewById(R.id.tvSig));

        initTimeData();
    }

    private void initTimeData(){
        long lMillis = m_Activity.getTimeBetweenSets();
        long minute = lMillis/1000/60;
        long sec = (lMillis-minute*1000*60)/1000;
        String minuteStr = String.valueOf(minute);
        String secStr = String.valueOf(sec);
        m_edtMinutes.setText(minuteStr);
        m_edtSeconds.setText(secStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                long millis = 0;
                String strSeconds = m_edtSeconds.getText().toString();
                String strMinutes = m_edtMinutes.getText().toString();
                if(strMinutes.length()>0){
                    millis+=(Integer.valueOf(strMinutes)*1000*60);
                }
                if(strSeconds.length()>0){
                    millis+=(Integer.valueOf(strSeconds)*1000);
                }
                m_Activity.setTimeBetweenSets(millis);
                break;
            case R.id.btnCancel:
                break;
        }
        dismiss();
    }
}