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
public class InfoDialog extends Dialog implements
        View.OnClickListener {

    private BaseActivity m_Activity;
    private int m_nIdInfoString;
    private TextView m_tvOkButton;
    private TextView m_tvInfo;

    public InfoDialog(BaseActivity baseActivity, int nIdInfoString) {
        super(baseActivity);
        this.m_Activity = baseActivity;
        m_nIdInfoString = nIdInfoString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.setCancelable(true);
        setContentView(R.layout.dialog_info);

        m_tvInfo = (TextView) findViewById(R.id.tvInfo);
        m_tvInfo.setText(getContext().getResources().getString(m_nIdInfoString));

        m_tvOkButton = (TextView) findViewById(R.id.btnOk);
        m_tvOkButton.setOnClickListener(this);

        m_Activity.setMediumFont(m_tvOkButton);
        m_Activity.setRegularFont(m_tvInfo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                break;
        }
        dismiss();
    }
}