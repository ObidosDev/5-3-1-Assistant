package dev.obidos.wrd.assistantfortrainingmethod531.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.BodyWeightData;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseData;
import dev.obidos.wrd.assistantfortrainingmethod531.database.entity.ExerciseWeightData;

/**
 * Created by vobideyko on 8/17/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "assistant_training.db";

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_DELETED = 1;

    //TABLE_EXERCISES
    private static final String TABLE_EXERCISES = "exercises";
    private static final String COL_INDEX = "_id";
    private static final String COL_NAME = "name";
    private static final String COL_TYPE = "type";
    private static final String COL_WEIGHT = "weight";
    private static final String COL_CYCLE_NUMBER = "cycle_number";
    private static final String COL_AIM_WEIGHT = "aim_weight";
    private static final String COL_RECORD_WEIGHT = "record_weight";
    private static final String COL_COLOR = "exercise_color";
    private static final String COL_STATUS = "exercise_status";

    //TABLE_BODY_WEIGHT
    private static final String TABLE_BODY_WEIGHT = "body_weight";
    private static final String COL_DATE_DAY = "some_date_day";
    private static final String COL_DATE_MONTH = "some_date_month";
    private static final String COL_DATE_YEAR = "some_date_year";

    //TABLE_EXERCISE_CHART_WEIGHT
    private static final String TABLE_EXERCISE_CHART_WEIGHT = "exercise_weight";
    private static final String COL_INDEX_EXERCISE = "id_exercise";

    private Context m_context;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        m_context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_EXERCISES + " ("
                + COL_INDEX +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_TYPE + " INTEGER NOT NULL, "
                + COL_WEIGHT + " REAL NOT NULL, "
                + COL_CYCLE_NUMBER + " INTEGER NOT NULL, "
                + COL_AIM_WEIGHT + " TEXT NOT NULL, "
                + COL_RECORD_WEIGHT + " TEXT NOT NULL, "
                + COL_COLOR + " INTEGER NOT NULL, "
                + COL_STATUS + " INTEGER NOT NULL)";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_BODY_WEIGHT + " ("
                + COL_INDEX +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_WEIGHT + " REAL NOT NULL, "
                + COL_DATE_DAY + " INTEGER NOT NULL, "
                + COL_DATE_MONTH + " INTEGER NOT NULL, "
                + COL_DATE_YEAR + " INTEGER NOT NULL)";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_EXERCISE_CHART_WEIGHT + " ("
                + COL_INDEX +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INDEX_EXERCISE +" INTEGER NOT NULL, "
                + COL_WEIGHT + " REAL NOT NULL, "
                + COL_DATE_DAY + " INTEGER NOT NULL, "
                + COL_DATE_MONTH + " INTEGER NOT NULL, "
                + COL_DATE_YEAR + " INTEGER NOT NULL)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_BODY_WEIGHT);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE_CHART_WEIGHT);
        //onCreate(db);

        String strTempTable = "temp_exercises";
        db.execSQL("DROP TABLE IF EXISTS " + strTempTable);
        //create temp table
        String CREATE_TABLE = "CREATE TABLE " + strTempTable + " ("
                + COL_INDEX +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_TYPE + " INTEGER NOT NULL, "
                + COL_WEIGHT + " REAL NOT NULL, "
                + COL_CYCLE_NUMBER + " INTEGER NOT NULL, "
                + COL_AIM_WEIGHT + " TEXT NOT NULL, "
                + COL_RECORD_WEIGHT + " TEXT NOT NULL, "
                + COL_COLOR + " INTEGER NOT NULL)";
        db.execSQL(CREATE_TABLE);

        //copy all data from exercise table to temp table
        db.execSQL("INSERT INTO " + strTempTable + " SELECT * FROM " + TABLE_EXERCISES + ";");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);

        CREATE_TABLE = "CREATE TABLE " + TABLE_EXERCISES + " ("
                + COL_INDEX +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_TYPE + " INTEGER NOT NULL, "
                + COL_WEIGHT + " REAL NOT NULL, "
                + COL_CYCLE_NUMBER + " INTEGER NOT NULL, "
                + COL_AIM_WEIGHT + " TEXT NOT NULL, "
                + COL_RECORD_WEIGHT + " TEXT NOT NULL, "
                + COL_COLOR + " INTEGER NOT NULL, "
                + COL_STATUS + " INTEGER NOT NULL)";
        db.execSQL(CREATE_TABLE);

        //copy all data from temp table to exercise table
        db.execSQL("INSERT INTO " + TABLE_EXERCISES + " SELECT *, 0 as " + COL_STATUS + " FROM " + strTempTable + ";");

        db.execSQL("DROP TABLE IF EXISTS " + strTempTable);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////Exercises
    public long addExercise(ExerciseData exerciseData) {
        long nId = -1;
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_NAME, exerciseData.getName());
            values.put(COL_TYPE, exerciseData.getType());
            values.put(COL_WEIGHT, exerciseData.getWeight());
            values.put(COL_CYCLE_NUMBER, exerciseData.getNumberCycle());
            values.put(COL_AIM_WEIGHT, exerciseData.getAimWeight());
            values.put(COL_RECORD_WEIGHT, exerciseData.getRecordWeight());
            values.put(COL_COLOR, exerciseData.getColorNumber());
            values.put(COL_STATUS, exerciseData.getStatus());

            nId = db.insert(TABLE_EXERCISES, null, values);
            db.close();
        return nId;
    }

    public void deleteAllExercises() {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_EXERCISES, null, null);
            db.delete(TABLE_EXERCISE_CHART_WEIGHT, null, null);
            db.close();
    }

    public void deleteAllExercisesByStatus(int nStatus) {
        if(nStatus == STATUS_NORMAL){
            ArrayList<ExerciseData> exerciseDatas = getAllExercisesByStatus(STATUS_NORMAL);
            for(ExerciseData exerciseData : exerciseDatas){
                exerciseData.setStatus(STATUS_DELETED);
                updateExercise(exerciseData);
            }
        } else if(nStatus == STATUS_DELETED){
            ArrayList<ExerciseData> exerciseDatas = getAllExercisesByStatus(STATUS_DELETED);
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_EXERCISES, COL_STATUS + "=" + STATUS_DELETED, null);
            db.close();
            ArrayList<Long> arrIds = new ArrayList<>();
            for(ExerciseData exerciseData : exerciseDatas){
                arrIds.add((long) exerciseData.getId());
            }
            deleteAllExercisesChartWeightsByIds(arrIds);
        }
    }

    public void deleteExercise(long nId) {
        ExerciseData exerciseData = getExercise(nId);
        if(exerciseData.getStatus()==STATUS_NORMAL){
            exerciseData.setStatus(STATUS_DELETED);
            updateExercise(exerciseData);
        } else if(exerciseData.getStatus()==STATUS_DELETED){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_EXERCISES, COL_INDEX + " = " + nId, null);
            db.close();
            deleteAllExerciseChartWeights(nId);
        }
    }

    public void updateExercise(ExerciseData exerciseData) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_EXERCISES
                + " SET "
                + COL_NAME + " = \"" + exerciseData.getName()
                + "\" WHERE " + COL_INDEX + " = " + exerciseData.getId() + ";";
        db.execSQL(updateQuery);

        updateQuery = "UPDATE " + TABLE_EXERCISES
                + " SET "
                + COL_TYPE + " = " + exerciseData.getType()
                + " WHERE " + COL_INDEX + " = " + exerciseData.getId() + ";";
        db.execSQL(updateQuery);

        updateQuery = "UPDATE " + TABLE_EXERCISES
                + " SET "
                + COL_WEIGHT + " = " + exerciseData.getWeight()
                + " WHERE " + COL_INDEX + " = " + exerciseData.getId() + ";";
        db.execSQL(updateQuery);

        updateQuery = "UPDATE " + TABLE_EXERCISES
                + " SET "
                + COL_CYCLE_NUMBER + " = " + exerciseData.getNumberCycle()
                + " WHERE " + COL_INDEX + " = " + exerciseData.getId() + ";";
        db.execSQL(updateQuery);

        updateQuery = "UPDATE " + TABLE_EXERCISES
                + " SET "
                + COL_AIM_WEIGHT + " = \"" + exerciseData.getAimWeight()
                + "\" WHERE " + COL_INDEX + " = " + exerciseData.getId() + ";";
        db.execSQL(updateQuery);

        updateQuery = "UPDATE " + TABLE_EXERCISES
                + " SET "
                + COL_RECORD_WEIGHT + " = \"" + exerciseData.getRecordWeight()
                + "\" WHERE " + COL_INDEX + " = " + exerciseData.getId() + ";";
        db.execSQL(updateQuery);

        updateQuery = "UPDATE " + TABLE_EXERCISES
                + " SET "
                + COL_COLOR + " = \"" + exerciseData.getColorNumber()
                + "\" WHERE " + COL_INDEX + " = " + exerciseData.getId() + ";";
        db.execSQL(updateQuery);

        updateQuery = "UPDATE " + TABLE_EXERCISES
                + " SET "
                + COL_STATUS + " = " + exerciseData.getStatus()
                + " WHERE " + COL_INDEX + " = " + exerciseData.getId() + ";";
        db.execSQL(updateQuery);

            db.close();
    }

    public ArrayList<ExerciseData> getAllExercisesByStatus(int nStatus) {
        if(nStatus != DatabaseHelper.STATUS_NORMAL && nStatus != DatabaseHelper.STATUS_DELETED) {
            throw new IllegalArgumentException("Status of exercises must be from {"
                    + DatabaseHelper.STATUS_NORMAL + ", "
                    + DatabaseHelper.STATUS_DELETED + "}, but nStatus = " + nStatus);
        }
        ArrayList<ExerciseData> exerciseDatas = new ArrayList<ExerciseData>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISES
                                + " WHERE " + COL_STATUS + " = " + nStatus
                                + " ORDER BY " + COL_COLOR + ", " + COL_NAME + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ExerciseData exerciseData = new ExerciseData();

                exerciseData.setId(Integer.parseInt(cursor.getString(0)));
                exerciseData.setName(cursor.getString(1));
                exerciseData.setType(cursor.getInt(2));
                exerciseData.setWeight(cursor.getDouble(3));
                exerciseData.setNumberCycle(cursor.getInt(4));
                exerciseData.setAimWeight(cursor.getString(5));
                exerciseData.setRecordWeight(cursor.getString(6));
                exerciseData.setColorNumber(cursor.getInt(7));
                exerciseData.setStatus(cursor.getInt(8));

                exerciseDatas.add(exerciseData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exerciseDatas;
    }

    public ArrayList<ExerciseData> getAllExercises() {
        ArrayList<ExerciseData> exerciseDatas = new ArrayList<ExerciseData>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISES
                + " ORDER BY " + COL_COLOR + ", " + COL_NAME + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ExerciseData exerciseData = new ExerciseData();

                exerciseData.setId(Integer.parseInt(cursor.getString(0)));
                exerciseData.setName(cursor.getString(1));
                exerciseData.setType(cursor.getInt(2));
                exerciseData.setWeight(cursor.getDouble(3));
                exerciseData.setNumberCycle(cursor.getInt(4));
                exerciseData.setAimWeight(cursor.getString(5));
                exerciseData.setRecordWeight(cursor.getString(6));
                exerciseData.setColorNumber(cursor.getInt(7));
                exerciseData.setStatus(cursor.getInt(8));

                exerciseDatas.add(exerciseData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exerciseDatas;
    }

    public ExerciseData getExercise(long nId) {
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISES
                + " WHERE " + COL_INDEX + " = " + nId + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ExerciseData exerciseData = new ExerciseData();
        if (cursor.moveToFirst()) {
            do {
                exerciseData.setId(Integer.parseInt(cursor.getString(0)));
                exerciseData.setName(cursor.getString(1));
                exerciseData.setType(cursor.getInt(2));
                exerciseData.setWeight(cursor.getDouble(3));
                exerciseData.setNumberCycle(cursor.getInt(4));
                exerciseData.setAimWeight(cursor.getString(5));
                exerciseData.setRecordWeight(cursor.getString(6));
                exerciseData.setColorNumber(cursor.getInt(7));
                exerciseData.setStatus(cursor.getInt(8));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exerciseData;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////Body weight
    public long addBodyWeight(BodyWeightData bodyWeightData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_WEIGHT, bodyWeightData.getWeight());
        String[] strDates = bodyWeightData.getStrDate().split("\\.");
        values.put(COL_DATE_DAY, Integer.valueOf(strDates[0]));
        values.put(COL_DATE_MONTH, Integer.valueOf(strDates[1]));
        values.put(COL_DATE_YEAR, Integer.valueOf(strDates[2]));

        long nId = db.insert(TABLE_BODY_WEIGHT, null, values);
        db.close();
        return nId;
    }

    public void deleteAllBodyWeights() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BODY_WEIGHT, null, null);
        db.close();
    }

    public void deleteBodyWeight(long nId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BODY_WEIGHT, COL_INDEX + " = " + nId, null);
        db.close();
    }

    public void updateBodyWeight(BodyWeightData bodyWeightData) {
        SQLiteDatabase db = this.getWritableDatabase();

        String updateQuery = "UPDATE " + TABLE_BODY_WEIGHT
                + " SET "
                + COL_WEIGHT + " = " + bodyWeightData.getWeight()
                + " WHERE " + COL_INDEX + " = " + bodyWeightData.getId() + ";";
        db.execSQL(updateQuery);

        String[] strDates = bodyWeightData.getStrDate().split("\\.");

        updateQuery = "UPDATE " + TABLE_BODY_WEIGHT
                + " SET "
                + COL_DATE_DAY + " = " + strDates[0]
                + " WHERE " + COL_INDEX + " = " + bodyWeightData.getId() + ";";
        db.execSQL(updateQuery);

        updateQuery = "UPDATE " + TABLE_BODY_WEIGHT
                + " SET "
                + COL_DATE_MONTH + " = " + strDates[1]
                + " WHERE " + COL_INDEX + " = " + bodyWeightData.getId() + ";";
        db.execSQL(updateQuery);

        updateQuery = "UPDATE " + TABLE_BODY_WEIGHT
                + " SET "
                + COL_DATE_YEAR + " = " + strDates[2]
                + " WHERE " + COL_INDEX + " = " + bodyWeightData.getId() + ";";
        db.execSQL(updateQuery);

        db.close();
    }

    public ArrayList<BodyWeightData> getAllBodyWeights() {
        ArrayList<BodyWeightData> bodyWeightDatas = new ArrayList<BodyWeightData>();
        String selectQuery = "SELECT  * FROM " + TABLE_BODY_WEIGHT
                + " ORDER BY " + COL_DATE_YEAR + ", "
                + COL_DATE_MONTH + ", "
                + COL_DATE_DAY + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BodyWeightData bodyWeightData = new BodyWeightData();

                bodyWeightData.setId(cursor.getInt(0));
                bodyWeightData.setWeight(cursor.getDouble(1));
                String strDate = cursor.getInt(2) + "." + cursor.getInt(3) + "." + cursor.getInt(4);
                bodyWeightData.setStrDate(strDate);

                bodyWeightDatas.add(bodyWeightData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bodyWeightDatas;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////Exercise progress weight
    public long addExerciseChartWeight(ExerciseWeightData exerciseWeightData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_WEIGHT, exerciseWeightData.getWeight());
        values.put(COL_INDEX_EXERCISE, exerciseWeightData.getIdExercise());
        String[] strDates = exerciseWeightData.getStrDate().split("\\.");
        values.put(COL_DATE_DAY, Integer.valueOf(strDates[0]));
        values.put(COL_DATE_MONTH, Integer.valueOf(strDates[1]));
        values.put(COL_DATE_YEAR, Integer.valueOf(strDates[2]));

        long nId = db.insert(TABLE_EXERCISE_CHART_WEIGHT, null, values);
        db.close();

        setRecordWeightForExerciseById(exerciseWeightData.getIdExercise());

        return nId;
    }

    public void deleteAllExerciseChartWeights(long nIdExercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE_CHART_WEIGHT, COL_INDEX_EXERCISE + "=" + nIdExercise, null);
        db.close();
        setRecordWeightForExerciseById(nIdExercise);
    }

    public void deleteAllExercisesChartWeightsByIds(ArrayList<Long> arrIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strIn = "";
        for(int i=0;i<arrIds.size();i++){
            strIn+=arrIds.get(i)+"";
            if(i<arrIds.size()-1){
                strIn+=",";
            }
        }
        db.delete(TABLE_EXERCISE_CHART_WEIGHT, COL_INDEX_EXERCISE + " IN (" + strIn+")", null);
        db.close();
        for(Long l : arrIds) {
            setRecordWeightForExerciseById(l);
        }
    }

    public void deleteAllExerciseChartWeights() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE_CHART_WEIGHT, null, null);
        db.close();

        ArrayList<ExerciseData> exerciseDatas = getAllExercises();
        for(ExerciseData exerciseData : exerciseDatas){
            exerciseData.setRecordWeight("0");
            updateExercise(exerciseData);
        }
    }

    public void deleteExerciseChartWeight(long nId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strQuery = "SELECT " + COL_INDEX_EXERCISE + " FROM " + TABLE_EXERCISE_CHART_WEIGHT
                + " WHERE " + COL_INDEX + " = " + nId;
        Cursor cursor = db.rawQuery(strQuery, null);
        long nIdExercise = -1;
        if (cursor.moveToFirst()) {
            do {
                nIdExercise = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.delete(TABLE_EXERCISE_CHART_WEIGHT, COL_INDEX + " = " + nId, null);
        db.close();

        if(nIdExercise!=-1) {
            setRecordWeightForExerciseById(nIdExercise);
        }
    }

    public ArrayList<ExerciseWeightData> getAllExerciseChartWeights(int nIdExercise) {
        ArrayList<ExerciseWeightData> exerciseWeightDatas = new ArrayList<ExerciseWeightData>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE_CHART_WEIGHT
                + " WHERE " + COL_INDEX_EXERCISE + "=" + nIdExercise
                + " ORDER BY " + COL_DATE_YEAR + ", "
                + COL_DATE_MONTH + ", "
                + COL_DATE_DAY + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ExerciseWeightData bodyWeightData = new ExerciseWeightData();

                bodyWeightData.setId(cursor.getInt(0));
                bodyWeightData.setIdExercise(cursor.getInt(1));
                bodyWeightData.setWeight(cursor.getDouble(2));
                String strDate = cursor.getInt(3) + "." + cursor.getInt(4) + "." + cursor.getInt(5);
                bodyWeightData.setStrDate(strDate);

                exerciseWeightDatas.add(bodyWeightData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exerciseWeightDatas;
    }

    private void setRecordWeightForExerciseById(long nId){
        ExerciseData exerciseData = getExercise(nId);
        SQLiteDatabase db = this.getWritableDatabase();
        String strQuery = "SELECT max(" + COL_WEIGHT + ") from " + TABLE_EXERCISE_CHART_WEIGHT
                + " WHERE " + COL_INDEX_EXERCISE + " = " + nId + ";";
        Cursor cursor = db.rawQuery(strQuery,null);
        float fRecordWeight = 0;
        if (cursor.moveToFirst()) {
            do {
                fRecordWeight = cursor.getFloat(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        exerciseData.setRecordWeight(String.valueOf(fRecordWeight));
        updateExercise(exerciseData);
    }
}