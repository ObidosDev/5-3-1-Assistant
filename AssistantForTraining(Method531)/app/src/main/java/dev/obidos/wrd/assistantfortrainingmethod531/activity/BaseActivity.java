package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;

import dev.obidos.wrd.assistantfortrainingmethod531.AssistantApp;
import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseData;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.tools.DateConverter;
import dev.obidos.wrd.assistantfortrainingmethod531.tools.PreferencesHelper;

/**
 * Created by vobideyko on 8/14/15.
 */
public class BaseActivity extends AppCompatActivity {

    private static Typeface m_sTypeFaceMedium;
    private static Typeface m_sTypeFaceRegular;

    private PreferencesHelper m_preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        m_sTypeFaceMedium = Typeface.createFromAsset(getAssets(), "roboto-medium.ttf");
        m_sTypeFaceRegular = Typeface.createFromAsset(getAssets(), "roboto-regular.ttf");
        m_preferencesHelper = new PreferencesHelper();
    }

    public void setMediumFont(View view) {
        ((TextView) view).setTypeface(m_sTypeFaceMedium);
    }

    public void setRegularFont(View view) {
        ((TextView) view).setTypeface(m_sTypeFaceRegular);
    }

    public static Typeface getTypeFace() {
        return m_sTypeFaceMedium;
    }



    public int getWeekOfCycle(){
            return m_preferencesHelper.getWeekOfCycle();
    }

    public void setWeekOfCycle(int nNumberWeekInCycle){
        m_preferencesHelper.setWeekOfCycle(nNumberWeekInCycle);
    }

    public String getStrDateStartOfCycle(){
            return m_preferencesHelper.getStrDateStartOfCycle();
    }

    public void setStrDateStartOfCycle(String strDateStartOfCycle){
        m_preferencesHelper.setStrDateStartOfCycle(strDateStartOfCycle);
    }

    public String getWeightAddTop(){
            return m_preferencesHelper.getWeightAddTop();
    }

    public void setWeightAddTop(String strWeightAddTop){
        m_preferencesHelper.setWeightAddTop(strWeightAddTop);
    }

    public String getWeightAddBottom(){
            return m_preferencesHelper.getWeightAddBottom();
    }

    public void setWeightAddBottom(String strWeightAddBottom){
        m_preferencesHelper.setWeightAddBottom(strWeightAddBottom);
    }

    public String getMinWeightPlateInGym(){
            return m_preferencesHelper.getMinWeightPlateInGym();
    }

    public void setMinWeightPlateInGym(String strMinWeightInGym){
        m_preferencesHelper.setMinWeightPlateInGym(strMinWeightInGym);
    }

    public boolean isCalculateCycle(){
            return m_preferencesHelper.isCalculateCycle();
    }

    public void setCalculateCycle(boolean bCalculate){
        m_preferencesHelper.setCalculateCycle(bCalculate);

            DateConverter currentDate = new DateConverter();
            currentDate.setDate(Calendar.getInstance());
            if (bCalculate) {
                DateConverter stopDate = new DateConverter();
                stopDate.setStrDate(getDateStopCalculationCycles());
                long millis = currentDate.getDate().getTimeInMillis() - stopDate.getDate().getTimeInMillis();
                long days = millis / ExerciseData.MILLIS_DAY;
                if (days > 2) {
                    setStrDateStartOfCycle(currentDate.getStrDate());
                }
                refreshAllDataAboutAllExercisesAndDates();
            } else {
                setDateStopCalculationCycles(currentDate.getStrDate());
            }
    }

    public boolean isSkipDeloadWeek(){
            return m_preferencesHelper.isSkipDeloadWeek();
    }

    public void setSkipDeloadWeek(boolean bSkip, final SettingsActivity settingsActivity){
        m_preferencesHelper.setSkipDeloadWeek(bSkip);

            if (bSkip && !isSixWeekCycle() && getWeekOfCycle() == 3) {
                QuestionDialog questionDialog = new QuestionDialog(settingsActivity, getString(R.string.question_skip_deload_start_new_cycle), new Runnable() {
                    @Override
                    public void run() {
                        refreshAllDataAboutAllExercisesAndDates();
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        settingsActivity.setCheckOffDeloadSkip();
                    }
                });
                questionDialog.show();
            }
            if (bSkip && isSixWeekCycle() && getWeekOfCycle() == 6) {
                QuestionDialog questionDialog = new QuestionDialog(settingsActivity, getString(R.string.question_skip_deload_start_new_cycle), new Runnable() {
                    @Override
                    public void run() {
                        refreshAllDataAboutAllExercisesAndDates();
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        settingsActivity.setCheckOffDeloadSkip();
                    }
                });
                questionDialog.show();
            }
    }

    public boolean isSixWeekCycle(){
            return m_preferencesHelper.isSixWeekCycle();
    }

    public void setSixWeekCycle(boolean bSixWeek, final SettingsActivity settingsActivity){
        m_preferencesHelper.setSixWeekCycle(bSixWeek);

            if (bSixWeek && getWeekOfCycle() == 3) {
                QuestionDialog questionDialog = new QuestionDialog(settingsActivity, getString(R.string.question_start_six_week_deload), new Runnable() {
                    @Override
                    public void run() {
                        refreshAllDataAboutAllExercisesAndDates();
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        m_preferencesHelper.setSixWeekCycle(false);
                        settingsActivity.setCheckOffSixWeek();
                    }
                });
                questionDialog.show();
            }

            if (!bSixWeek
                    && (getWeekOfCycle() >= 3)) {
                QuestionDialog questionDialog = new QuestionDialog(settingsActivity, getString(R.string.question_stop_six_week), new Runnable() {
                    @Override
                    public void run() {
                        DateConverter dateConverter = new DateConverter();
                        dateConverter.setStrDate(getStrDateStartOfCycle());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(dateConverter.getDate().getTimeInMillis() + ExerciseData.MILLIS_WEEK * 3);
                        dateConverter.setDate(calendar);
                        setStrDateStartOfCycle(dateConverter.getStrDate());
                        refreshAllDataAboutAllExercisesAndDates();
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        m_preferencesHelper.setSixWeekCycle(true);
                        settingsActivity.setCheckOnSixWeek();
                    }
                });
                questionDialog.show();
            }
    }

    private String getDateStopCalculationCycles(){
            return m_preferencesHelper.getDateStopCalculationCycles();
    }

    private void setDateStopCalculationCycles(String strCalculateStopDate){
        m_preferencesHelper.setDateStopCalculationCycles(strCalculateStopDate);
    }

    public long getTimeBetweenSets(){
            return m_preferencesHelper.getTimeBetweenSets();
    }

    public void setTimeBetweenSets(long nMillis){
        m_preferencesHelper.setTimeBetweenSets(nMillis);
    }

    public boolean isSixWeekMiddleUpdated(){
            return m_preferencesHelper.isSixWeekMiddleUpdated();
    }

    public void setSixWeekMiddleUpdated(boolean bUpdated){
        m_preferencesHelper.setSixWeekMiddleUpdated(bUpdated);
    }

    public boolean isBoringButBigEnable(){
            return m_preferencesHelper.isBoringButBigEnable();
    }

    public void setBoringButBigEnable(boolean bBoringButBig){
        m_preferencesHelper.setBoringButBigEnable(bBoringButBig);
    }

    public boolean isVibrationEnable(){
            return m_preferencesHelper.isVibrationEnable();
    }

    public void setVibrationEnable(boolean bVibration){
        m_preferencesHelper.setVibrationEnable(bVibration);
    }


    public void refreshAllDataAboutAllExercisesAndDates(){

        if(!isCalculateCycle()) {
            return;
        }

        int weeksInCycle = 4;
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        ArrayList<ExerciseData> exerciseDataArrayList = databaseHandler.getAllExercises();

        if (exerciseDataArrayList.size() == 0) {
            return;
        }

        DateConverter dateStarOfCycle = new DateConverter();
        dateStarOfCycle.setStrDate(getStrDateStartOfCycle());
        DateConverter dateCurrent = new DateConverter();
        dateCurrent.setDate(Calendar.getInstance());
        long lMillisBetweenStartOfCycleAndNow = dateCurrent.getDate().getTimeInMillis() - dateStarOfCycle.getDate().getTimeInMillis();
        //weeks
        long weeks = lMillisBetweenStartOfCycleAndNow / ExerciseData.MILLIS_WEEK;
        long numberOfWeek = weeks % weeksInCycle;

        if (isSkipDeloadWeek()) {
            weeksInCycle = 3;
            numberOfWeek = weeks % weeksInCycle;
        }

        if (isSixWeekCycle()) {
            if(isSkipDeloadWeek()){
                weeksInCycle = 6;
            } else {
                weeksInCycle = 7;
            }
            numberOfWeek = weeks % weeksInCycle;
        }

        setWeekOfCycle((int) numberOfWeek);

        //cycles
        long cycles = weeks / weeksInCycle;
        String addTopWeight = getWeightAddTop();
        String addBottomWeight = getWeightAddBottom();
        if (cycles > 0) {
            long millis = dateStarOfCycle.getDate().getTimeInMillis()+cycles*ExerciseData.MILLIS_WEEK*weeksInCycle;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            dateStarOfCycle.setDate(calendar);
            setStrDateStartOfCycle(dateStarOfCycle.getStrDate());
            setSixWeekMiddleUpdated(false);
            for (ExerciseData exerciseData : exerciseDataArrayList) {
                long localCycles = cycles;
                if(exerciseData.getNumberCycle()==0){
                    localCycles--;
                    exerciseData.setNumberCycle(1);
                    if(localCycles==0){
                        databaseHandler.updateExercise(exerciseData);
                        continue;
                    }
                }
                BigDecimal cyclesB = new BigDecimal(localCycles);
                if (exerciseData.getType() == ExerciseData.PART_TOP) {
                    BigDecimal addTopWeightB = new BigDecimal(addTopWeight);
                    addTopWeightB = addTopWeightB.multiply(cyclesB);
                    addTopWeightB = addTopWeightB.add(new BigDecimal(exerciseData.getWeight()));
                    double newWeight = addTopWeightB.doubleValue();
                    exerciseData.setWeight(newWeight);
                } else {
                    BigDecimal addBottomWeightB = new BigDecimal(addBottomWeight);
                    addBottomWeightB = addBottomWeightB.multiply(cyclesB);
                    addBottomWeightB = addBottomWeightB.add(new BigDecimal(exerciseData.getWeight()));
                    double newWeight = addBottomWeightB.doubleValue();
                    exerciseData.setWeight(newWeight);
                }
                exerciseData.setNumberCycle((int) (exerciseData.getNumberCycle() + localCycles));

            databaseHandler.updateExercise(exerciseData);
            }
            databaseHandler.close();
        } else {
            if(isSixWeekCycle() && getWeekOfCycle()>2 && !isSixWeekMiddleUpdated()){
                setSixWeekMiddleUpdated(true);
                for (ExerciseData exerciseData : exerciseDataArrayList) {
                    if(exerciseData.getNumberCycle()==0){
                        continue;
                    }
                    BigDecimal cyclesB = new BigDecimal("1");
                    if (exerciseData.getType() == ExerciseData.PART_TOP) {
                        BigDecimal addTopWeightB = new BigDecimal(addTopWeight);
                        addTopWeightB = addTopWeightB.multiply(cyclesB);
                        addTopWeightB = addTopWeightB.add(new BigDecimal(exerciseData.getWeight()));
                        double newWeight = addTopWeightB.doubleValue();
                        exerciseData.setWeight(newWeight);
                    } else {
                        BigDecimal addBottomWeightB = new BigDecimal(addBottomWeight);
                        addBottomWeightB = addBottomWeightB.multiply(cyclesB);
                        addBottomWeightB = addBottomWeightB.add(new BigDecimal(exerciseData.getWeight()));
                        double newWeight = addBottomWeightB.doubleValue();
                        exerciseData.setWeight(newWeight);
                    }
                    databaseHandler.updateExercise(exerciseData);
                }
                databaseHandler.close();
            }
        }
    }

    public String removeAllZeroAndDotFromStr(String strValue){
        boolean f = true;
        int j = -1;
        for(int i=strValue.length()-1;i>=0;i--){
            if(f && (strValue.charAt(i)=='0' || strValue.charAt(i)=='.')){
                if(strValue.charAt(i)=='.'){
                    f=false;
                }
                j=i;
            } else {
                break;
            }
        }
        if(j!=-1){
            strValue = strValue.substring(0,j);
        }
        return strValue;
    }

    public String formatWeight(double fWeight){
        //Log.e("MuTag", fWeight+"");

        BigDecimal minWeight = new BigDecimal(getMinWeightPlateInGym());//min plate
        //Log.e("MyTag","minWeight " + minWeight.toString());

        minWeight = minWeight.add(minWeight);//From 2 sides
        //Log.e("MyTag","minWeight*2 " + minWeight.toString());

        BigDecimal readyWeight10 = new BigDecimal((((int)(fWeight/10))*10));//set weight with 10kg
        //Log.e("MyTag","readyWeight " + readyWeight10.toString());

        BigDecimal weight = new BigDecimal(fWeight);//current weight
        weight = weight.subtract(readyWeight10);//subtract already done weight
        //Log.e("MyTag", "fWeight-=readyWeight  " + weight.toString());

        if(minWeight.doubleValue()<=2.5){
            BigDecimal readyWeight5 = new BigDecimal((((int)(weight.doubleValue()/5))*5));//set weight with 5kg
            //Log.e("MyTag","readyWeight " + readyWeight5.toString());

            weight = weight.subtract(readyWeight5);//subtract already done weight
            //Log.e("MyTag", "fWeight-=readyWeight  " + weight.toString());

            readyWeight10 = readyWeight10.add(readyWeight5);//sum ready weights
            //Log.e("MyTag", "readyWeight10+=readyWeight5  " + readyWeight10.toString());
        }

        BigDecimal countOfWeightPlates = new BigDecimal(weight.divide(minWeight, 2, RoundingMode.HALF_DOWN).toBigInteger().toString());
        //Log.e("MyTag", "countOfWeightPlates  " + countOfWeightPlates.toString());

        //Log.e("MyTag", "return  " + countOfWeightPlates.multiply(minWeight).add(readyWeight10).toString());
        return removeAllZeroAndDotFromStr(countOfWeightPlates.multiply(minWeight).add(readyWeight10).toString());
    }

    public String formatName(String strName, int nLong){
        if(strName.length()>nLong+1){
            strName = strName.substring(0,nLong).trim() + "…";
        }
        return strName;
    }

    public void openKeyboard(View view){
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
    }
}
