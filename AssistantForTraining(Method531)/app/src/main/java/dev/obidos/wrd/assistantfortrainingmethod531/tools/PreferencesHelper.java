package dev.obidos.wrd.assistantfortrainingmethod531.tools;

import android.content.SharedPreferences;

import java.util.Calendar;

import dev.obidos.wrd.assistantfortrainingmethod531.AssistantApp;
import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.SettingsActivity;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseData;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;

/**
 * Created by vobideyko on 9/11/15.
 */
public class PreferencesHelper {
    private static final String WEEK_NUMBER_IN_CYCLE = "week_number_in_cycle";
    private static final String DATE_START_OF_CYCLE = "date_start_of_cycle";
    private static final String WEIGHT_ADD_TOP = "weight_add_top";
    private static final String WEIGHT_ADD_BOTTOM = "weight_add_bottom";
    private static final String WEIGHT_MIN_IN_GYM = "weight_min_in_gym";
    private static final String CYCLE_CALCULATION = "cycle_calculation";
    private static final String CYCLE_STOP_DATE = "cycle_stop_date";
    private static final String SKIP_DELOAD_WEEK = "skip_deload_week";
    private static final String SIX_WEEK_CYCLE = "six_week_cycle";
    private static final String TIMER_MILLIS = "timer_millis";
    private static final String SIX_WEEK_MIDDLE_UPDATE = "six_week_middle_update";
    private static final String BORING_BUT_BIG_ENABLE = "boring_but_big_enable";
    private static final String VIBRATION_ENABLE = "vibration_enable";

    private SharedPreferences m_sharedPreferences;

    public PreferencesHelper(){
        m_sharedPreferences = AssistantApp.getSharedPreferences();
    }

    public int getWeekOfCycle(){
        return m_sharedPreferences.getInt(WEEK_NUMBER_IN_CYCLE, 0);
    }

    public void setWeekOfCycle(int nNumberWeekInCycle){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putInt(WEEK_NUMBER_IN_CYCLE, nNumberWeekInCycle);
        editor.commit();
    }

    public String getStrDateStartOfCycle(){
        DateConverter dateConverter = new DateConverter();
        dateConverter.setDate(Calendar.getInstance());
        return m_sharedPreferences.getString(DATE_START_OF_CYCLE, dateConverter.getStrDate());
    }

    public void setStrDateStartOfCycle(String strDateStartOfCycle){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putString(DATE_START_OF_CYCLE, strDateStartOfCycle);
        editor.commit();
    }

    public String getWeightAddTop(){
        return m_sharedPreferences.getString(WEIGHT_ADD_TOP, "2");
    }

    public void setWeightAddTop(String strWeightAddTop){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putString(WEIGHT_ADD_TOP, strWeightAddTop);
        editor.commit();
    }

    public String getWeightAddBottom(){
        return m_sharedPreferences.getString(WEIGHT_ADD_BOTTOM, "4");
    }

    public void setWeightAddBottom(String strWeightAddBottom){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putString(WEIGHT_ADD_BOTTOM, strWeightAddBottom);
        editor.commit();
    }

    public String getMinWeightPlateInGym(){
        return m_sharedPreferences.getString(WEIGHT_MIN_IN_GYM, "1.25");
    }

    public void setMinWeightPlateInGym(String strMinWeightInGym){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putString(WEIGHT_MIN_IN_GYM, strMinWeightInGym);
        editor.commit();
    }

    public boolean isCalculateCycle(){
        return m_sharedPreferences.getBoolean(CYCLE_CALCULATION, true);
    }

    public void setCalculateCycle(boolean bCalculate){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putBoolean(CYCLE_CALCULATION, bCalculate);
        editor.commit();
    }

    public boolean isSkipDeloadWeek(){
        return m_sharedPreferences.getBoolean(SKIP_DELOAD_WEEK, false);
    }

    public void setSkipDeloadWeek(boolean bSkip){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putBoolean(SKIP_DELOAD_WEEK, bSkip);
        editor.commit();
    }

    public boolean isSixWeekCycle(){
        return m_sharedPreferences.getBoolean(SIX_WEEK_CYCLE, false);
    }

    public void setSixWeekCycle(boolean bSixWeek){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putBoolean(SIX_WEEK_CYCLE, bSixWeek);
        editor.commit();
    }

    public String getDateStopCalculationCycles(){
        return m_sharedPreferences.getString(CYCLE_STOP_DATE, "1.1.2015");
    }

    public void setDateStopCalculationCycles(String strCalculateStopDate){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putString(CYCLE_STOP_DATE, strCalculateStopDate);
        editor.commit();
    }

    public long getTimeBetweenSets(){
        return m_sharedPreferences.getLong(TIMER_MILLIS, 45000);
    }

    public void setTimeBetweenSets(long nMillis){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putLong(TIMER_MILLIS, nMillis);
        editor.commit();
    }

    public boolean isSixWeekMiddleUpdated(){
        return m_sharedPreferences.getBoolean(SIX_WEEK_MIDDLE_UPDATE, false);
    }

    public void setSixWeekMiddleUpdated(boolean bUpdated){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putBoolean(SIX_WEEK_MIDDLE_UPDATE, bUpdated);
        editor.commit();
    }

    public boolean isBoringButBigEnable(){
        return m_sharedPreferences.getBoolean(BORING_BUT_BIG_ENABLE, false);
    }

    public void setBoringButBigEnable(boolean bBoringButBig){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putBoolean(BORING_BUT_BIG_ENABLE, bBoringButBig);
        editor.commit();
    }

    public boolean isVibrationEnable(){
        return m_sharedPreferences.getBoolean(VIBRATION_ENABLE, true);
    }

    public void setVibrationEnable(boolean bVibration){
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putBoolean(VIBRATION_ENABLE, bVibration);
        editor.commit();
    }
}
