package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseWeightData;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.InfoDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.tools.TrainingConstants;

/**
 * Created by vobideyko on 8/18/15.
 */
public class WeightExerciseChartActivity extends BaseActivity implements OnChartValueSelectedListener, View.OnClickListener {

    private static final int MENU_DELETE = 1;
    private static final int MENU_HELP = 2;

    private Toolbar mToolbar;

    private LineChart mChart;
    private ArrayList<ExerciseWeightData> m_exerciseWeightDataArrayList;
    private int m_nIndexWeightData=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_exercise_chart);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(getString(R.string.exercise_chart_progress) + " '" +getIntent().getStringExtra(TrainingConstants.EXTRA_NAME_EXERCISE) +"'");

        Drawable drawableIconNavigation = ContextCompat.getDrawable(this, R.drawable.svg_back);
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

        initChart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();

        MenuItem itemDelete = menu.add(1, MENU_DELETE, Menu.NONE, R.string.menu_save);
        itemDelete.setVisible(false);
        MenuItem itemHelp = menu.add(0, MENU_HELP, Menu.NONE, R.string.menu_help);

        Drawable drawableIcon = ContextCompat.getDrawable(this, R.drawable.svg_delete);
        drawableIcon.setColorFilter(this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        itemDelete.setIcon(drawableIcon);
        itemDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemDelete.setShortcut('0', 'a');

        drawableIcon = ContextCompat.getDrawable(this, R.drawable.svg_help);
        drawableIcon.setColorFilter(this.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        itemHelp.setIcon(drawableIcon);
        itemHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemHelp.setShortcut('1', 'b');

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case MENU_HELP:
                InfoDialog infoDialog = new InfoDialog(WeightExerciseChartActivity.this,R.string.text_tip_how_use_chart_exercise);
                infoDialog.show();
                break;
            case MENU_DELETE:
                QuestionDialog questionDialog = new QuestionDialog(WeightExerciseChartActivity.this,
                        getResources().getString(R.string.question_delete_record),
                        new Runnable() {
                            @Override
                            public void run() {
                                DatabaseHelper databaseHandler = new DatabaseHelper(WeightExerciseChartActivity.this);
                                databaseHandler.deleteExerciseChartWeight(m_exerciseWeightDataArrayList.get(m_nIndexWeightData).getId());
                                databaseHandler.close();
                                m_nIndexWeightData = -1;
                                refreshChartData();

                                mToolbar.getMenu().findItem(MENU_DELETE).setVisible(false);
                            }
                        });
                questionDialog.show();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void initChart(){
        mChart = (LineChart) findViewById(R.id.chartWeight);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(13);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(13f);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);
        xAxis.setTypeface(getTypeFace());
        xAxis.setAxisLineColor(getResources().getColor(R.color.colorDivider));
        xAxis.setTextColor(getResources().getColor(R.color.colorPrimaryText));

        mChart.getAxisLeft().setDrawGridLines(true);

        mChart.getAxisLeft().setTypeface(getTypeFace());
        mChart.getAxisLeft().setTextSize(10f);
        mChart.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.colorDivider));
        mChart.getAxisLeft().setTextColor(getResources().getColor(R.color.colorPrimaryText));
        mChart.getAxisLeft().setStartAtZero(false);

        mChart.getAxisRight().setTypeface(getTypeFace());
        mChart.getAxisRight().setTextSize(10f);
        mChart.getAxisRight().setAxisLineColor(getResources().getColor(R.color.colorDivider));
        mChart.getAxisRight().setTextColor(getResources().getColor(R.color.colorPrimaryText));
        mChart.getAxisRight().setStartAtZero(false);

        mChart.getLegend().setEnabled(false);
        mChart.setNoDataText("");
        mChart.setNoDataTextDescription(getResources().getString(R.string.add_new_exercise_weight));
        mChart.setDescriptionTextSize(50f);
        mChart.setDescriptionColor(getResources().getColor(R.color.colorPrimaryText));
    }

    private void setData() {
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_exerciseWeightDataArrayList = databaseHandler.getAllExerciseChartWeights(getIntent().getIntExtra(TrainingConstants.EXTRA_ID_EXERCISE,-1));
        databaseHandler.close();
        mChart.clear();

        ArrayList<String> xValues = new ArrayList<>();
        for (ExerciseWeightData exerciseWeightData : m_exerciseWeightDataArrayList) {
            xValues.add(exerciseWeightData.getStrDate());
        }

        ArrayList<Entry> yValuesYear = new ArrayList<>();

        for (int i = 0; i < m_exerciseWeightDataArrayList.size(); i++) {
            yValuesYear.add(new BarEntry((float) m_exerciseWeightDataArrayList.get(i).getWeight(), i));
        }

        LineDataSet weightSet = new LineDataSet(yValuesYear, "Weight");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        ArrayList<Integer> colorsArrayList = new ArrayList<>();
        colorsArrayList.add(getResources().getColor(R.color.colorAccent));

        weightSet.setColors(colorsArrayList);
        dataSets.add(weightSet);

        LineData data = new LineData(xValues, dataSets);
        data.setValueTextSize(13f);
        data.setValueTypeface(getTypeFace());

        mChart.setData(data);
        mChart.invalidate();
    }

    public void refreshChartData(){
        setData();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null) {
            return;
        }

        m_nIndexWeightData = e.getXIndex();

        mToolbar.getMenu().findItem(MENU_DELETE).setVisible(true);
    }

    public void onNothingSelected() {
        mToolbar.getMenu().findItem(MENU_DELETE).setVisible(false);
    }

    @Override
    public void onClick(View v) {

    }
}