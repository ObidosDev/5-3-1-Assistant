package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Calendar;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseData;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.ColorPickerDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.InfoDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.TimerDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.tools.DateConverter;
import dev.obidos.wrd.assistantfortrainingmethod531.tools.TrainingConstants;

/**
 * Created by vobideyko on 8/18/15.
 */
public class AddExerciseActivity extends BaseActivity implements View.OnClickListener {

    private static final int MENU_SAVE = 1;
    private static final int MENU_HELP = 2;

    public static final int NEW_EXERCISE = -2;

    private Toolbar mToolbar;

    private ImageView m_ivColorChange;
    private EditText m_edtName, m_edtWeight, m_edtRepeatCount, m_edtAimWeight;
    private RadioButton m_rbtnTop, m_rbtnBottom;
    private int m_nExerciseId = -2;
    private boolean m_bFromInfoActivity = false;
    private ExerciseData m_exerciseData;
    private TextInputLayout m_tilName, m_tilWeight, m_tilReps, m_tilAimWeight;
    private int m_nStartColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(R.string.title_new_exercise);

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

        m_tilName = (TextInputLayout) findViewById(R.id.tilName);
        m_tilWeight = (TextInputLayout) findViewById(R.id.tilWeight);
        m_tilReps = (TextInputLayout) findViewById(R.id.tilReps);
        m_tilAimWeight = (TextInputLayout) findViewById(R.id.tilAimWeight);

        removeErrorFromTIL(m_tilName);
        removeErrorFromTIL(m_tilWeight);
        removeErrorFromTIL(m_tilReps);
        removeErrorFromTIL(m_tilAimWeight);

        m_nExerciseId = getIntent().getIntExtra(TrainingConstants.EXTRA_ID_EXERCISE, NEW_EXERCISE);

        m_ivColorChange = (ImageView) findViewById(R.id.ivColorChange);
        m_ivColorChange.setOnClickListener(this);

        m_edtName = (EditText) findViewById(R.id.edtName);
        m_edtWeight = (EditText) findViewById(R.id.edtWeight);
        m_edtRepeatCount = (EditText) findViewById(R.id.edtCount);
        m_edtAimWeight = (EditText) findViewById(R.id.edtAimWeight);

        m_rbtnTop = (RadioButton) findViewById(R.id.rbtnTop);
        m_rbtnBottom = (RadioButton) findViewById(R.id.rbtnBottom);

        m_exerciseData = new ExerciseData();
        if(m_nExerciseId!= NEW_EXERCISE){
            m_bFromInfoActivity = getIntent().getBooleanExtra(TrainingConstants.EXTRA_FROM_INFO_EXERCISE_ACTIVITY, false);
            DatabaseHelper databaseHandler = new DatabaseHelper(this);
            m_exerciseData = databaseHandler.getExercise(m_nExerciseId);
            databaseHandler.close();
            m_edtName.setText(m_exerciseData.getName());
            m_edtWeight.setText(String.valueOf(roundWeight(m_exerciseData.getWeight() / 0.9 / 1.0333)));
            m_edtAimWeight.setText(m_exerciseData.getAimWeight());
            m_edtRepeatCount.setText("1");
            if(m_exerciseData.getType()==ExerciseData.PART_TOP){
                m_rbtnTop.setChecked(true);
            } else {
                m_rbtnBottom.setChecked(true);
            }
            getSupportActionBar().setTitle(R.string.title_edit_exercise);
            setColorExercise(m_exerciseData.getColorNumber());
            m_nStartColor = m_exerciseData.getColorNumber();
        }

        init();
    }

    private void init(){
        setRegularFont(m_edtRepeatCount);
        setRegularFont(m_edtWeight);
        setRegularFont(m_edtAimWeight);
        setRegularFont(m_edtName);
        setRegularFont(m_rbtnTop);
        setRegularFont(m_rbtnBottom);

        setRegularFont(findViewById(R.id.tvX));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();

        MenuItem itemHelp = menu.add(0, MENU_HELP, Menu.NONE, R.string.menu_help);
        MenuItem itemSave = menu.add(1, MENU_SAVE, Menu.NONE, R.string.menu_save);

        Drawable drawableIcon = ContextCompat.getDrawable(this, R.drawable.svg_help);
        drawableIcon.setColorFilter(this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        itemHelp.setIcon(drawableIcon);
        itemHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemHelp.setShortcut('0', 'a');

        drawableIcon = ContextCompat.getDrawable(this, R.drawable.svg_save);
        drawableIcon.setColorFilter(this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        itemSave.setIcon(drawableIcon);
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSave.setShortcut('1', 'b');

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case MENU_HELP:
                InfoDialog infoDialog = new InfoDialog(AddExerciseActivity.this,R.string.text_tip_how_fill_out_the_form);
                infoDialog.show();
                break;
            case MENU_SAVE:
                saveExerciseAndExit();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(checkChanges()
                && (getTextFromTextView(m_edtName).length()!=0
                    || getTextFromTextView(m_edtWeight).length()!=0
                    || getTextFromTextView(m_edtRepeatCount).length()!=0
                    || getTextFromTextView(m_edtAimWeight).length()!=0)) {
            QuestionDialog questionDialog;
            String strMessage;
            if(m_nExerciseId==NEW_EXERCISE) {
                strMessage = getResources().getString(R.string.question_do_not_save);
            } else {
                strMessage = getResources().getString(R.string.question_do_not_save_changes);
            }
            questionDialog = new QuestionDialog(this, strMessage,
                    new Runnable() {
                        @Override
                        public void run() {
                            saveExerciseAndExit();
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            returnToInfoActivityIfNeed();
                            finish();
                        }
                    });
            questionDialog.show();
        } else {
            if(m_nExerciseId!=NEW_EXERCISE){
                returnToInfoActivityIfNeed();
                finish();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivColorChange:
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this);
                colorPickerDialog.show();
                break;
        }
    }

    private void saveExerciseAndExit(){
        if(checkValuesInEdt()) {
            if(m_nExerciseId==-2) {
                m_exerciseData.setName(getTextFromTextView(m_edtName));

                double weight = Double.valueOf(getTextFromTextView(m_edtWeight));
                int count = Integer.valueOf(getTextFromTextView(m_edtRepeatCount));
                double calcWeight = ((weight * count * 0.0333) + weight) * 0.9;
                m_exerciseData.setWeight(roundWeight(calcWeight));

                m_exerciseData.setAimWeight(getTextFromTextView(m_edtAimWeight));

                if (m_rbtnTop.isChecked()) {
                    m_exerciseData.setType(ExerciseData.PART_TOP);
                } else {
                    m_exerciseData.setType(ExerciseData.PART_BOTTOM);
                }

                setStartDateForExercise(m_exerciseData);

                DatabaseHelper databaseHandler = new DatabaseHelper(this);
                if (databaseHandler.getAllExercisesByStatus(DatabaseHelper.STATUS_NORMAL).size() == 0) {//if we do not have any exercises
                    setWeekOfCycle(0);
                    DateConverter culc = new DateConverter();
                    culc.setDate(Calendar.getInstance());
                    setStrDateStartOfCycle(culc.getStrDate());
                }
                databaseHandler.addExercise(m_exerciseData);
                databaseHandler.close();
            } else {
                m_exerciseData.setName(getTextFromTextView(m_edtName));
                double weight = Double.valueOf(getTextFromTextView(m_edtWeight));
                int count = Integer.valueOf(getTextFromTextView(m_edtRepeatCount));
                double calcWeight = ((weight * count * 0.0333) + weight) * 0.9;
                m_exerciseData.setWeight(roundWeight(calcWeight));
                m_exerciseData.setAimWeight(getTextFromTextView(m_edtAimWeight));
                if (m_rbtnTop.isChecked()) {
                    m_exerciseData.setType(ExerciseData.PART_TOP);
                } else {
                    m_exerciseData.setType(ExerciseData.PART_BOTTOM);
                }
                DatabaseHelper databaseHandler = new DatabaseHelper(this);
                databaseHandler.updateExercise(m_exerciseData);
                databaseHandler.close();
            }
            returnToInfoActivityIfNeed();
            finish();
        }
    }

    private void returnToInfoActivityIfNeed(){
        if(m_bFromInfoActivity){
            Intent intent = new Intent(AddExerciseActivity.this, ExerciseInfoActivity.class);
            intent.putExtra(TrainingConstants.EXTRA_ID_EXERCISE, m_nExerciseId);
            startActivity(intent);
        }
    }

    public void setColorExercise(int nColorNumber){
        m_exerciseData.setColorNumber(nColorNumber);
        int[] markerColors = getResources().getIntArray(R.array.marker_colors);
        m_ivColorChange.setBackgroundColor(markerColors[nColorNumber]);
    }

    private boolean checkValuesInEdt(){
        boolean aimWeight = checkAimWeightIsValid();
        if(!aimWeight){
            m_edtAimWeight.requestFocus();
        }
        boolean count = checkRepeatCountIsValid();
        if(!count){
            m_edtRepeatCount.requestFocus();
        }
        boolean weight = checkWeightIsValid();
        if(!weight){
            m_edtWeight.requestFocus();
        }
        boolean name = checkNameIsValid();
        if(!name){
            m_edtName.requestFocus();
        }
        if(!name){
            openKeyboard(m_edtName);
        } else if(!weight) {
            openKeyboard(m_edtWeight);
        } else if(!count) {
            openKeyboard(m_edtRepeatCount);
        } else if(!aimWeight) {
            openKeyboard(m_edtAimWeight);
        }
        return (name && weight && count && aimWeight);
    }

    private boolean checkNameIsValid(){
        String strName = getTextFromTextView(m_edtName);
        if(strName.length()==0){
            m_tilName.setError(getResources().getString(R.string.toast_enter_name));
            return false;
        }
        removeErrorFromTIL(m_tilName);
        return true;
    }

    private boolean checkWeightIsValid(){
        String strWeight = getTextFromTextView(m_edtWeight);
        if(strWeight.length()==0){
            m_tilWeight.setError(getResources().getString(R.string.toast_enter_weight));
            return false;
        }
        double fWeight = Double.valueOf(getTextFromTextView(m_edtWeight));
        if(fWeight<=0){
            m_tilWeight.setError(getResources().getString(R.string.toast_weight_bigger_than_zero));
            return false;
        }
        removeErrorFromTIL(m_tilWeight);
        return true;
    }

    private boolean checkAimWeightIsValid(){
        String strWeight = getTextFromTextView(m_edtAimWeight);
        if(strWeight.length()==0){
            m_tilAimWeight.setError(getResources().getString(R.string.toast_enter_aim_weight));
            return false;
        }
        double fWeight = Double.valueOf(getTextFromTextView(m_edtAimWeight));
        if(fWeight<=0){
            m_tilAimWeight.setError(getResources().getString(R.string.toast_weight_bigger_than_zero));
            return false;
        }
        removeErrorFromTIL(m_tilAimWeight);
        return true;
    }

    private boolean checkRepeatCountIsValid(){
        String strRepeatCount = getTextFromTextView(m_edtRepeatCount);
        if(strRepeatCount.length()==0){
            m_tilReps.setError(getResources().getString(R.string.toast_enter_repeat_count));
            return false;
        }
        int fRepeatCount = Integer.valueOf(strRepeatCount);
        if(fRepeatCount<=0){
            m_tilReps.setError(getResources().getString(R.string.toast_repeat_count_bigger_than_zero));
            return false;
        }
        removeErrorFromTIL(m_tilReps);
        return true;
    }

    private boolean checkChanges(){
        if(m_nExerciseId>=0) {
            if (!getTextFromTextView(m_edtName).equals(m_exerciseData.getName())) {
                return true;
            }
            if (!getTextFromTextView(m_edtWeight).equals(String.valueOf(roundWeight(m_exerciseData.getWeight() / 0.9 / 1.0333)))) {
                return true;
            }
            if (!getTextFromTextView(m_edtRepeatCount).equals("1")) {
                return true;
            }
            if (!getTextFromTextView(m_edtAimWeight).equals(m_exerciseData.getAimWeight())) {
                return true;
            }
            if (m_exerciseData.getType() == ExerciseData.PART_TOP) {
                if (!m_rbtnTop.isChecked()) {
                    return true;
                }
            } else {
                if (!m_rbtnBottom.isChecked()) {
                    return true;
                }
            }

            if(m_nStartColor!=m_exerciseData.getColorNumber()){
                return true;
            }

            return false;
        } else {
            return true;
        }
    }

    private void setStartDateForExercise(ExerciseData exerciseData){
        int numberWeekInCycle = getWeekOfCycle();
        if(isSixWeekCycle()){
            if (numberWeekInCycle != 2 && numberWeekInCycle < 5 ) {
                exerciseData.setNumberCycle(1);
            } else {
                exerciseData.setNumberCycle(0);
            }
        } else {
            if (numberWeekInCycle < 2) {
                exerciseData.setNumberCycle(1);
            } else {
                exerciseData.setNumberCycle(0);
            }
        }
    }

    protected String getTextFromTextView(TextView textView){
        return textView.getText().toString().trim();
    }

    private double roundWeight(double weight){
        long factor = (long) Math.pow(10, 2);
        weight = weight * factor;
        long tmp = Math.round(weight);
        return (double) tmp / factor;
    }

    private void removeErrorFromTIL(TextInputLayout textInputLayout){
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError("sds");
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }
}
