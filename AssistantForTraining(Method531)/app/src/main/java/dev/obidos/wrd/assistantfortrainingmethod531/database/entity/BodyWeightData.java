package dev.obidos.wrd.assistantfortrainingmethod531.database.entity;

/**
 * Created by vobideyko on 8/17/15.
 */
public class BodyWeightData {
    private int m_nId;
    private double m_fWeight;
    private String m_strDate;

    public int getId() {
        return m_nId;
    }

    public void setId(int nId) {
        this.m_nId = nId;
    }

    public double getWeight() {
        return m_fWeight;
    }

    public void setWeight(double fWeight) {
        this.m_fWeight = fWeight;
    }

    public String getStrDate() {
        return m_strDate;
    }

    public void setStrDate(String strDate) {
        this.m_strDate = strDate;
    }
}
