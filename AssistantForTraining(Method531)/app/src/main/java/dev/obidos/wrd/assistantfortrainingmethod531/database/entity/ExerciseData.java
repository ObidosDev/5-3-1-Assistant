package dev.obidos.wrd.assistantfortrainingmethod531.database.entity;

import java.util.concurrent.TimeUnit;

/**
 * Created by vobideyko on 8/17/15.
 */
public class ExerciseData {
    public static final long MILLIS_DAY = TimeUnit.DAYS.toMillis(1);
    public static final long MILLIS_WEEK = TimeUnit.DAYS.toMillis(7);

    public static final int PART_TOP=0;
    public static final int PART_BOTTOM=1;

    private int m_nId;
    private String m_strName;
    private int m_nType;
    private double m_fWeight;
    private String m_strAimWeight;
    private String m_strRecordWeight = "0";
    private int m_nNumberCycle = 0;
    private int m_nColorNumber;

    public int getId() {
        return m_nId;
    }

    public void setId(int nId) {
        this.m_nId = nId;
    }

    public String getName() {
        return m_strName;
    }

    public void setName(String strName) {
        this.m_strName = strName;
    }

    public int getType() {
        return m_nType;
    }

    public void setType(int nType) {
        this.m_nType = nType;
    }

    public double getWeight() {
        return m_fWeight;
    }

    public void setWeight(double fWeight) {
        this.m_fWeight = fWeight;
    }

    public String getAimWeight() {
        return m_strAimWeight;
    }

    public void setAimWeight(String strAimWeight) {
        this.m_strAimWeight = strAimWeight;
    }

    public String getRecordWeight() {
        return m_strRecordWeight;
    }

    public void setRecordWeight(String strRecordWeight) {
        this.m_strRecordWeight = strRecordWeight;
    }

    public int getNumberCycle() {
        return m_nNumberCycle;
    }

    public void setNumberCycle(int nNumberCycle) {
        this.m_nNumberCycle = nNumberCycle;
    }

    public int getColorNumber() {
        return m_nColorNumber;
    }

    public void setColorNumber(int nColorNumber) {
        this.m_nColorNumber = nColorNumber;
    }
}
