package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseData;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.TimerDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.views.CheckableImageView;

/**
 * Created by vobideyko on 8/19/15.
 */
public class ExerciseInfoActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnDismissListener, PopupMenu.OnMenuItemClickListener {

    private ExerciseData m_exerciseData;

    private ImageView m_ivTimer, m_ivMenu, m_ivChart;

    private ArrayList<LinearLayout> m_arrayListLLMain;
    private ArrayList<CheckableImageView> m_arrayListChkImageView;
    private ArrayList<TextView> m_arrayListTVWeights;
    private ArrayList<TextView> m_arrayListTVReps;

    private LinearLayout m_llBoringButBigContent;

    private TextView m_tvTitleName;
    private TextView m_tvWeekType,m_tvCycleNumber,m_tvMaxWeight,m_tvRecordWeight;

    private int[] m_nArrRepsWarmUp = {5, 5, 3};//default warm up
    private int[] m_nArrRepsWarmUpDeload = {5, 5, 5};//warm up deload

    private int[] m_nArrRepsWorkout1 = {5, 5, 5};// 555 week reps
    private int[] m_nArrRepsWorkout2 = {3, 3, 3};// 333 week reps
    private int[] m_nArrRepsWorkout3 = {5, 3, 1};// 531 week reps

    private int[] m_nArrRepsWorkoutDeload = {5, 5, 5};// deload reps

    private float[] m_fArrWeightWarmUp = {0.4f, 0.5f, 0.6f};
    private float[] m_fArrWeightWarmUpDeload = {0.4f, 0.4f, 0.5f};

    private float[] m_fArrWeightWorkout1 = {0.65f, 0.75f, 0.85f};// 555 week weight
    private float[] m_fArrWeightWorkout2 = {0.70f, 0.80f, 0.90f};// 333 week weight
    private float[] m_fArrWeightWorkout3 = {0.75f, 0.85f, 0.95f};// 531 week weight

    private float[] m_fArrWeightWorkoutDeload = {0.5f, 0.6f, 0.6f};

    private float m_fCoefficientBbb = 0.5f; //BBB weight coefficient
    private int[] m_nArrIntBbbReps = {10, 10, 10, 10, 10};// BBB reps

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_info_activity);

        m_arrayListLLMain = new ArrayList<>();
        m_arrayListChkImageView = new ArrayList<>();
        m_arrayListTVWeights = new ArrayList<>();
        m_arrayListTVReps = new ArrayList<>();

        initViews();

        initFont();

        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_exerciseData = databaseHandler.getExercise(getIntent().getIntExtra("id",-1));
        databaseHandler.close();
        m_tvRecordWeight.setText(m_exerciseData.getRecordWeight());
    }

    private void initViews(){
        findViewById(R.id.ivBack).setOnClickListener(this);

        m_llBoringButBigContent = (LinearLayout) findViewById(R.id.llBoringButBigContent);

        //Warm-up
        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckWarmUpSet1));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llWarmUpSet1));

        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckWarmUpSet2));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llWarmUpSet2));

        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckWarmUpSet3));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llWarmUpSet3));

        //Workout
        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckWorkoutSet1));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llWorkoutSet1));

        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckWorkoutSet2));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llWorkoutSet2));

        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckWorkoutSet3));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llWorkoutSet3));

        //BBB
        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckBoringButBigSet1));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llBoringButBigSet1));

        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckBoringButBigSet2));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llBoringButBigSet2));

        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckBoringButBigSet3));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llBoringButBigSet3));

        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckBoringButBigSet4));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llBoringButBigSet4));

        m_arrayListChkImageView.add((CheckableImageView) findViewById(R.id.ivCheckBoringButBigSet5));
        m_arrayListLLMain.add((LinearLayout) findViewById(R.id.llBoringButBigSet5));

        for(LinearLayout linearLayout : m_arrayListLLMain){
            linearLayout.setOnClickListener(this);
        }

        for(CheckableImageView checkableImageView : m_arrayListChkImageView){
            checkableImageView.setChecked(false);
            checkableImageView.setDrawable(R.drawable.png_checkmark, -1);
        }

        //Weight
        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvWarmUpSet1Weight));
        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvWarmUpSet2Weight));
        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvWarmUpSet3Weight));

        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvSet1Weight));
        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvSet2Weight));
        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvSet3Weight));

        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvBoringButBigSet1Weight));
        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvBoringButBigSet2Weight));
        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvBoringButBigSet3Weight));
        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvBoringButBigSet4Weight));
        m_arrayListTVWeights.add((TextView) findViewById(R.id.tvBoringButBigSet5Weight));

        //Reps
        m_arrayListTVReps.add((TextView) findViewById(R.id.tvWarmUpSet1Repeats));
        m_arrayListTVReps.add((TextView) findViewById(R.id.tvWarmUpSet2Repeats));
        m_arrayListTVReps.add((TextView) findViewById(R.id.tvWarmUpSet3Repeats));

        m_arrayListTVReps.add((TextView) findViewById(R.id.tvSet1Repeats));
        m_arrayListTVReps.add((TextView) findViewById(R.id.tvSet2Repeats));
        m_arrayListTVReps.add((TextView) findViewById(R.id.tvSet3Repeats));

        m_arrayListTVReps.add((TextView) findViewById(R.id.tvBoringButBigSet1Repeats));
        m_arrayListTVReps.add((TextView) findViewById(R.id.tvBoringButBigSet2Repeats));
        m_arrayListTVReps.add((TextView) findViewById(R.id.tvBoringButBigSet3Repeats));
        m_arrayListTVReps.add((TextView) findViewById(R.id.tvBoringButBigSet4Repeats));
        m_arrayListTVReps.add((TextView) findViewById(R.id.tvBoringButBigSet5Repeats));

        m_tvTitleName = (TextView) findViewById(R.id.tvTitleActivity);

        m_tvWeekType = (TextView) findViewById(R.id.tvWeekType);
        m_tvCycleNumber = (TextView) findViewById(R.id.tvCycleNumber);
        m_tvMaxWeight = (TextView) findViewById(R.id.tvMaxWeight);
        m_tvRecordWeight = (TextView) findViewById(R.id.tvRecordWeight);

        m_ivTimer = (ImageView) findViewById(R.id.ivTimer);
        m_ivTimer.setOnClickListener(this);

        m_ivMenu = (ImageView) findViewById(R.id.ivMenu);
        m_ivMenu.setOnClickListener(this);

        m_ivChart = (ImageView) findViewById(R.id.ivChart);
        m_ivChart.setOnClickListener(this);
    }

    private void initData(){
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_exerciseData = databaseHandler.getExercise(getIntent().getIntExtra("id",-1));
        databaseHandler.close();
        m_tvTitleName.setText(formatName(m_exerciseData.getName(), getResources().getInteger(R.integer.exercise_info_title_length)));

        if(isSixWeekCycle()){
            m_tvWeekType.setText("" + getResources().getStringArray(R.array.weeks6)[getWeekOfCycle()]);
        } else {
            m_tvWeekType.setText("" + getResources().getStringArray(R.array.weeks)[getWeekOfCycle()]);
        }
        m_tvCycleNumber.setText("" + m_exerciseData.getNumberCycle());
        m_tvMaxWeight.setText(formatWeight(m_exerciseData.getWeight()));
        m_tvRecordWeight.setText(m_exerciseData.getRecordWeight());

        setCurrentWeek();
    }

    private void initFont(){
        setRegularFont(m_tvWeekType);
        setRegularFont(m_tvCycleNumber);
        setRegularFont(m_tvMaxWeight);
        setRegularFont(m_tvRecordWeight);

        setRegularFont(findViewById(R.id.tvCycleNumberLabel));
        setRegularFont(findViewById(R.id.tvWeekTypeLabel));
        setRegularFont(findViewById(R.id.tvMaxWeightLabel));
        setRegularFont(findViewById(R.id.tvRecordWeightLabel));

        setMediumFont(findViewById(R.id.tvTitleWarmUp));
        setMediumFont(findViewById(R.id.tvTitleWorkout));
        setMediumFont(findViewById(R.id.tvTitleBoringButBig));

        for(TextView textView : m_arrayListTVWeights){
            setRegularFont(textView);
        }

        for(TextView textView : m_arrayListTVReps){
            setRegularFont(textView);
        }

        setMediumFont(m_tvTitleName);
    }

    private void setCurrentWeek(){
        if(isSixWeekCycle()){
            switch (getWeekOfCycle()) {
                case 0:
                case 3:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(m_fArrWeightWarmUp,
                            m_fArrWeightWorkout1,
                            m_nArrRepsWarmUp,
                            m_nArrRepsWorkout1,false);
                    break;
                case 1:
                case 4:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(m_fArrWeightWarmUp,
                            m_fArrWeightWorkout2,
                            m_nArrRepsWarmUp,
                            m_nArrRepsWorkout2, false);
                    break;
                case 2:
                case 5:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(m_fArrWeightWarmUp,
                            m_fArrWeightWorkout3,
                            m_nArrRepsWarmUp,
                            m_nArrRepsWorkout3, false);
                    break;
                case 6:
                    setBoringButBigVisible(false);
                    setWeek(m_fArrWeightWarmUpDeload,
                            m_fArrWeightWorkoutDeload,
                            m_nArrRepsWarmUpDeload,
                            m_nArrRepsWorkoutDeload,true);
                    break;
            }
        } else {
            switch (getWeekOfCycle()) {
                case 0:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(m_fArrWeightWarmUp,
                            m_fArrWeightWorkout1,
                            m_nArrRepsWarmUp,
                            m_nArrRepsWorkout1,false);
                    break;
                case 1:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(m_fArrWeightWarmUp,
                            m_fArrWeightWorkout2,
                            m_nArrRepsWarmUp,
                            m_nArrRepsWorkout2, false);
                    break;
                case 2:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(m_fArrWeightWarmUp,
                            m_fArrWeightWorkout3,
                            m_nArrRepsWarmUp,
                            m_nArrRepsWorkout3, false);
                    break;
                case 3:
                    setBoringButBigVisible(false);
                    setWeek(m_fArrWeightWarmUpDeload,
                            m_fArrWeightWorkoutDeload,
                            m_nArrRepsWarmUpDeload,
                            m_nArrRepsWorkoutDeload,true);
                    break;
            }
        }
    }

    /***
     * @param fWeightArrayWarmup float array consist of 3 items, coefficient of weight in warm up
     * @param fWeightArrayWorkout float array consist of 3 items, coefficient of weight in workout
     * @param nRepsArrayWarmup int array consist of 3 items, reps count in warm up
     * @param nRepsArrayWorkout int array consist of 3 items, reps count in workout
     * @param deload flag which marks week as deload or not
     */
    private void setWeek(float[] fWeightArrayWarmup, float[] fWeightArrayWorkout,int[] nRepsArrayWarmup, int[] nRepsArrayWorkout,boolean deload){
        m_arrayListTVWeights.get(0).setText(formatWeight(m_exerciseData.getWeight() * fWeightArrayWarmup[0]));
        m_arrayListTVWeights.get(1).setText(formatWeight(m_exerciseData.getWeight() * fWeightArrayWarmup[1]));
        m_arrayListTVWeights.get(2).setText(formatWeight(m_exerciseData.getWeight() * fWeightArrayWarmup[2]));

        m_arrayListTVWeights.get(3).setText(formatWeight(m_exerciseData.getWeight() * fWeightArrayWorkout[0]));
        m_arrayListTVWeights.get(4).setText(formatWeight(m_exerciseData.getWeight() * fWeightArrayWorkout[1]));
        m_arrayListTVWeights.get(5).setText(formatWeight(m_exerciseData.getWeight() * fWeightArrayWorkout[2]));

        m_arrayListTVReps.get(0).setText("x"+String.valueOf(nRepsArrayWarmup[0]));
        m_arrayListTVReps.get(1).setText("x" + String.valueOf(nRepsArrayWarmup[1]));
        m_arrayListTVReps.get(2).setText("x" + String.valueOf(nRepsArrayWarmup[2]));

        m_arrayListTVReps.get(3).setText("x" + String.valueOf(nRepsArrayWorkout[0]));
        m_arrayListTVReps.get(4).setText("x" + String.valueOf(nRepsArrayWorkout[1]));
        if(deload){
            m_arrayListTVReps.get(5).setText("x" + String.valueOf(nRepsArrayWorkout[2]));
        } else {
            String strWeightLastSet = m_arrayListTVWeights.get(5).getText().toString();
            String strWeightOldRecord = m_exerciseData.getRecordWeight();
            String strHowManyRepsToRecord="";
            if(Double.valueOf(strWeightOldRecord)!=0) {
                BigDecimal bdWeightLastSet = new BigDecimal(strWeightLastSet);
                BigDecimal bdWeightOldRecord = new BigDecimal(strWeightOldRecord);
                bdWeightOldRecord = bdWeightOldRecord.subtract(bdWeightLastSet);
                bdWeightOldRecord = bdWeightOldRecord.divide(bdWeightLastSet, 10, RoundingMode.HALF_DOWN);
                bdWeightOldRecord = (bdWeightOldRecord.divide(new BigDecimal("0.0333"), 0, RoundingMode.HALF_DOWN)).add(BigDecimal.ONE);
                String strCountRepsForNewRecord = String.valueOf(bdWeightOldRecord.intValue());
                strHowManyRepsToRecord = "(x" + strCountRepsForNewRecord + "+)";
            }
            m_arrayListTVReps.get(5).setText("x" + String.valueOf(nRepsArrayWorkout[2]) + "+ " + strHowManyRepsToRecord);
        }
    }

    @Override
    public void onClick(final View v) {
        int ind;
        switch (v.getId()){
            case R.id.ivMenu:
                PopupMenu popupMenu = new PopupMenu(this, v);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivChart:
                Intent chartIntent = new Intent(this, WeightExerciseChartActivity.class);
                chartIntent.putExtra("name", m_exerciseData.getName());
                chartIntent.putExtra("id", m_exerciseData.getId());
                startActivity(chartIntent);
                break;
            case R.id.ivTimer:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                TimerDialog timerDialog = new TimerDialog(ExerciseInfoActivity.this, getNextWeight(), getNextReps());
                timerDialog.setOnDismissListener(this);
                timerDialog.show();
                break;
            case R.id.llWarmUpSet1:
                ind = 0;
                m_arrayListChkImageView.get(ind).changeCheckState();
                break;
            case R.id.llWarmUpSet2:
                ind = 1;
                m_arrayListChkImageView.get(ind).changeCheckState();
                break;
            case R.id.llWarmUpSet3:
                ind = 2;
                m_arrayListChkImageView.get(ind).changeCheckState();
                break;
            case R.id.llWorkoutSet1:
                ind = 3;
                m_arrayListChkImageView.get(ind).changeCheckState();
                break;
            case R.id.llWorkoutSet2:
                ind = 4;
                m_arrayListChkImageView.get(ind).changeCheckState();
                break;
            case R.id.llWorkoutSet3:
                ind = 5;
                m_arrayListChkImageView.get(ind).changeCheckState();
                if(m_arrayListChkImageView.get(ind).isChecked() && ((!isSixWeekCycle() && getWeekOfCycle()!=3) || (isSixWeekCycle() && getWeekOfCycle()!=6))){
                    Intent lastSetCalc = new Intent(this,LastSetCalculationActivity.class);
                    lastSetCalc.putExtra("id", m_exerciseData.getId());
                    lastSetCalc.putExtra("last_set_weight", m_arrayListTVWeights.get(5).getText().toString());
                    lastSetCalc.putExtra("aim_weight", m_exerciseData.getAimWeight());
                    startActivity(lastSetCalc);
                }
                break;
            case R.id.llBoringButBigSet1:
                ind = 6;
                m_arrayListChkImageView.get(ind).changeCheckState();
                break;
            case R.id.llBoringButBigSet2:
                ind = 7;
                m_arrayListChkImageView.get(ind).changeCheckState();
                break;
            case R.id.llBoringButBigSet3:
                ind = 8;
                m_arrayListChkImageView.get(ind).changeCheckState();
                break;
            case R.id.llBoringButBigSet4:
                ind = 9;
                m_arrayListChkImageView.get(ind).changeCheckState();
                break;
            case R.id.llBoringButBigSet5:
                ind = 10;
                m_arrayListChkImageView.get(ind).changeCheckState();
                break;
        }
    }

    private void getStuckCalc(){
        BigDecimal weightB = new BigDecimal(m_exerciseData.getWeight());
        weightB = weightB.multiply(new BigDecimal("0.9"));
        double newWeight = weightB.doubleValue();
        m_exerciseData.setWeight(newWeight);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_edit:
                Intent intent = new Intent(ExerciseInfoActivity.this, AddExerciseActivity.class);
                intent.putExtra("id",m_exerciseData.getId());
                intent.putExtra("info",true);
                startActivity(intent);
                finish();
                return true;
            case R.id.item_delete:
                QuestionDialog questionDialog = new QuestionDialog(ExerciseInfoActivity.this, getResources().getString(R.string.question_delete_exercise), new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper databaseHandler = new DatabaseHelper(getApplicationContext());
                        databaseHandler.deleteExercise(m_exerciseData.getId());
                        databaseHandler.close();
                        finish();
                    }
                });
                questionDialog.show();
                return true;
            case R.id.item_get_stuck:
                QuestionDialog questionDialog1 = new QuestionDialog(ExerciseInfoActivity.this, getString(R.string.question_get_stuck), new Runnable() {
                    @Override
                    public void run() {
                        getStuckCalc();

                        DatabaseHelper databaseHandler = new DatabaseHelper(getApplicationContext());
                        databaseHandler.updateExercise(m_exerciseData);
                        databaseHandler.close();

                        initData();
                    }
                });
                questionDialog1.show();
                return true;
        }
        return false;
    }

    private String getNextWeight(){
        String str = "-";
        for(int i=0;i<11;i++){
            if(!m_arrayListChkImageView.get(i).isChecked()){
                str = m_arrayListTVWeights.get(i).getText().toString();
                break;
            }
        }
        return str;
    }

    private String getNextReps(){
        String str = "-";
        for(int i=0;i<11;i++){
            if(!m_arrayListChkImageView.get(i).isChecked()){
                str = m_arrayListTVReps.get(i).getText().toString();
                break;
            }
        }
        return str;
    }

    private void setBoringButBigVisible(boolean isVisible){
        if(isVisible){
            m_llBoringButBigContent.setVisibility(View.VISIBLE);
            setBoringButBigWeight(m_fCoefficientBbb);
            setBoringButBigReps(m_nArrIntBbbReps);
        } else {
            m_llBoringButBigContent.setVisibility(View.GONE);
            for (int i = 6; i < 11; i++) {
                m_arrayListChkImageView.get(i).setChecked(true);
            }
        }
    }

    private void setBoringButBigWeight(float fCoefficient){
        String strWeight = formatWeight(m_exerciseData.getWeight() * fCoefficient);
        m_arrayListTVWeights.get(6).setText(strWeight);
        m_arrayListTVWeights.get(7).setText(strWeight);
        m_arrayListTVWeights.get(8).setText(strWeight);
        m_arrayListTVWeights.get(9).setText(strWeight);
        m_arrayListTVWeights.get(10).setText(strWeight);
    }

    private void setBoringButBigReps(int[] arrIntRepsBbb){
        for(int i=6;i<=10;i++){
            m_arrayListTVReps.get(i).setText("x" + arrIntRepsBbb[i-6]);
        }
    }

}
