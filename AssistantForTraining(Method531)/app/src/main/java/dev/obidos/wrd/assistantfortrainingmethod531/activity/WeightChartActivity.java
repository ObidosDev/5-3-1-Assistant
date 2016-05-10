package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.BodyWeightData;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.AddBodyWeightDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.InfoDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.tools.TrainingConstants;

/**
 * Created by vobideyko on 8/18/15.
 */
public class WeightChartActivity extends BaseActivity implements OnChartValueSelectedListener, View.OnClickListener {

    private static final int MENU_DELETE = 1;
    private static final int MENU_HELP = 2;

    private Toolbar mToolbar;

    private BarChart mChart;
    private ArrayList<BodyWeightData> m_bodyWeightDataArrayList;
    private int m_nWinter, m_nSpring, m_nSummer, m_nAutumn;
    private FloatingActionButton m_fabAddBodyWeight;
    private int m_nIndexWeightData=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_chart);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(R.string.weight_chart_title);

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
                InfoDialog infoDialog = new InfoDialog(WeightChartActivity.this,R.string.text_tip_how_use_chart);
                infoDialog.show();
                break;
            case MENU_DELETE:
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
        mChart.setNoDataTextDescription(getResources().getString(R.string.tap_to_add_new_body_weight));
        mChart.setDescriptionTextSize(50f);
        mChart.setDescriptionColor(getResources().getColor(R.color.colorPrimaryText));
    }

    private void setData() {
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_bodyWeightDataArrayList = databaseHandler.getAllBodyWeights();
        databaseHandler.close();
        mChart.clear();

        ArrayList<String> xValues = new ArrayList<>();
        for (BodyWeightData bodyWeightData : m_bodyWeightDataArrayList) {
            xValues.add(bodyWeightData.getStrDate());
        }

        ArrayList<BarEntry> yValuesYear = new ArrayList<>();

        for (int i = 0; i < m_bodyWeightDataArrayList.size(); i++) {
            yValuesYear.add(new BarEntry((float) m_bodyWeightDataArrayList.get(i).getWeight(), i));
        }

        BarDataSet weightSet = new BarDataSet(yValuesYear, "Weight");
        weightSet.setBarSpacePercent(10f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();

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

        BarData data = new BarData(xValues, dataSets);
        data.setValueTextSize(13f);
        data.setValueTypeface(getTypeFace());
        data.setGroupSpace(0f);

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
        if(m_nIndexWeightData!=-1){
            AddBodyWeightDialog addBodyWeightDialog = new AddBodyWeightDialog(WeightChartActivity.this,m_bodyWeightDataArrayList.get(m_nIndexWeightData));
            addBodyWeightDialog.show();
            m_nIndexWeightData=-1;
        }

        mToolbar.getMenu().findItem(MENU_DELETE).setVisible(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabAddBodyWeight:
                AddBodyWeightDialog addBodyWeightDialog = new AddBodyWeightDialog(WeightChartActivity.this);
                addBodyWeightDialog.show();
                break;
        }
    }
}