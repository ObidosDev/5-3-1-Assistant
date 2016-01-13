package dev.obidos.wrd.assistantfortrainingmethod531.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.BaseActivity;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.WeightChartActivity;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.BodyWeightData;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;

/**
 * Created by vobideyko on 8/18/15.
 */
public class AddBodyWeightDialog extends Dialog implements
        View.OnClickListener {

    private BaseActivity m_Activity;
    private BodyWeightData m_bodyWeightData=null;
    private TextView m_tvSaveButton, m_tvCancelButton;
    private EditText m_edtWeight;
    private DatePicker m_datePicker;
    private Toast m_toastDate;
    private TextInputLayout m_tilBodyWeight;

    public AddBodyWeightDialog(BaseActivity baseActivity) {
        super(baseActivity);
        this.m_Activity = baseActivity;
    }

    public AddBodyWeightDialog(BaseActivity baseActivity, BodyWeightData bodyWeightData) {
        super(baseActivity);
        this.m_Activity = baseActivity;
        m_bodyWeightData = bodyWeightData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.setCancelable(false);
        setContentView(R.layout.add_body_weight_dialog);

        m_tilBodyWeight = (TextInputLayout) findViewById(R.id.tilBodyWeight);
        removeErrorFromTIL(m_tilBodyWeight);

        m_toastDate = Toast.makeText(AddBodyWeightDialog.this.getContext(), R.string.weight_from_future,Toast.LENGTH_SHORT);

        m_tvSaveButton = (TextView) findViewById(R.id.btnSave);
        m_tvCancelButton = (TextView) findViewById(R.id.btnCancel);
        m_tvSaveButton.setOnClickListener(this);
        m_tvCancelButton.setOnClickListener(this);

        m_edtWeight = (EditText) findViewById(R.id.edtWeight);
        m_datePicker = (DatePicker) findViewById(R.id.datePicker);

        m_datePicker.setCalendarViewShown(false);
        m_datePicker.setSpinnersShown(true);

        m_Activity.setRegularFont(findViewById(R.id.tvTitleDialog));
        m_Activity.setMediumFont(m_tvSaveButton);
        m_Activity.setMediumFont(m_tvCancelButton);
        m_Activity.setMediumFont(m_edtWeight);

        if(m_bodyWeightData!=null){
            ((TextView)findViewById(R.id.tvTitleDialog)).setText(getContext().getResources().getString(R.string.title_dialog_change_body_weight));
            m_edtWeight.setText(String.valueOf(m_bodyWeightData.getWeight()));
            String[] strDateInfos = m_bodyWeightData.getStrDate().split("\\.");
            m_datePicker.updateDate(Integer.valueOf(strDateInfos[2]),
                                    Integer.valueOf(strDateInfos[1])-1,
                                    Integer.valueOf(strDateInfos[0]));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                String strDate = m_datePicker.getDayOfMonth() + "." + (m_datePicker.getMonth()+1) + "." + m_datePicker.getYear();
                if(checkWeight() && checkDate()) {
                    double fWeight = Double.valueOf(m_edtWeight.getText().toString());
                    DatabaseHelper databaseHandler = new DatabaseHelper(m_Activity);
                    if(m_bodyWeightData==null) {
                        m_bodyWeightData = new BodyWeightData();
                        m_bodyWeightData.setStrDate(strDate);
                        m_bodyWeightData.setWeight(fWeight);
                        databaseHandler.addBodyWeight(m_bodyWeightData);
                    } else {
                        m_bodyWeightData.setStrDate(strDate);
                        m_bodyWeightData.setWeight(fWeight);
                        databaseHandler.updateBodyWeight(m_bodyWeightData);
                    }
                    databaseHandler.close();
                    dismiss();
                    ((WeightChartActivity)m_Activity).refreshChartData();
                }
                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }

    private boolean checkWeight(){
        if(m_edtWeight.getText().toString().length()==0){
            m_tilBodyWeight.setError(getContext().getResources().getString(R.string.toast_enter_weight));
            return false;
        }
        if(m_edtWeight.getText().toString().length()!=0 && Double.valueOf(m_edtWeight.getText().toString())==0){
            m_tilBodyWeight.setError(getContext().getResources().getString(R.string.toast_weight_bigger_than_zero));
            return false;
        }
        removeErrorFromTIL(m_tilBodyWeight);
        return true;
    }

    private boolean checkDate(){
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.YEAR)<m_datePicker.getYear()){
            if(m_toastDate !=null && m_toastDate.getView().isShown()){
                m_toastDate.cancel();
            }
            m_toastDate.show();
            return false;
        }

        if (calendar.get(Calendar.MONTH) < m_datePicker.getMonth()
                && calendar.get(Calendar.YEAR)==m_datePicker.getYear()) {
            if (m_toastDate != null && m_toastDate.getView().isShown()) {
                m_toastDate.cancel();
            }
            m_toastDate.show();
            return false;
        }

        if(calendar.get(Calendar.DAY_OF_MONTH)<m_datePicker.getDayOfMonth()
                && calendar.get(Calendar.MONTH) == m_datePicker.getMonth()
                && calendar.get(Calendar.YEAR)==m_datePicker.getYear()){
            if(m_toastDate !=null && m_toastDate.getView().isShown()){
                m_toastDate.cancel();
            }
            m_toastDate.show();
            return false;
        }
        return true;
    }

    private void removeErrorFromTIL(TextInputLayout textInputLayout){
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError("sds");
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }
}