package dev.obidos.wrd.assistantfortrainingmethod531.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.AddExerciseActivity;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.BaseActivity;

/**
 * Created by vobideyko on 8/31/15.
 */
public class ColorPickerDialog extends Dialog implements View.OnClickListener {

    private AddExerciseActivity m_Activity;
    private TextView m_tvCancelButton;
    private TextView m_tvTitle;
    private ImageView m_ivColor0, m_ivColor1, m_ivColor2;
    private ImageView m_ivColor3, m_ivColor4, m_ivColor5;

    public ColorPickerDialog(AddExerciseActivity baseActivity) {
        super(baseActivity);
        this.m_Activity = baseActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.setCancelable(true);
        setContentView(R.layout.color_picker_dialog);

        m_tvTitle = (TextView) findViewById(R.id.tvTitleDialog);

        m_tvCancelButton = (TextView) findViewById(R.id.btnCancel);
        m_tvCancelButton.setOnClickListener(this);

        m_ivColor0 = (ImageView) findViewById(R.id.color0);
        m_ivColor0.setOnClickListener(this);

        m_ivColor1 = (ImageView) findViewById(R.id.color1);
        m_ivColor1.setOnClickListener(this);

        m_ivColor2 = (ImageView) findViewById(R.id.color2);
        m_ivColor2.setOnClickListener(this);

        m_ivColor3 = (ImageView) findViewById(R.id.color3);
        m_ivColor3.setOnClickListener(this);

        m_ivColor4 = (ImageView) findViewById(R.id.color4);
        m_ivColor4.setOnClickListener(this);

        m_ivColor5 = (ImageView) findViewById(R.id.color5);
        m_ivColor5.setOnClickListener(this);

        m_Activity.setMediumFont(m_tvCancelButton);
        m_Activity.setRegularFont(m_tvTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                break;
            case R.id.color0:
                m_Activity.setColorExercise(0);
                break;
            case R.id.color1:
                m_Activity.setColorExercise(1);
                break;
            case R.id.color2:
                m_Activity.setColorExercise(2);
                break;
            case R.id.color3:
                m_Activity.setColorExercise(3);
                break;
            case R.id.color4:
                m_Activity.setColorExercise(4);
                break;
            case R.id.color5:
                m_Activity.setColorExercise(5);
                break;
        }
        dismiss();
    }
}