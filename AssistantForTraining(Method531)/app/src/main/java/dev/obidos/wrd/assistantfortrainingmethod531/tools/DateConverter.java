package dev.obidos.wrd.assistantfortrainingmethod531.tools;

import java.util.Calendar;

/**
 * Created by vobideyko on 8/28/15.
 */
public class DateConverter {

    private String m_strDate;

    public Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        String[] str = m_strDate.split("\\.");
        calendar.set(Integer.valueOf(str[2]),Integer.valueOf(str[1])-1,Integer.valueOf(str[0]),0,0,1);
        return calendar;
    }

    public void setDate(Calendar calendarDate) {
        this.m_strDate = calendarDate.get(Calendar.DAY_OF_MONTH)+"."
                +(calendarDate.get(Calendar.MONTH)+1)+"."
                +calendarDate.get(Calendar.YEAR);
    }

    public String getStrDate() {
        return m_strDate;
    }

    public void setStrDate(String strDate) {
        this.m_strDate = strDate;
    }

}
