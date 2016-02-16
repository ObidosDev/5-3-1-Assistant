package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.BodyWeightData;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.AddBodyWeightDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.InfoDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;

/**
 * Created by vobideyko on 8/18/15.
 */
public class WeightChartActivity extends BaseActivity implements OnChartValueSelectedListener, View.OnClickListener {

    private BarChart mChart;
    private ImageView m_ivDelete, m_ivInfo;
    private ArrayList<BodyWeightData> m_bodyWeightDataArrayList;
    private int m_nWinter, m_nSpring, m_nSummer, m_nAutumn;
    private FloatingActionButton m_fabAddBodyWeight;
    private ImageView m_ivBack;
    private int m_nIndexWeightData=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_chart);
        setMediumFont(findViewById(R.id.tvTitleActivity));

        m_ivBack = (ImageView) findViewById(R.id.ivBack);
        m_ivBack.setOnClickListener(this);

        m_fabAddBodyWeight = (FloatingActionButton) findViewById(R.id.fabAddBodyWeight);
        m_fabAddBodyWeight.setOnClickListener(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) m_fabAddBodyWeight.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            m_fabAddBodyWeight.setLayoutParams(p);
        }
        m_fabAddBodyWeight.hide();

        m_nWinter = getResources().getColor(R.color.winter);
        m_nSpring = getResources().getColor(R.color.spring);
        m_nSummer = getResources().getColor(R.color.summer);
        m_nAutumn = getResources().getColor(R.color.autumn);

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
        m_fabAddBodyWeight.postDelayed(new Runnable() {
            @Override
            public void run() {
                m_fabAddBodyWeight.show();
            }
        }, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_fabAddBodyWeight.hide();
    }

    private void initChart(){
        mChart = (BarChart) findViewById(R.id.chartWeight);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        mChart.setMaxVisibleValueCount(11);

        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);
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
        mChart.setNoDataTextDescription(getResources().getString(R.string.tap_to_add_new_body_weight));
        mChart.setDescriptionTextSize(50f);
        mChart.setDescriptionColor(getResources().getColor(R.color.primary_text));
    }

    private void setData() {
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_bodyWeightDataArrayList = databaseHandler.getAllBodyWeights();
        databaseHandler.close();
        mChart.clear();

        ArrayList<String> xVals = new ArrayList<String>();
        for (BodyWeightData bodyWeightData : m_bodyWeightDataArrayList) {
            xVals.add(bodyWeightData.getStrDate());
        }

        ArrayList<BarEntry> yValsYear = new ArrayList<BarEntry>();

        for (int i = 0; i < m_bodyWeightDataArrayList.size(); i++) {
            yValsYear.add(new BarEntry((float) m_bodyWeightDataArrayList.get(i).getWeight(), i));
        }

        BarDataSet weightSet = new BarDataSet(yValsYear, "Weight");
        weightSet.setBarSpacePercent(20f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();

        ArrayList<Integer> colorsArrayList = new ArrayList<>();
        for (BodyWeightData bodyWeightData : m_bodyWeightDataArrayList) {
            int month = Integer.valueOf(bodyWeightData.getStrDate().split("\\.")[1]);
            switch (month){
                case 12:
                case 1:
                case 2:
                    colorsArrayList.add(m_nWinter);
                    break;
                case 3:
                case 4:
                case 5:
                    colorsArrayList.add(m_nSpring);
                    break;
                case 6:
                case 7:
                case 8:
                    colorsArrayList.add(m_nSummer);
                    break;
                case 9:
                case 10:
                case 11:
                    colorsArrayList.add(m_nAutumn);
                    break;
            }
        }
        if(colorsArrayList.isEmpty()){
            colorsArrayList.add(m_nWinter);
        }
        weightSet.setColors(colorsArrayList);
        dataSets.add(weightSet);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(13f);
        data.setValueTypeface(getTypeFace());
        data.setGroupSpace(0f);

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
        if(m_nIndexWeightData!=-1){
            AddBodyWeightDialog addBodyWeightDialog = new AddBodyWeightDialog(WeightChartActivity.this,m_bodyWeightDataArrayList.get(m_nIndexWeightData));
            addBodyWeightDialog.show();
            m_nIndexWeightData=-1;
        }
        m_ivDelete.setVisibility(View.GONE);
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivInfo:
                InfoDialog infoDialog = new InfoDialog(WeightChartActivity.this,R.string.text_tip_how_use_chart);
                infoDialog.show();
                break;
            case R.id.ivDelete:
                QuestionDialog questionDialog = new QuestionDialog(WeightChartActivity.this,
                        getResources().getString(R.string.question_delete_record),
                        new Runnable() {
                            @Override
                            public void run() {
                                DatabaseHelper databaseHandler = new DatabaseHelper(WeightChartActivity.this);
                                databaseHandler.deleteBodyWeight(m_bodyWeightDataArrayList.get(m_nIndexWeightData).getId());
                                databaseHandler.close();
                                m_nIndexWeightData = -1;
                                refreshChartData();
                                m_ivDelete.setVisibility(View.GONE);
                            }
                        });
                questionDialog.show();
                break;
            case R.id.fabAddBodyWeight:
                AddBodyWeightDialog addBodyWeightDialog = new AddBodyWeightDialog(WeightChartActivity.this);
                addBodyWeightDialog.show();
                break;
        }
    }
}