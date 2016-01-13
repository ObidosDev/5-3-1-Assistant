package dev.obidos.wrd.assistantfortrainingmethod531.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.BaseActivity;

/**
 * Created by vobideyko on 8/18/15.
 */
public class EnterNumberDialog extends Dialog implements
        View.OnClickListener {

    public static final int MIN_PLATE_WEIGHT = 0;
    public static final int ADD_WEIGHT_TOP = 1;
    public static final int ADD_WEIGHT_BOTTOM = 2;

    private BaseActivity m_Activity;
    private int m_nType;
    private String m_strOldValue;
    private TextView m_tvOkButton, m_tvCancelButton;
    private TextView m_tvTitle;
    private EditText m_edtValue;

    public EnterNumberDialog(BaseActivity baseActivity, String strOldValue, int nType) {
        super(baseActivity);
        this.m_Activity = baseActivity;
        this.m_nType = nType;
        m_strOldValue = strOldValue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.setCancelable(false);
        setContentView(R.layout.enter_number_dialog);

        m_tvTitle = (TextView) findViewById(R.id.tvTitleDialog);

        m_edtValue = (EditText) findViewById(R.id.edtValue);
        m_edtValue.setText(m_strOldValue);

        m_tvOkButton = (TextView) findViewById(R.id.btnOk);
        m_tvCancelButton = (TextView) findViewById(R.id.btnCancel);
        m_tvOkButton.setOnClickListener(this);
        m_tvCancelButton.setOnClickListener(this);

        m_Activity.setMediumFont(m_tvOkButton);
        m_Activity.setMediumFont(m_tvCancelButton);
        m_Activity.setRegularFont(m_tvTitle);
        m_Activity.setMediumFont(m_edtValue);

        switch (m_nType){
            case MIN_PLATE_WEIGHT:
                m_tvTitle.setText(getContext().getResources().getString(R.string.min_plate_weight));
                break;
            case ADD_WEIGHT_TOP:
                m_tvTitle.setText(getContext().getResources().getString(R.string.additional_weight_for_top));
                break;
            case ADD_WEIGHT_BOTTOM:
                m_tvTitle.setText(getContext().getResources().getString(R.string.additional_weight_for_bottom));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                if(m_edtValue.getText().toString().length()!=0){
                    if(m_nType == MIN_PLATE_WEIGHT && Float.valueOf(m_edtValue.getText().toString())==0){
                        m_edtValue.setText(m_Activity.removeAllZeroAndDotFromStr(m_strOldValue));
                    }
                    m_strOldValue = m_edtValue.getText().toString();
                }
                switch (m_nType){
                    case MIN_PLATE_WEIGHT:
                        m_Activity.setMinWeightPlateInGym(m_Activity.removeAllZeroAndDotFromStr(m_strOldValue));
                        break;
                    case ADD_WEIGHT_TOP:
                        m_Activity.setWeightAddTop(m_Activity.removeAllZeroAndDotFromStr(m_strOldValue));
                        break;
                    case ADD_WEIGHT_BOTTOM:
                        m_Activity.setWeightAddBottom(m_Activity.removeAllZeroAndDotFromStr(m_strOldValue));
                        break;
                }
                break;
            case R.id.btnCancel:
                break;
        }
        dismiss();
    }
}