package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.adapter.ExercisesRecyclerViewAdapter;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseData;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.views.DividerItemDecoration;

/**
 * Created by vobideyko on 8/14/15.
 */
public class ExercisesActivity extends BaseActivity implements DialogInterface.OnDismissListener, View.OnClickListener{

    private Toolbar mToolbar;
    private DrawerLayout m_drawerLayout;
    private LinearLayout m_leftDrawerLinearLayout;

    private RecyclerView m_exercisesRecyclerView;
    private ExercisesRecyclerViewAdapter m_exercisesRecyclerViewAdapter;
    private TextView m_tvSubTitleWeek;
    private TextView m_tvEmpty;
    private ArrayList<ExerciseData> m_exerciseDataArrayList;
    private FloatingActionButton m_fabAddExercise;

    private int m_nStatusOfExercises = DatabaseHelper.STATUS_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.exercises_title));

        Drawable drawableIconNavigation = ContextCompat.getDrawable(this, R.drawable.svg_menu_hamburger);
        drawableIconNavigation.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        mToolbar.setNavigationIcon(drawableIconNavigation);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_drawerLayout.isDrawerOpen(m_leftDrawerLinearLayout)){
                    m_drawerLayout.closeDrawer(m_leftDrawerLinearLayout);
                } else {
                    m_drawerLayout.openDrawer(m_leftDrawerLinearLayout);
                }
            }
        });

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

        hideFab();

        initDrawerMenu();
        initFonts();
    }

    private void initDrawerMenu(){

        findViewById(R.id.llExercises).setOnClickListener(this);
        findViewById(R.id.llBodyWeight).setOnClickListener(this);
        findViewById(R.id.llSettings).setOnClickListener(this);
        findViewById(R.id.llTrash).setOnClickListener(this);
        findViewById(R.id.llEmail).setOnClickListener(this);
        findViewById(R.id.llInfo).setOnClickListener(this);
        findViewById(R.id.llSync).setOnClickListener(this);

        //init icons states
        ImageView ivExercises = (ImageView) findViewById(R.id.ivExercisesMenu);
        ImageView ivTrash = (ImageView) findViewById(R.id.ivTrashMenu);
        ImageView ivWeight = (ImageView) findViewById(R.id.ivWeightMenu);
        ImageView ivSettings = (ImageView) findViewById(R.id.ivSettingsMenu);
        ImageView ivEmail = (ImageView) findViewById(R.id.ivEmailMenu);
        ImageView ivAbout = (ImageView) findViewById(R.id.ivInfoMenu);

        setSvgStateToImageView(ivExercises, R.drawable.svg_exercise_drawer);
        setSvgStateToImageView(ivTrash, R.drawable.svg_trash_drawer);
        setSvgStateToImageView(ivWeight, R.drawable.svg_weight_drawer);
        setSvgStateToImageView(ivSettings, R.drawable.svg_settings_drawer);
        setSvgStateToImageView(ivEmail, R.drawable.svg_email_drawer);
        setSvgStateToImageView(ivAbout, R.drawable.svg_info_drawer);

        m_drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        m_leftDrawerLinearLayout = (LinearLayout) findViewById(R.id.leftDrawer);
        m_drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                hideFab();
                m_exercisesRecyclerViewAdapter.closeMenu(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                showFab();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                switch (newState) {
                    case DrawerLayout.STATE_IDLE:
                        if (!m_drawerLayout.isDrawerOpen(m_leftDrawerLinearLayout)) {
                            showFab();
                        }
                        break;
                    case DrawerLayout.STATE_DRAGGING:
                        hideFab();
                        m_exercisesRecyclerViewAdapter.closeMenu(true);
                        break;
                    case DrawerLayout.STATE_SETTLING:
                        hideFab();
                        break;
                }
            }
        });
    }

    private void initFonts(){
        setMediumFont(m_tvSubTitleWeek);

        setRegularFont(m_tvEmpty);

        setMediumFont(findViewById(R.id.tvExercises));
        setMediumFont(findViewById(R.id.tvBodyWeight));
        setMediumFont(findViewById(R.id.tvSettings));
        setMediumFont(findViewById(R.id.tvTrash));
        setMediumFont(findViewById(R.id.tvEmail));
        setMediumFont(findViewById(R.id.tvInfo));
        setMediumFont(findViewById(R.id.tvSync));
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
                showFab();
            }
        }, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_exercisesRecyclerViewAdapter.closeMenu(true);
        m_drawerLayout.closeDrawer(m_leftDrawerLinearLayout);
        hideFab();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        checkCountExercises();
    }

    private void checkCountExercises(){
        if(m_exercisesRecyclerViewAdapter.getItemCount()==0){
            switch (m_nStatusOfExercises){
                case DatabaseHelper.STATUS_NORMAL:
                    m_tvEmpty.setText(R.string.tap_to_add_new_exercise);
                    break;
                case DatabaseHelper.STATUS_DELETED:
                    m_tvEmpty.setText(R.string.trash_is_empty);
                    break;
            }
            ((View) m_tvSubTitleWeek.getParent()).setVisibility(View.GONE);
            m_tvEmpty.setVisibility(View.VISIBLE);
            m_exercisesRecyclerView.setVisibility(View.GONE);
        } else {
            ((View)m_tvSubTitleWeek.getParent()).setVisibility(View.VISIBLE);
            m_tvEmpty.setVisibility(View.GONE);
            m_exercisesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showFab(){
        switch (m_nStatusOfExercises){
            case DatabaseHelper.STATUS_NORMAL:
                m_fabAddExercise.show();
                break;
            case DatabaseHelper.STATUS_DELETED:
                if(m_exercisesRecyclerViewAdapter.getItemCount()!=0){
                    m_fabAddExercise.show();
                } else {
                    m_fabAddExercise.hide();
                }
                break;
        }
    }

    private void hideFab(){
        m_fabAddExercise.hide();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.fabAddExercise:
                switch (m_nStatusOfExercises){
                    case DatabaseHelper.STATUS_NORMAL:
                        intent = new Intent(ExercisesActivity.this, AddExerciseActivity.class);
                        startActivity(intent);
                        break;
                    case DatabaseHelper.STATUS_DELETED:
                        QuestionDialog questionDialogRestore = new QuestionDialog(ExercisesActivity.this,
                                getResources().getString(R.string.question_delete_all_from_trash), new Runnable() {
                            @Override
                            public void run() {
                                DatabaseHelper databaseHandler = new DatabaseHelper(getApplicationContext());
                                databaseHandler.deleteAllExercisesByStatus(DatabaseHelper.STATUS_DELETED);
                                databaseHandler.close();
                                setStatusOfExercises(DatabaseHelper.STATUS_DELETED);
                            }
                        });
                        questionDialogRestore.show();
                        break;
                }
                break;
            case R.id.ivDelete:
                showFab();
                break;
            case R.id.ivRestore:
                showFab();
                break;
            case R.id.llExercises:
                m_drawerLayout.closeDrawer(m_leftDrawerLinearLayout);
                setStatusOfExercises(DatabaseHelper.STATUS_NORMAL);
                break;
            case R.id.llBodyWeight:
                m_drawerLayout.closeDrawer(m_leftDrawerLinearLayout);
                intent = new Intent(ExercisesActivity.this, WeightChartActivity.class);
                startActivity(intent);
                break;
            case R.id.llSettings:
                m_drawerLayout.closeDrawer(m_leftDrawerLinearLayout);
                intent = new Intent(ExercisesActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.llTrash:
                m_drawerLayout.closeDrawer(m_leftDrawerLinearLayout);
                setStatusOfExercises(DatabaseHelper.STATUS_DELETED);
                break;
            case R.id.llEmail:
                m_drawerLayout.closeDrawer(m_leftDrawerLinearLayout);
                PackageInfo pInfo = null;
                String strVersionApp = "0.0e";
                int nVersionCode = 0;
                try {
                    pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                    strVersionApp = pInfo.versionName;
                    nVersionCode = pInfo.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getResources().getString(R.string.my_email), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + " "
                        + getResources().getString(R.string.app_version)+ " " + strVersionApp + " (" + nVersionCode + ")");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.title_mail_chooser)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this,
                            R.string.info_no_mail_clients,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.llInfo:
                m_drawerLayout.closeDrawer(m_leftDrawerLinearLayout);
                intent = new Intent(ExercisesActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.llSync:
                m_drawerLayout.closeDrawer(m_leftDrawerLinearLayout);
                intent = new Intent(ExercisesActivity.this, SyncActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        switch (m_nStatusOfExercises){
            case DatabaseHelper.STATUS_NORMAL:
                super.onBackPressed();
                break;
            case DatabaseHelper.STATUS_DELETED:
                setStatusOfExercises(DatabaseHelper.STATUS_NORMAL);
                break;
        }
    }

    private void setStatusOfExercises(int nStatus){
        m_nStatusOfExercises = nStatus;
        m_exercisesRecyclerViewAdapter.closeMenu(true);
        hideFab();
        DatabaseHelper databaseHandler = new DatabaseHelper(this);
        m_exerciseDataArrayList = databaseHandler.getAllExercisesByStatus(nStatus);
        databaseHandler.close();
        m_exercisesRecyclerViewAdapter.setExerciseDataArrayList(m_exerciseDataArrayList);
        checkCountExercises();
        switch (nStatus){
            case DatabaseHelper.STATUS_NORMAL:
                getSupportActionBar().setTitle(getResources().getString(R.string.exercises_title));
                m_fabAddExercise.setImageResource(R.drawable.png_add_fab);
                break;
            case DatabaseHelper.STATUS_DELETED:
                getSupportActionBar().setTitle(getResources().getString(R.string.trash_title));
                m_fabAddExercise.setImageResource(R.drawable.png_trash_white);
                break;
        }
        m_fabAddExercise.postDelayed(new Runnable() {
            @Override
            public void run() {
                showFab();
            }
        }, 500);
    }

    private void setSvgStateToImageView(ImageView imageView, int resIdSvg){
        Drawable drawableIcon;
        StateListDrawable states = new StateListDrawable();

        drawableIcon = ContextCompat.getDrawable(this, resIdSvg);
        drawableIcon.setColorFilter(this.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        states.addState(new int[] {android.R.attr.state_pressed}, drawableIcon);

        drawableIcon = ContextCompat.getDrawable(this, resIdSvg);
        drawableIcon.setColorFilter(this.getResources().getColor(R.color.colorSecondaryText), PorterDuff.Mode.SRC_ATOP);
        states.addState(new int[] { }, drawableIcon);

        imageView.setImageDrawable(states);
    }
}
