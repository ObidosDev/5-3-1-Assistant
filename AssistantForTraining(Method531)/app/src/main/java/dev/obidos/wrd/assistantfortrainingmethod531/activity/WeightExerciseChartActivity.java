package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseWeightData;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.InfoDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;

/**
 * Created by vobideyko on 8/18/15.
 */
public class WeightExerciseChartActivity extends BaseActivity implements OnChartValueSelectedListener, View.OnClickListener {

    private LineChart mChart;
    private ImageView m_ivDelete, m_ivInfo;
    private ArrayList<ExerciseWeightData> m_exerciseWeightDataArrayList;
    private ImageView m_ivBack;
    private int m_nIndexWeightData=-1;
    private TextView m_tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_exercise_chart);

        m_tvTitle = (TextView) findViewById(R.id.tvTitleActivity);
        setMediumFont(m_tvTitle);
        m_tvTitle.setText(formatName(getString(R.string.exercise_chart_progress) + " '" +getIntent().getStringExtra("name") +"'",getResources().getInteger(R.integer.exercise_chart_title_length)));

        m_ivBack = (ImageView) findViewById(R.id.ivBack);
        m_ivBack.setOnClickListener(this);

        initChart();

        m_ivDelete = (ImageView) findViewById(R.id.ivDelete);
        m_ivDelete.setOnClickListener(this);

        m_ivInfo = (ImageView) findViewById(R.id.ivInfo);
        m_ivInfo.setOnClickListener(this);
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
        xAxis.setAxisLineColor(getResources().getColor(R.color.divider));
        xAxis.setTextColor(getResources().getColor(R.color.primary_text));

        mChart.getAxisLeft().setDrawGridLines(true);

        mChart.getAxisLeft().setTypeface(getTypeFace());
        mChart.getAxisLeft().setTextSize(10f);
        mChart.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.divider));
        mChart.getAxisLeft().setTextColor(getResources().getColor(R.color.primary_text));
        mChart.getAxisLeft().setStartAtZero(false);

        mChart.getAxisRight().setTypeface(getTypeFace());
        mChart.getAxisRight().setTextSize(10f);
        mChart.getAxisRight().setAxisLineColor(getResources().getColor(R.color.divider));
        mChart.getAxisRight().setTextColor(getResources().getColor(R.color.primary_text));
        mChart.getAxisRight().setStartAtZero(false);

        mChart.getLegend().setEnabled(false);
        mChart.setNoDataText("");
        mChart.setNoDataTextDescription(getResources().getString(R.string.add_new_exercise_weight));
        mChart.setDescriptionTextSize(50f);
        mChart.setDescriptionColor(getResources().getColor(R.color.primary_text));
    }

    private void setData() {
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_exerciseWeightDataArrayList = databaseHandler.getAllExerciseChartWeights(getIntent().getIntExtra("id",-1));
        databaseHandler.close();
        mChart.clear();

        ArrayList<String> xVals = new ArrayList<String>();
        for (ExerciseWeightData exerciseWeightData : m_exerciseWeightDataArrayList) {
            xVals.add(exerciseWeightData.getStrDate());
        }

        ArrayList<Entry> yValsYear = new ArrayList<Entry>();

        for (int i = 0; i < m_exerciseWeightDataArrayList.size(); i++) {
            yValsYear.add(new BarEntry((float) m_exerciseWeightDataArrayList.get(i).getWeight(), i));
        }

        LineDataSet weightSet = new LineDataSet(yValsYear, "Weight");

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        ArrayList<Integer> colorsArrayList = new ArrayList<>();
        colorsArrayList.add(getResources().getColor(R.color.accent));

        weightSet.setColors(colorsArrayList);
        dataSets.add(weightSet);

        LineData data = new LineData(xVals, dataSets);
        data.setValueTextSize(13f);
        data.setValueTypeface(getTypeFace());

        mChart.setData(data);
        mChart.invalidate();
    }

    public void refreshChartData(){
        setData();
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null) {
            return;
        }

        m_nIndexWeightData = e.getXIndex();
        m_ivDelete.setVisibility(View.VISIBLE);
    }

    public void onNothingSelected() {
        m_ivDelete.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivInfo:
                InfoDialog infoDialog = new InfoDialog(WeightExerciseChartActivity.this,R.string.text_tip_how_use_chart_exercise);
                infoDialog.show();
                break;
            case R.id.ivDelete:
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
                                m_ivDelete.setVisibility(View.GONE);
                            }
                        });
                questionDialog.show();
                break;
        }
    }
}