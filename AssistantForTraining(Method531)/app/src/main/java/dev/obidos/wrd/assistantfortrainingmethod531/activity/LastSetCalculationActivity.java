package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseWeightData;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.InfoDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.tools.DateConverter;
import dev.obidos.wrd.assistantfortrainingmethod531.tools.TrainingConstants;

/**
 * Created by vobideyko on 8/18/15.
 */
public class LastSetCalculationActivity extends BaseActivity implements View.OnClickListener {

    private static final int MENU_SAVE = 1;

    private Toolbar mToolbar;

    private TextView m_tvAimWeight, m_tvCalcWeightLastSet, m_tvPercentage, m_tvWeightLastSet;
    private EditText m_edtRepeatCount;
    private int m_nExerciseId = -2;
    private TextInputLayout m_tilReps;
    private double m_fWeightLastSetCalc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_set_calculation);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(R.string.title_activity_last_set);

        Drawable drawableIconNavigation = ContextCompat.getDrawable(this, R.drawable.svg_close);
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

        m_nExerciseId = getIntent().getIntExtra(TrainingConstants.EXTRA_ID_EXERCISE, -2);
        if(m_nExerciseId==-2){
            finish();
        }

        m_tilReps = (TextInputLayout) findViewById(R.id.tilRepsCountLastSet);

        m_tvAimWeight = (TextView) findViewById(R.id.tvAimWeight);
        m_tvAimWeight.setText(getIntent().getStringExtra(TrainingConstants.EXTRA_AIM_WEIGHT_EXERCISE));

        m_tvCalcWeightLastSet = (TextView) findViewById(R.id.tvCalcWeightLastSet);
        m_tvPercentage = (TextView) findViewById(R.id.tvPercent);

        m_tvWeightLastSet = (TextView) findViewById(R.id.tvWeightLastSet);
        m_tvWeightLastSet.setText(getIntent().getStringExtra(TrainingConstants.EXTRA_LAST_SET_WEIGHT_EXERCISE));

        m_edtRepeatCount = (EditText) findViewById(R.id.edtCountRepsLastSet);
        m_edtRepeatCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strEnter = s.toString();
                if (strEnter.length() != 0 && Integer.valueOf(strEnter) != 0) {
                    calcLastSetResult();
                } else {
                    m_tvCalcWeightLastSet.setText("0");
                    m_tvPercentage.setText("0%");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        init();
    }

    private void init(){
        removeErrorFromTIL(m_tilReps);

        setRegularFont(m_edtRepeatCount);
        setRegularFont(findViewById(R.id.tvRepsLastSetLabel));
        setRegularFont(findViewById(R.id.tvCalcWeightLastSetLabel));
        setRegularFont(m_tvCalcWeightLastSet);
        setRegularFont(findViewById(R.id.tvAimWeightLabel));
        setRegularFont(m_tvAimWeight);
        setRegularFont(findViewById(R.id.tvPercentLabel));
        setRegularFont(m_tvPercentage);
        setRegularFont(findViewById(R.id.tvWeightLastSetLabel));
        setRegularFont(m_tvWeightLastSet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();

        MenuItem itemSave = menu.add(1, MENU_SAVE, Menu.NONE, R.string.menu_save);

        Drawable drawableIcon = ContextCompat.getDrawable(this, R.drawable.svg_save);
        drawableIcon.setColorFilter(this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        itemSave.setIcon(drawableIcon);
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSave.setShortcut('1', 'b');

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case MENU_SAVE:
                if(checkRepeatCountIsValid()) {
                    saveValueToBd();
                    finish();
                } else {
                    openKeyboard(m_edtRepeatCount);
                }
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
    }

    private void saveValueToBd() {
        ExerciseWeightData exerciseWeightData = new ExerciseWeightData();
        exerciseWeightData.setIdExercise(m_nExerciseId);
        DateConverter dateConverter = new DateConverter();
        dateConverter.setDate(Calendar.getInstance());
        exerciseWeightData.setStrDate(dateConverter.getStrDate());
        exerciseWeightData.setWeight(m_fWeightLastSetCalc);
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        databaseHandler.addExerciseChartWeight(exerciseWeightData);
        databaseHandler.close();
    }

    private boolean checkRepeatCountIsValid() {
        String strRepeatCount = getTextFromTextView(m_edtRepeatCount);
        if(strRepeatCount.length()==0){
            m_tilReps.setError(getResources().getString(R.string.toast_enter_repeat_count));
            return false;
        }
        removeErrorFromTIL(m_tilReps);
        return true;
    }

    protected String getTextFromTextView(TextView textView){
        return textView.getText().toString().trim();
    }

    private void calcLastSetResult(){
        String strWorkWeight = m_tvWeightLastSet.getText().toString();
        String strAimWeight = m_tvAimWeight.getText().toString();
        String strRepsCount = m_edtRepeatCount.getText().toString();

        BigDecimal calcWeight = new BigDecimal(strWorkWeight);
        BigDecimal coef = new BigDecimal(strRepsCount);
        coef = (coef.multiply(new BigDecimal(TrainingConstants.SPECIAL_CONST_FOR_RM_CALCULATION))).add(BigDecimal.ONE);

        //coef = coef.multiply(new BigDecimal("0.9"));//if add it than we obtain TM (Training maximum) instead RM (Rep maximum)

        calcWeight = calcWeight.multiply(coef);
        calcWeight = calcWeight.setScale(2, RoundingMode.HALF_UP);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        m_tvCalcWeightLastSet.setText(df.format(calcWeight).replace(",", "."));
        m_fWeightLastSetCalc = Double.valueOf(df.format(calcWeight).replace(",", "."));

        calcWeight = calcWeight.multiply(new BigDecimal("100"));
        if(!strAimWeight.equals("0") && Double.valueOf(strAimWeight)!=0) {
            calcWeight = calcWeight.divide(new BigDecimal(strAimWeight), 2, RoundingMode.HALF_DOWN);
        }
        m_tvPercentage.setText(calcWeight.toString()+"%");
    }

    private void removeErrorFromTIL(TextInputLayout textInputLayout){
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError("sds");
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }
}
