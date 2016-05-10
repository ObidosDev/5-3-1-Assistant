package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.EnterNumberDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.EnterTimeDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.SetStartTimeDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.tools.TrainingConstants;
import dev.obidos.wrd.assistantfortrainingmethod531.views.CheckableImageView;

/**
 * Created by vobideyko on 8/20/15.
 */
public class SettingsActivity extends BaseActivity implements DialogInterface.OnDismissListener, View.OnClickListener {

    private Toolbar mToolbar;

    private CheckableImageView m_checkableImageViewCalcCycle, m_checkableImageViewSixWeekCycle;
    private CheckableImageView m_checkableImageViewBoringButBig, m_checkableImageViewSkipDeload;
    private CheckableImageView m_checkableImageViewVibration;
    private TextView m_tvMinWeightPlate, m_tvAddWeightTop, m_tvAddWeightBottom, m_tvTimerValue, m_tvStartDateValue;
    private  EnterNumberDialog m_enterNumberDialog;
    private LinearLayout m_llResetAll, m_llResetChart, m_llDeleteAllExercises;
    private LinearLayout m_llAbout, m_llWriteToAuthor, m_llResetAllExerciseCharts, m_llSetStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(R.string.title_settings);

        Drawable drawableIconNavigation = ContextCompat.getDrawable(this, R.drawable.svg_back);
        drawableIconNavigation.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        mToolbar.setNavigationIcon(drawableIconNavigation);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        m_tvStartDateValue = (TextView) findViewById(R.id.tvSetStartDateValue);
        m_tvStartDateValue.setText(getStrDateStartOfCycle());

        m_tvMinWeightPlate = (TextView) findViewById(R.id.tvPlateWeightValue);
        m_tvMinWeightPlate.setText(String.valueOf(getMinWeightPlateInGym()));

        findViewById(R.id.llMinWeightPlate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_enterNumberDialog = new EnterNumberDialog(SettingsActivity.this,
                        m_tvMinWeightPlate.getText().toString(),
                        EnterNumberDialog.MIN_PLATE_WEIGHT);
                m_enterNumberDialog.setOnDismissListener(SettingsActivity.this);
                m_enterNumberDialog.show();
            }
        });

        m_llSetStartDate = (LinearLayout) findViewById(R.id.llSetStartDate);
        m_llSetStartDate.setOnClickListener(this);

        m_llResetAllExerciseCharts = (LinearLayout) findViewById(R.id.llResetExerciseChart);
        m_llResetAllExerciseCharts.setOnClickListener(this);

        m_llAbout = (LinearLayout) findViewById(R.id.llAbout);
        m_llAbout.setOnClickListener(this);

        m_llWriteToAuthor = (LinearLayout) findViewById(R.id.llWriteToAuthor);
        m_llWriteToAuthor.setOnClickListener(this);

        m_tvAddWeightTop = (TextView) findViewById(R.id.tvAddWeightTopValue);
        m_tvAddWeightTop.setText(String.valueOf(getWeightAddTop()));

        findViewById(R.id.llAddWeightTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_enterNumberDialog = new EnterNumberDialog(SettingsActivity.this,
                        m_tvAddWeightTop.getText().toString(),
                        EnterNumberDialog.ADD_WEIGHT_TOP);
                m_enterNumberDialog.setOnDismissListener(SettingsActivity.this);
                m_enterNumberDialog.show();
            }
        });

        m_tvAddWeightBottom = (TextView) findViewById(R.id.tvAddWeightBottomValue);
        m_tvAddWeightBottom.setText(String.valueOf(getWeightAddBottom()));
        findViewById(R.id.llAddWeightBottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_enterNumberDialog = new EnterNumberDialog(SettingsActivity.this,
                        m_tvAddWeightBottom.getText().toString(),
                        EnterNumberDialog.ADD_WEIGHT_BOTTOM);
                m_enterNumberDialog.setOnDismissListener(SettingsActivity.this);
                m_enterNumberDialog.show();
            }
        });

        m_tvTimerValue = (TextView) findViewById(R.id.tvTimerValue);
        m_tvTimerValue.setText(getStrTimeFormatted(getTimeBetweenSets()));
        findViewById(R.id.llTimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterTimeDialog enterTimeDialog = new EnterTimeDialog(SettingsActivity.this);
                enterTimeDialog.setOnDismissListener(SettingsActivity.this);
                enterTimeDialog.show();
            }
        });

        m_llResetAll = (LinearLayout) findViewById(R.id.llResetAll);
        m_llResetAll.setOnClickListener(this);

        m_llResetChart = (LinearLayout) findViewById(R.id.llResetChart);
        m_llResetChart.setOnClickListener(this);

        m_llDeleteAllExercises = (LinearLayout) findViewById(R.id.llDeleteAllExercises);
        m_llDeleteAllExercises.setOnClickListener(this);

        setupCheckableImageView();
        init();
    }

    private void init() {
        setMediumFont(m_tvMinWeightPlate);
        setMediumFont(m_tvAddWeightBottom);
        setMediumFont(m_tvAddWeightTop);
        setMediumFont(m_tvTimerValue);
        setMediumFont(m_tvStartDateValue);

        setMediumFont(findViewById(R.id.tvTitleGeneral));
        setMediumFont(findViewById(R.id.tvTitleCycle));
        setMediumFont(findViewById(R.id.tvTitleWorkout));
        setMediumFont(findViewById(R.id.tvTitleOther));

        setRegularFont(findViewById(R.id.tvPlateWeight));
        setRegularFont(findViewById(R.id.tvAddWeightTop));
        setRegularFont(findViewById(R.id.tvAddWeightBottom));
        setRegularFont(findViewById(R.id.tvResetAll));
        setRegularFont(findViewById(R.id.tvResetChart));
        setRegularFont(findViewById(R.id.tvDeleteAllExercises));
        setRegularFont(findViewById(R.id.tvStartStop));
        setRegularFont(findViewById(R.id.tvAbout));
        setRegularFont(findViewById(R.id.tvWriteToAuthor));
        setRegularFont(findViewById(R.id.tvSixWeekCycle));
        setRegularFont(findViewById(R.id.tvSkipDeload));
        setRegularFont(findViewById(R.id.tvTimer));
        setRegularFont(findViewById(R.id.tvBoringButBig));
        setRegularFont(findViewById(R.id.tvVibration));
        setRegularFont(findViewById(R.id.tvSetStartDate));
    }

    private void setupCheckableImageView(){
        m_checkableImageViewCalcCycle = (CheckableImageView) findViewById(R.id.chbCalculation);
        m_checkableImageViewCalcCycle.setChecked(isCalculateCycle());
        m_checkableImageViewCalcCycle.setOnClickListener(this);

        m_checkableImageViewSixWeekCycle = (CheckableImageView) findViewById(R.id.chbSixWeekCycle);
        m_checkableImageViewSixWeekCycle.setChecked(isSixWeekCycle());
        m_checkableImageViewSixWeekCycle.setOnClickListener(this);

        m_checkableImageViewSkipDeload = (CheckableImageView) findViewById(R.id.chbSkipDeload);
        m_checkableImageViewSkipDeload.setChecked(isSkipDeloadWeek());
        m_checkableImageViewSkipDeload.setOnClickListener(this);

        m_checkableImageViewBoringButBig = (CheckableImageView) findViewById(R.id.chbBoringButBig);
        m_checkableImageViewBoringButBig.setChecked(isBoringButBigEnable());
        m_checkableImageViewBoringButBig.setOnClickListener(this);

        m_checkableImageViewVibration = (CheckableImageView) findViewById(R.id.chbVibration);
        m_checkableImageViewVibration.setChecked(isVibrationEnable());
        m_checkableImageViewVibration.setOnClickListener(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        m_tvMinWeightPlate.setText(getMinWeightPlateInGym());
        m_tvAddWeightTop.setText(getWeightAddTop());
        m_tvAddWeightBottom.setText(getWeightAddBottom());
        m_tvTimerValue.setText(getStrTimeFormatted(getTimeBetweenSets()));
        m_tvStartDateValue.setText(getStrDateStartOfCycle());
    }

    @Override
    public void onClick(View v) {
        QuestionDialog questionDialog;
        switch (v.getId()){
            case R.id.chbCalculation:
                m_checkableImageViewCalcCycle.changeCheckState();
                setCalculateCycle(m_checkableImageViewCalcCycle.isChecked());
                break;
            case R.id.chbBoringButBig:
                m_checkableImageViewBoringButBig.changeCheckState();
                setBoringButBigEnable(m_checkableImageViewBoringButBig.isChecked());
                break;
            case R.id.chbSixWeekCycle:
                m_checkableImageViewSixWeekCycle.changeCheckState();
                setSixWeekCycle(m_checkableImageViewSixWeekCycle.isChecked(), this);
                break;
            case R.id.chbSkipDeload:
                m_checkableImageViewSkipDeload.changeCheckState();
                setSkipDeloadWeek(m_checkableImageViewSkipDeload.isChecked(), this);
                break;
            case R.id.chbVibration:
                m_checkableImageViewVibration.changeCheckState();
                setVibrationEnable(m_checkableImageViewVibration.isChecked());
                break;
            case R.id.llDeleteAllExercises:
                questionDialog = new QuestionDialog(SettingsActivity.this,
                        getString(R.string.question_delete_all_exercises), new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper databaseHandler = new DatabaseHelper(SettingsActivity.this);
                        databaseHandler.deleteAllExercises();
                        databaseHandler.close();
                    }
                });
                questionDialog.show();
                break;
            case R.id.llResetExerciseChart:
                questionDialog = new QuestionDialog(SettingsActivity.this,
                        getString(R.string.question_reset_exercise_chart), new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper databaseHandler = new DatabaseHelper(SettingsActivity.this);
                        databaseHandler.deleteAllExerciseChartWeights();
                        databaseHandler.close();
                    }
                });
                questionDialog.show();
                break;
            case R.id.llResetChart:
                questionDialog = new QuestionDialog(SettingsActivity.this,
                        getString(R.string.question_reset_chart), new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper databaseHandler = new DatabaseHelper(SettingsActivity.this);
                        databaseHandler.deleteAllBodyWeights();
                        databaseHandler.close();
                    }
                });
                questionDialog.show();
                break;
            case R.id.llResetAll:
                questionDialog = new QuestionDialog(SettingsActivity.this,
                        getString(R.string.question_reset_all), new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper databaseHandler = new DatabaseHelper(SettingsActivity.this);
                        databaseHandler.deleteAllBodyWeights();
                        databaseHandler.deleteAllExercises();
                        databaseHandler.close();
                        SettingsActivity.this.setMinWeightPlateInGym("1.25");
                        SettingsActivity.this.setWeightAddTop("2");
                        SettingsActivity.this.setWeightAddBottom("4");
                    }
                });
                questionDialog.setOnDismissListener(SettingsActivity.this);
                questionDialog.show();
                break;
            case R.id.llAbout:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.llWriteToAuthor:
                PackageInfo pInfo = null;
                String strVersionApp = "0.0e";
                int nVersionCode = 0;
                try {
                    pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                    strVersionApp = pInfo.versionName;
                    nVersionCode = pInfo.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getResources().getString(R.string.my_email), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + " "
                        + getResources().getString(R.string.app_version)+ " " + strVersionApp + " (" + nVersionCode + ")");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.title_mail_chooser)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this,
                            R.string.info_no_mail_clients,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.llSetStartDate:
                SetStartTimeDialog setStartTimeDialog = new SetStartTimeDialog(this);
                setStartTimeDialog.setOnDismissListener(SettingsActivity.this);
                setStartTimeDialog.show();
                break;
        }
    }

    public void setCheckOffDeloadSkip() {
        m_checkableImageViewSkipDeload.performClick();
    }

    public void setCheckOffSixWeek() {
        m_checkableImageViewSixWeekCycle.setChecked(false);
    }

    public void setCheckOnSixWeek() {
        m_checkableImageViewSixWeekCycle.setChecked(true);
    }

    private String getStrTimeFormatted(long lMillis){
        long minute = lMillis/1000/60;
        long sec = (lMillis-minute*1000*60)/1000;
        long millis = lMillis - minute*1000*60 - sec*1000;
        String minuteStr = String.valueOf(minute);
        if(minuteStr.length()==1){
            minuteStr = "0"+minuteStr;
        }
        String secStr = String.valueOf(sec);
        if(secStr.length()==1){
            secStr = "0"+secStr;
        }
        String time =minuteStr+":"+secStr;
        return time;
    }
}
