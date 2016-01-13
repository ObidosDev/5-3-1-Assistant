package dev.obidos.wrd.assistantfortrainingmethod531.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import dev.obidos.wrd.assistantfortrainingmethod531.R;

/**
 * Created by vobideyko on 7/2/15.
 */
public class CheckableImageView extends ImageView {
    private boolean m_bChecked = false;
    private boolean m_bNewDrawable = false;
    private int m_nDrawableChecked = R.drawable.png_check_on;
    private int m_nDrawableNoChecked = R.drawable.png_check_off;

    public CheckableImageView(Context context) {
        super(context);
        setChecked(m_bChecked);
    }

    public CheckableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChecked(m_bChecked);
    }

    public CheckableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChecked(m_bChecked);
    }

    public boolean isChecked(){
        return m_bChecked;
    }

    public void setChecked(boolean isChecked){
        m_bChecked = isChecked;
        if(isChecked){
            this.setImageResource(m_nDrawableChecked);
            if(m_bNewDrawable){
                this.setBackgroundColor(getResources().getColor(R.color.primary_light));
            }
        } else {
            if(m_nDrawableNoChecked!=-1) {
                this.setImageResource(m_nDrawableNoChecked);
            } else {
                this.setImageDrawable(null);
            }
            if(m_bNewDrawable){
                this.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
        this.invalidate();
    }

    public void setDrawable(int idDrawableOn, int idDrawableOff){
        m_nDrawableChecked = idDrawableOn;
        m_nDrawableNoChecked = idDrawableOff;
        m_bNewDrawable = true;
        setChecked(m_bChecked);
    }

    public void changeCheckState(){
        setChecked(!m_bChecked);
    }
}
