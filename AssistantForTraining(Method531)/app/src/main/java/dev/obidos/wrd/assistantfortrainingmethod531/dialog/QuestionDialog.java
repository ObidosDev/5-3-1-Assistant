package dev.obidos.wrd.assistantfortrainingmethod531.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.BaseActivity;

/**
 * Created by vobideyko on 8/18/15.
 */
public class QuestionDialog extends Dialog implements
        View.OnClickListener {

    private BaseActivity m_Activity;
    private Runnable m_runnableYes;
    private Runnable m_runnableNo;
    private String m_strQuestion;
    private TextView m_tvYesButton, m_tvNoButton;
    private TextView m_tvQuestion;

    public QuestionDialog(BaseActivity baseActivity,String strQuestion, Runnable runnableYes) {
        super(baseActivity);
        this.m_Activity = baseActivity;
        this.m_runnableYes = runnableYes;
        this.m_runnableNo = new Runnable() {
            @Override
            public void run() {

            }
        };
        m_strQuestion = strQuestion;
    }

    public QuestionDialog(BaseActivity baseActivity,String strQuestion, Runnable runnableYes, Runnable runnableNo) {
        super(baseActivity);
        this.m_Activity = baseActivity;
        this.m_runnableYes = runnableYes;
        this.m_runnableNo = runnableNo;
        m_strQuestion = strQuestion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.setCancelable(true);
        setContentView(R.layout.dialog_question);

        m_tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        m_tvQuestion.setText(m_strQuestion);

        m_tvYesButton = (TextView) findViewById(R.id.btnYes);
        m_tvNoButton = (TextView) findViewById(R.id.btnNo);
        m_tvYesButton.setOnClickListener(this);
        m_tvNoButton.setOnClickListener(this);

        m_Activity.setMediumFont(m_tvYesButton);
        m_Activity.setMediumFont(m_tvNoButton);
        m_Activity.setRegularFont(m_tvQuestion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnYes:
                m_runnableYes.run();
                break;
            case R.id.btnNo:
                m_runnableNo.run();
                break;
        }
        dismiss();
    }
}