package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import dev.obidos.wrd.assistantfortrainingmethod531.tools.TrainingConstants;
import dev.obidos.wrd.assistantfortrainingmethod531.views.CheckableImageView;

/**
 * Created by vobideyko on 8/19/15.
 */
public class ExerciseInfoActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnDismissListener {

    private static final int MENU_CHART = 0;
    private static final int MENU_TIMER = 1;
    private static final int MENU_EDIT = 2;
    private static final int MENU_DELETE = 3;
    private static final int MENU_GET_STUCK = 4;
    private static final int MENU_RESTORE = 5;

    private ExerciseData m_exerciseData;

    private Toolbar mToolbar;

    private ArrayList<LinearLayout> m_arrayListLLMain;
    private ArrayList<CheckableImageView> m_arrayListChkImageView;
    private ArrayList<TextView> m_arrayListTVWeights;
    private ArrayList<TextView> m_arrayListTVReps;

    private LinearLayout m_llBoringButBigContent;

    private TextView m_tvWeekType,m_tvCycleNumber,m_tvMaxWeight,m_tvRecordWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Drawable drawableIconNavigation = ContextCompat.getDrawable(this, R.drawable.svg_back);
        drawableIconNavigation.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        mToolbar.setNavigationIcon(drawableIconNavigation);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Drawable drawableIconOverflow = ContextCompat.getDrawable(this, R.drawable.svg_menu_popup);
        drawableIconOverflow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        mToolbar.setOverflowIcon(drawableIconOverflow);

        m_arrayListLLMain = new ArrayList<>();
        m_arrayListChkImageView = new ArrayList<>();
        m_arrayListTVWeights = new ArrayList<>();
        m_arrayListTVReps = new ArrayList<>();

        initViews();

        initFont();

        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();

        MenuItem itemTimer = menu.add(0, MENU_TIMER, Menu.NONE, R.string.menu_timer);
        MenuItem itemChart = menu.add(1, MENU_CHART, Menu.NONE, R.string.menu_chart);
        MenuItem itemEdit = menu.add(1, MENU_EDIT, Menu.NONE, R.string.text_edit);
        itemEdit.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        if(m_exerciseData.getStatus() == DatabaseHelper.STATUS_DELETED) {
            MenuItem itemRestore = menu.add(1, MENU_RESTORE, Menu.NONE, R.string.text_restore);
            itemRestore.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }

        MenuItem itemDelete = menu.add(1, MENU_DELETE, Menu.NONE, R.string.text_delete);
        itemDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem itemGetStuck = menu.add(1, MENU_GET_STUCK, Menu.NONE, R.string.text_get_stuck);
        itemGetStuck.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        Drawable drawableIcon = ContextCompat.getDrawable(this, R.drawable.svg_timer);
        drawableIcon.setColorFilter(this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        itemTimer.setIcon(drawableIcon);
        itemTimer.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemTimer.setShortcut('0', 'a');

        drawableIcon = ContextCompat.getDrawable(this, R.drawable.svg_chart);
        drawableIcon.setColorFilter(this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        itemChart.setIcon(drawableIcon);
        itemChart.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        itemChart.setShortcut('1', 'b');

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case MENU_CHART:
                Intent chartIntent = new Intent(this, WeightExerciseChartActivity.class);
                chartIntent.putExtra(TrainingConstants.EXTRA_NAME_EXERCISE, m_exerciseData.getName());
                chartIntent.putExtra(TrainingConstants.EXTRA_ID_EXERCISE, m_exerciseData.getId());
                startActivity(chartIntent);
                break;
            case MENU_TIMER:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                TimerDialog timerDialog = new TimerDialog(ExerciseInfoActivity.this, getNextWeight(), getNextReps());
                timerDialog.setOnDismissListener(this);
                timerDialog.show();
                break;
            case MENU_EDIT:
                Intent intent = new Intent(ExerciseInfoActivity.this, AddExerciseActivity.class);
                intent.putExtra(TrainingConstants.EXTRA_ID_EXERCISE,m_exerciseData.getId());
                intent.putExtra(TrainingConstants.EXTRA_FROM_INFO_EXERCISE_ACTIVITY,true);
                startActivity(intent);
                finish();
                break;
            case MENU_RESTORE:
                QuestionDialog questionDialogRestore = new QuestionDialog(ExerciseInfoActivity.this,
                        getResources().getString(R.string.question_restore_exercise), new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper databaseHandler = new DatabaseHelper(getApplicationContext());
                        m_exerciseData.setStatus(DatabaseHelper.STATUS_NORMAL);
                        databaseHandler.updateExercise(m_exerciseData);
                        databaseHandler.close();
                        finish();
                    }
                });
                questionDialogRestore.show();
                break;
            case MENU_DELETE:
                String strQuestion = "";
                switch (m_exerciseData.getStatus()){
                    case DatabaseHelper.STATUS_NORMAL:
                        strQuestion = getResources().getString(R.string.question_move_to_trash);
                        break;
                    case DatabaseHelper.STATUS_DELETED:
                        strQuestion = getResources().getString(R.string.question_delete_exercise);
                        break;
                }
                QuestionDialog questionDialog = new QuestionDialog(ExerciseInfoActivity.this, strQuestion, new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper databaseHandler = new DatabaseHelper(getApplicationContext());
                        switch (m_exerciseData.getStatus()){
                            case DatabaseHelper.STATUS_NORMAL:
                                m_exerciseData.setStatus(DatabaseHelper.STATUS_DELETED);
                                databaseHandler.updateExercise(m_exerciseData);
                                break;
                            case DatabaseHelper.STATUS_DELETED:
                                databaseHandler.deleteExercise(m_exerciseData.getId());
                                break;
                        }
                        databaseHandler.close();
                        finish();
                    }
                });
                questionDialog.show();
                break;
            case MENU_GET_STUCK:
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
                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_exerciseData = databaseHandler.getExercise(getIntent().getIntExtra(TrainingConstants.EXTRA_ID_EXERCISE, -1));
        databaseHandler.close();
        m_tvRecordWeight.setText(m_exerciseData.getRecordWeight());
    }

    private void initViews(){
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
            checkableImageView.setDrawable(R.drawable.svg_checkbox_on, R.drawable.svg_checkbox_off, this);
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

        m_tvWeekType = (TextView) findViewById(R.id.tvWeekType);
        m_tvCycleNumber = (TextView) findViewById(R.id.tvCycleNumber);
        m_tvMaxWeight = (TextView) findViewById(R.id.tvMaxWeight);
        m_tvRecordWeight = (TextView) findViewById(R.id.tvRecordWeight);
    }

    private void initData(){
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_exerciseData = databaseHandler.getExercise(getIntent().getIntExtra(TrainingConstants.EXTRA_ID_EXERCISE,-1));
        databaseHandler.close();

        getSupportActionBar().setTitle(m_exerciseData.getName());

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
    }

    private void setCurrentWeek(){
        if(isSixWeekCycle()){
            switch (getWeekOfCycle()) {
                case 0:
                case 3:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(TrainingConstants.ARRAY_WEIGHT_WARM_UP,
                            TrainingConstants.ARRAY_WEIGHT_WORKOUT_1,
                            TrainingConstants.ARRAY_REPS_WARM_UP,
                            TrainingConstants.ARRAY_REPS_WORKOUT_1,false);
                    break;
                case 1:
                case 4:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(TrainingConstants.ARRAY_WEIGHT_WARM_UP,
                            TrainingConstants.ARRAY_WEIGHT_WORKOUT_2,
                            TrainingConstants.ARRAY_REPS_WARM_UP,
                            TrainingConstants.ARRAY_REPS_WORKOUT_2, false);
                    break;
                case 2:
                case 5:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(TrainingConstants.ARRAY_WEIGHT_WARM_UP,
                            TrainingConstants.ARRAY_WEIGHT_WORKOUT_3,
                            TrainingConstants.ARRAY_REPS_WARM_UP,
                            TrainingConstants.ARRAY_REPS_WORKOUT_3, false);
                    break;
                case 6:
                    setBoringButBigVisible(false);
                    setWeek(TrainingConstants.ARRAY_WEIGHT_WARM_UP_DELOAD,
                            TrainingConstants.ARRAY_WEIGHT_WORKOUT_DELOAD,
                            TrainingConstants.ARRAY_REPS_WARM_UP_DELOAD,
                            TrainingConstants.ARRAY_REPS_WORKOUT_DELOAD,true);
                    break;
            }
        } else {
            switch (getWeekOfCycle()) {
                case 0:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(TrainingConstants.ARRAY_WEIGHT_WARM_UP,
                            TrainingConstants.ARRAY_WEIGHT_WORKOUT_1,
                            TrainingConstants.ARRAY_REPS_WARM_UP,
                            TrainingConstants.ARRAY_REPS_WORKOUT_1,false);
                    break;
                case 1:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(TrainingConstants.ARRAY_WEIGHT_WARM_UP,
                            TrainingConstants.ARRAY_WEIGHT_WORKOUT_2,
                            TrainingConstants.ARRAY_REPS_WARM_UP,
                            TrainingConstants.ARRAY_REPS_WORKOUT_2, false);
                    break;
                case 2:
                    setBoringButBigVisible(isBoringButBigEnable());
                    setWeek(TrainingConstants.ARRAY_WEIGHT_WARM_UP,
                            TrainingConstants.ARRAY_WEIGHT_WORKOUT_3,
                            TrainingConstants.ARRAY_REPS_WARM_UP,
                            TrainingConstants.ARRAY_REPS_WORKOUT_3, false);
                    break;
                case 3:
                    setBoringButBigVisible(false);
                    setWeek(TrainingConstants.ARRAY_WEIGHT_WARM_UP_DELOAD,
                            TrainingConstants.ARRAY_WEIGHT_WORKOUT_DELOAD,
                            TrainingConstants.ARRAY_REPS_WARM_UP_DELOAD,
                            TrainingConstants.ARRAY_REPS_WORKOUT_DELOAD,true);
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
    private void setWeek(float[] fWeightArrayWarmup, float[] fWeightArrayWorkout,int[] nRepsArrayWarmup, int[] nRepsArrayWorkout, boolean deload){
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
                bdWeightOldRecord = (bdWeightOldRecord.divide(new BigDecimal(TrainingConstants.SPECIAL_CONST_FOR_RM_CALCULATION), 0, RoundingMode.HALF_DOWN)).add(BigDecimal.ONE);
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
            case R.id.llWarmUpSet1:
                ind = 0;
                changeStateOfSetLine(ind);
                break;
            case R.id.llWarmUpSet2:
                ind = 1;
                changeStateOfSetLine(ind);
                break;
            case R.id.llWarmUpSet3:
                ind = 2;
                changeStateOfSetLine(ind);
                break;
            case R.id.llWorkoutSet1:
                ind = 3;
                changeStateOfSetLine(ind);
                break;
            case R.id.llWorkoutSet2:
                ind = 4;
                changeStateOfSetLine(ind);
                break;
            case R.id.llWorkoutSet3:
                ind = 5;
                changeStateOfSetLine(ind);
                if(m_arrayListChkImageView.get(ind).isChecked() && ((!isSixWeekCycle() && getWeekOfCycle()!=3) || (isSixWeekCycle() && getWeekOfCycle()!=6))){
                    Intent lastSetCalc = new Intent(this,LastSetCalculationActivity.class);
                    lastSetCalc.putExtra(TrainingConstants.EXTRA_ID_EXERCISE, m_exerciseData.getId());
                    lastSetCalc.putExtra(TrainingConstants.EXTRA_LAST_SET_WEIGHT_EXERCISE, m_arrayListTVWeights.get(5).getText().toString());
                    lastSetCalc.putExtra(TrainingConstants.EXTRA_AIM_WEIGHT_EXERCISE, m_exerciseData.getAimWeight());
                    startActivity(lastSetCalc);
                }
                break;
            case R.id.llBoringButBigSet1:
                ind = 6;
                changeStateOfSetLine(ind);
                break;
            case R.id.llBoringButBigSet2:
                ind = 7;
                changeStateOfSetLine(ind);
                break;
            case R.id.llBoringButBigSet3:
                ind = 8;
                changeStateOfSetLine(ind);
                break;
            case R.id.llBoringButBigSet4:
                ind = 9;
                changeStateOfSetLine(ind);
                break;
            case R.id.llBoringButBigSet5:
                ind = 10;
                changeStateOfSetLine(ind);
                break;
        }
    }

    private void changeStateOfSetLine(int ind){
        m_arrayListChkImageView.get(ind).changeCheckState();

        String charSequenceReps = m_arrayListTVReps.get(ind).getText().toString();
        SpannableString spannableStringReps = new SpannableString(charSequenceReps);

        String charSequenceWeight = m_arrayListTVWeights.get(ind).getText().toString();
        SpannableString spannableStringWeight = new SpannableString(charSequenceWeight);

        if(m_arrayListChkImageView.get(ind).isChecked()){
            m_arrayListTVWeights.get(ind).setTextColor(getResources().getColor(R.color.colorSecondaryText));
            m_arrayListTVReps.get(ind).setTextColor(getResources().getColor(R.color.colorSecondaryText));

            spannableStringReps.setSpan(new StrikethroughSpan(), 0, spannableStringReps.length(), 0);
            spannableStringWeight.setSpan(new StrikethroughSpan(), 0, spannableStringWeight.length(), 0);
        } else {
            m_arrayListTVWeights.get(ind).setTextColor(getResources().getColor(R.color.colorPrimaryText));
            m_arrayListTVReps.get(ind).setTextColor(getResources().getColor(R.color.colorPrimaryText));
        }

        m_arrayListTVReps.get(ind).setText(spannableStringReps, TextView.BufferType.SPANNABLE);
        m_arrayListTVWeights.get(ind).setText(spannableStringWeight, TextView.BufferType.SPANNABLE);
    }

    private void getStuckCalc(){
        BigDecimal weightB = new BigDecimal(m_exerciseData.getWeight());
        weightB = weightB.multiply(new BigDecimal(TrainingConstants.GET_STUCK_CONST));
        double newWeight = weightB.doubleValue();
        m_exerciseData.setWeight(newWeight);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
            setBoringButBigWeight(TrainingConstants.COEFFICIENT_BORING_BUT_BIG);
            setBoringButBigReps(TrainingConstants.ARRAY_REPS_BORING_BUT_BIG);
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
