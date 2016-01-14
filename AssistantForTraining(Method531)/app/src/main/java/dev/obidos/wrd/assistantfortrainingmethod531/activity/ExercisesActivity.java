package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.adapter.ExercisesRecyclerViewAdapter;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseData;
import dev.obidos.wrd.assistantfortrainingmethod531.views.DividerItemDecoration;

/**
 * Created by vobideyko on 8/14/15.
 */
public class ExercisesActivity extends BaseActivity implements DialogInterface.OnDismissListener, View.OnClickListener{

    private RecyclerView m_exercisesRecyclerView;
    private ExercisesRecyclerViewAdapter m_exercisesRecyclerViewAdapter;
    private ImageView m_ivBodyWeight, m_ivSettings;
    private TextView m_tvSubTitleWeek;
    private TextView m_tvEmpty;
    private ArrayList<ExerciseData> m_exerciseDataArrayList;
    private FloatingActionButton m_fabAddExercise;

    private int m_nStatusOfExercises = DatabaseHelper.STATUS_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_activity);

        m_fabAddExercise = (FloatingActionButton) findViewById(R.id.fabAddExercise);
        m_fabAddExercise.setOnClickListener(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) m_fabAddExercise.getLayoutParams();
            p.setMargins(0, 0, 0, 0); // get rid of margins since shadow area is now the margin
            m_fabAddExercise.setLayoutParams(p);
        }

        m_tvSubTitleWeek = (TextView) findViewById(R.id.tvWeek);
        m_tvEmpty = (TextView) findViewById(R.id.tvEmpty);


        m_exercisesRecyclerView = (RecyclerView) findViewById(R.id.exercisesRecyclerView);
        m_exercisesRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager m_LayoutManager = new LinearLayoutManager(this);
        m_exercisesRecyclerView.setLayoutManager(m_LayoutManager);
        m_exerciseDataArrayList = new ArrayList<>();
        m_exercisesRecyclerViewAdapter = new ExercisesRecyclerViewAdapter(m_exerciseDataArrayList, this);
        m_exercisesRecyclerView.setAdapter(m_exercisesRecyclerViewAdapter);

        m_exercisesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                m_exercisesRecyclerViewAdapter.closeMenu(true);
            }
        });
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        m_exercisesRecyclerView.addItemDecoration(itemDecoration);

        m_ivBodyWeight = (ImageView) findViewById(R.id.ivBodyWeight);
        m_ivBodyWeight.setOnClickListener(this);

        m_ivSettings = (ImageView) findViewById(R.id.ivSettings);
        m_ivSettings.setOnClickListener(this);

        m_fabAddExercise.hide();

        init();
    }

    private void init(){
        setMediumFont(findViewById(R.id.tvTitleActivity));
        setMediumFont(m_tvSubTitleWeek);

        setRegularFont(m_tvEmpty);
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_exercisesRecyclerViewAdapter.closeMenu(true);
        if(isSixWeekCycle()){
            m_tvSubTitleWeek.setText(getResources().getString(R.string.week_type_text)
                    + ""
                    + getResources().getStringArray(R.array.weeks61)[getWeekOfCycle()]);
        } else {
            m_tvSubTitleWeek.setText(getResources().getString(R.string.week_type_text)
                    + ""
                    + getResources().getStringArray(R.array.weeks1)[getWeekOfCycle()]);
        }
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_exerciseDataArrayList = databaseHandler.getAllExercisesByStatus(m_nStatusOfExercises);
        databaseHandler.close();
        m_exercisesRecyclerViewAdapter.setExerciseDataArrayList(m_exerciseDataArrayList);
        checkCountExercises();
        m_fabAddExercise.postDelayed(new Runnable() {
            @Override
            public void run() {
                m_fabAddExercise.show();
            }
        }, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_exercisesRecyclerViewAdapter.closeMenu(true);
        m_fabAddExercise.hide();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        checkCountExercises();
    }

    private void checkCountExercises(){
        if(m_exercisesRecyclerViewAdapter.getItemCount()==0){
            ((View) m_tvSubTitleWeek.getParent()).setVisibility(View.GONE);
            m_tvEmpty.setVisibility(View.VISIBLE);
            m_exercisesRecyclerView.setVisibility(View.GONE);
        } else {
            ((View)m_tvSubTitleWeek.getParent()).setVisibility(View.VISIBLE);
            m_tvEmpty.setVisibility(View.GONE);
            m_exercisesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ivSettings:
                intent = new Intent(ExercisesActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.ivBodyWeight:
                intent = new Intent(ExercisesActivity.this, WeightChartActivity.class);
                startActivity(intent);
                break;
            case R.id.fabAddExercise:
                intent = new Intent(ExercisesActivity.this, AddExerciseActivity.class);
                startActivity(intent);
                break;
            case R.id.ivDelete:
                m_fabAddExercise.show();
                break;
        }
    }

    private void setStatusOfExercises(int nStatus){
        m_nStatusOfExercises = nStatus;
        m_exercisesRecyclerViewAdapter.closeMenu(true);
        m_fabAddExercise.hide();
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_exerciseDataArrayList = databaseHandler.getAllExercisesByStatus(nStatus);
        databaseHandler.close();
        m_exercisesRecyclerViewAdapter.setExerciseDataArrayList(m_exerciseDataArrayList);
        checkCountExercises();
        // TODO: 1/14/16 change fab
        switch (nStatus){
            case DatabaseHelper.STATUS_NORMAL:
                m_fabAddExercise.setImageResource(R.drawable.png_add_fab);
                break;
            case DatabaseHelper.STATUS_DELETED:
                m_fabAddExercise.setImageResource(R.drawable.png_trash_white);
                break;
        }
        m_fabAddExercise.postDelayed(new Runnable() {
            @Override
            public void run() {
                m_fabAddExercise.show();
            }
        }, 500);
    }
}
