package dev.obidos.wrd.assistantfortrainingmethod531.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.BaseActivity;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.SettingsActivity;

/**
 * Created by vobideyko on 8/18/15.
 */
public class SetStartTimeDialog extends Dialog implements View.OnClickListener {

    private BaseActivity m_Activity;
    private TextView m_tvSaveButton, m_tvCancelButton;
    private TextView m_tvTitle;
    private DatePicker m_datePicker;

    public SetStartTimeDialog(BaseActivity baseActivity) {
        super(baseActivity);
        this.m_Activity = baseActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.setCancelable(true);
        setContentView(R.layout.dialog_set_start_time);

        m_datePicker = (DatePicker) findViewById(R.id.datePicker);

        m_datePicker.setCalendarViewShown(false);
        m_datePicker.setSpinnersShown(true);
        String[] strDateInfos = m_Activity.getStrDateStartOfCycle().split("\\.");
        m_datePicker.updateDate(Integer.valueOf(strDateInfos[2]),
                Integer.valueOf(strDateInfos[1]) - 1,
                Integer.valueOf(strDateInfos[0]));

        m_tvTitle = (TextView) findViewById(R.id.tvTitleDialog);

        m_tvSaveButton = (TextView) findViewById(R.id.btnSave);
        m_tvCancelButton = (TextView) findViewById(R.id.btnCancel);
        m_tvSaveButton.setOnClickListener(this);
        m_tvCancelButton.setOnClickListener(this);

        m_Activity.setMediumFont(m_tvSaveButton);
        m_Activity.setMediumFont(m_tvCancelButton);
        m_Activity.setRegularFont(m_tvTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                QuestionDialog questionDialog = new QuestionDialog(m_Activity, m_Activity.getString(R.string.question_change_cycle_start_date),
                        new Runnable() {
                            @Override
                            public void run() {
                                String strDate = m_datePicker.getDayOfMonth() + "." + (m_datePicker.getMonth()+1) + "." + m_datePicker.getYear();
                                m_Activity.setStrDateStartOfCycle(strDate);
                                m_Activity.refreshAllDataAboutAllExercisesAndDates();
                            }
                        });
                questionDialog.setOnDismissListener((SettingsActivity)m_Activity);
                questionDialog.show();
                break;
            case R.id.btnCancel:
                break;
        }
        dismiss();
    }
}