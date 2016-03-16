package dev.obidos.wrd.assistantfortrainingmethod531.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.AddExerciseActivity;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.ExerciseInfoActivity;
import dev.obidos.wrd.assistantfortrainingmethod531.activity.ExercisesActivity;
import dev.obidos.wrd.assistantfortrainingmethod531.database.DatabaseHelper;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseData;
import dev.obidos.wrd.assistantfortrainingmethod531.dialog.QuestionDialog;
import dev.obidos.wrd.assistantfortrainingmethod531.tools.TrainingConstants;

/**
 * Created by vobideyko on 8/14/15.
 */
public class ExercisesRecyclerViewAdapter extends RecyclerView.Adapter<ExercisesRecyclerViewAdapter.ViewHolder> {
    private ArrayList<ExerciseData> m_Dataset;
    private ExercisesActivity m_activity;

    private ArrayList<Integer> m_arrayListPositionOpenMenu = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivEdit;
        public ImageView ivRestore;
        public HorizontalScrollView horizontalScrollView;
        public ImageView ivDelete;
        public ImageView ivMarker;
        public TextView tvNameExercise;
        public TextView tvCycleNumber;
        public TextView tv1tm;
        public LinearLayout linearLayoutItem;
        public int m_nPosition;

        public boolean bMoved = false;
        public boolean bOpend = false;
        public ViewHolder(View v) {
            super(v);
            ivEdit = (ImageView) v.findViewById(R.id.ivEdit);
            ivEdit.setOnClickListener(this);
            ivRestore = (ImageView) v.findViewById(R.id.ivRestore);
            ivRestore.setOnClickListener(this);
            ivDelete = (ImageView) v.findViewById(R.id.ivDelete);
            ivDelete.setOnClickListener(this);
            tvNameExercise = (TextView) v.findViewById(R.id.tvNameExercise);
            tvCycleNumber = (TextView) v.findViewById(R.id.tvCycleNumber);
            tv1tm = (TextView) v.findViewById(R.id.tv1tm);
            linearLayoutItem = (LinearLayout) v.findViewById(R.id.llItemContent);
            linearLayoutItem.setOnClickListener(this);
            horizontalScrollView = (HorizontalScrollView) v.findViewById(R.id.horizontalScrollView);
            ivMarker = (ImageView) v.findViewById(R.id.ivMarker);
        }

        @Override
        public void onClick(final View v) {
            switch (v.getId()){
                case R.id.ivEdit:
                    closeMenu(true);
                    Intent intentEdit = new Intent(m_activity, AddExerciseActivity.class);
                    intentEdit.putExtra(TrainingConstants.EXTRA_ID_EXERCISE, m_Dataset.get(m_nPosition).getId());
                    m_activity.startActivity(intentEdit);
                    break;
                case R.id.ivRestore:
                    closeMenu(true);
                    QuestionDialog questionDialogRestore = new QuestionDialog(m_activity,
                            m_activity.getResources().getString(R.string.question_restore_exercise), new Runnable() {
                        @Override
                        public void run() {
                            DatabaseHelper databaseHandler = new DatabaseHelper(m_activity);
                            m_Dataset.get(m_nPosition).setStatus(DatabaseHelper.STATUS_NORMAL);
                            databaseHandler.updateExercise(m_Dataset.get(m_nPosition));
                            databaseHandler.close();
                            ExercisesRecyclerViewAdapter.this.notifyItemRemoved(m_nPosition);
                            m_Dataset.remove(m_nPosition);
                            ExercisesRecyclerViewAdapter.this.notifyItemRangeChanged(m_nPosition,m_Dataset.size()-m_nPosition);
                            m_activity.onClick(v);
                        }
                    });
                    questionDialogRestore.setOnDismissListener(m_activity);
                    questionDialogRestore.show();
                    break;
                case R.id.ivDelete:
                    closeMenu(true);
                    String strQuestion ="";
                    switch (m_Dataset.get(m_nPosition).getStatus()){
                        case DatabaseHelper.STATUS_NORMAL:
                            strQuestion = m_activity.getResources().getString(R.string.question_move_to_trash);
                            break;
                        case DatabaseHelper.STATUS_DELETED:
                            strQuestion = m_activity.getResources().getString(R.string.question_delete_exercise);
                            break;
                    }
                    QuestionDialog questionDialog = new QuestionDialog(m_activity,
                            strQuestion, new Runnable() {
                        @Override
                        public void run() {
                            DatabaseHelper databaseHandler = new DatabaseHelper(m_activity);
                            databaseHandler.deleteExercise(m_Dataset.get(m_nPosition).getId());
                            databaseHandler.close();
                            ExercisesRecyclerViewAdapter.this.notifyItemRemoved(m_nPosition);
                            m_Dataset.remove(m_nPosition);
                            ExercisesRecyclerViewAdapter.this.notifyItemRangeChanged(m_nPosition,m_Dataset.size()-m_nPosition);
                            m_activity.onClick(v);
                        }
                    });
                    questionDialog.setOnDismissListener(m_activity);
                    questionDialog.show();
                    break;
                case R.id.llItemContent:
                    closeMenu(true);
                    Intent intentInfo = new Intent(m_activity, ExerciseInfoActivity.class);
                    intentInfo.putExtra("id", m_Dataset.get(m_nPosition).getId());
                    m_activity.startActivity(intentInfo);
                    break;
            }
        }
    }

    public ExercisesRecyclerViewAdapter(ArrayList<ExerciseData> myDataset, ExercisesActivity activity) {
        m_Dataset = myDataset;
        m_activity = activity;
    }

    @Override
    public ExercisesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercises_recycler, parent, false);

        TextView tvNameExercise = (TextView) v.findViewById(R.id.tvNameExercise);
        m_activity.setRegularFont(tvNameExercise);
        TextView tvCycleNumber = (TextView) v.findViewById(R.id.tvCycleNumber);
        m_activity.setMediumFont(tvCycleNumber);
        TextView tv1tm = (TextView) v.findViewById(R.id.tv1tm);
        m_activity.setMediumFont(tv1tm);

        ImageView imageView = (ImageView) v.findViewById(R.id.ivRestore);
        imageView.setVisibility(View.GONE);

        LinearLayout relativeLayoutItem = (LinearLayout) v.findViewById(R.id.llItemContent);
        int width = getScreenWidth(v.getContext().getApplicationContext());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) relativeLayoutItem.getLayoutParams();
        layoutParams.width = width;
        relativeLayoutItem.setLayoutParams(layoutParams);

        v.findViewById(R.id.horizontalScrollView).setEnabled(true);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        setHorizontalScrollViewSettings(holder, position);

        holder.m_nPosition = position;
        holder.tvNameExercise.setText(m_Dataset.get(position).getName());
        holder.tvCycleNumber.setText(m_activity.getResources().getString(R.string.text_cycle_number) + m_Dataset.get(position).getNumberCycle());
        holder.tv1tm.setText(m_activity.getResources().getString(R.string.tm) + " " + m_activity.formatWeight(m_Dataset.get(position).getWeight()));
        int[] markerColors = m_activity.getResources().getIntArray(R.array.marker_colors);
        holder.ivMarker.setBackgroundColor(markerColors[m_Dataset.get(position).getColorNumber()]);

        if(m_Dataset.get(position).getStatus()==DatabaseHelper.STATUS_DELETED){
            holder.ivRestore.setVisibility(View.VISIBLE);
        } else {
            holder.ivRestore.setVisibility(View.GONE);
        }
    }

    private void setHorizontalScrollViewSettings(final ViewHolder holder,final int position){
        if(!m_arrayListPositionOpenMenu.isEmpty() && positionInArrayList(position)){
            holder.bOpend = true;
            holder.horizontalScrollView.postDelayed(new Runnable() {
                public void run() {
                    holder.horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            }, 10L);
        } else {
            holder.bOpend = false;
            holder.horizontalScrollView.postDelayed(new Runnable() {
                public void run() {
                    holder.horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                }
            }, 10L);
        }

        holder.horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    holder.bMoved = true;
                }

                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && holder.bMoved) {
                    holder.bMoved = false;
                    boolean find = positionInArrayList(position);
                    if (!find && !holder.bOpend
                            && v.getScrollX() != 0) {
                        holder.horizontalScrollView.postDelayed(new Runnable() {
                            public void run() {
                                m_arrayListPositionOpenMenu.add(position);
                                closeMenu(false);
                                holder.horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                                holder.bOpend = true;
                            }
                        }, 70L);
                    } else if (find && v.getScrollX() != ((HorizontalScrollView) v).getChildAt(0).getWidth() - getScreenWidth(v.getContext().getApplicationContext())) {
                        holder.bOpend = false;
                        closeMenu(true);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return m_Dataset.size();
    }

    private int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics.widthPixels;
    }

    private boolean positionInArrayList(int position){
        for(Integer i : m_arrayListPositionOpenMenu){
            if(i == position){
                return true;
            }
        }
        return false;
    }

    public synchronized void closeMenu(boolean bLastClose){
        if(!m_arrayListPositionOpenMenu.isEmpty()) {
            while ((m_arrayListPositionOpenMenu.size()!=1 && !bLastClose)
                    || (m_arrayListPositionOpenMenu.size()!=0 && bLastClose)){
                for (int i = 0; i < m_arrayListPositionOpenMenu.size(); i++) {
                    Integer pos = m_arrayListPositionOpenMenu.get(i);
                    m_arrayListPositionOpenMenu.remove(pos);
                    notifyItemChanged(pos);
                    break;
                }
            }
        }
    }

    public void setExerciseDataArrayList(ArrayList<ExerciseData> exerciseDataArrayList){
        m_Dataset = exerciseDataArrayList;
        this.notifyDataSetChanged();
    }
}