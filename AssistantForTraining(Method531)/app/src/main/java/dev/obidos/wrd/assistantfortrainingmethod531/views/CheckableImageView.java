package dev.obidos.wrd.assistantfortrainingmethod531.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import dev.obidos.wrd.assistantfortrainingmethod531.R;

/**
 * Created by vobideyko on 7/2/15.
 */
public class CheckableImageView extends ImageView {
    private boolean m_bChecked = false;
    private Drawable m_nDrawableChecked = null;
    private Drawable m_nDrawableNoChecked = null;

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
            if(m_nDrawableChecked != null) {
                this.setImageDrawable(m_nDrawableChecked);
            } else {
                this.setImageResource(R.drawable.png_check_on);
            }
        } else {
            if(m_nDrawableNoChecked != null) {
                this.setImageDrawable(m_nDrawableNoChecked);
            } else {
                this.setImageResource(R.drawable.png_check_off);
            }
        }
        this.invalidate();
    }

    public void setDrawable(int idDrawableOn, int idDrawableOff, Context context){
        if(idDrawableOn != -1) {
            m_nDrawableChecked = ContextCompat.getDrawable(context, idDrawableOn);
            m_nDrawableChecked.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        } else {
            m_nDrawableChecked = null;
        }

        if(idDrawableOff != -1) {
            m_nDrawableNoChecked = ContextCompat.getDrawable(context, idDrawableOff);
            m_nDrawableNoChecked.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        } else {
            m_nDrawableNoChecked = null;
        }

        setChecked(m_bChecked);
    }

    public void changeCheckState(){
        setChecked(!m_bChecked);
    }
}
