package dev.obidos.wrd.assistantfortrainingmethod531.database.entity;

/**
 * Created by vobideyko on 8/17/15.
 */
public class ExerciseWeightData {
    private int m_nId;
    private int m_nIdExercise;
    private double m_fWeight;
    private String m_strDate;

    public int getId() {
        return m_nId;
    }

    public void setId(int nId) {
        this.m_nId = nId;
    }

    public int getIdExercise() {
        return m_nIdExercise;
    }

    public void setIdExercise(int nIdExercise) {
        this.m_nIdExercise = nIdExercise;
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
