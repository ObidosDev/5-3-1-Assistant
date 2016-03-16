package dev.obidos.wrd.assistantfortrainingmethod531.tools;

/**
 * Created by vobideyko on 3/16/16.
 */
public class TrainingConstants {

    public static final String EXTRA_ID_EXERCISE = "id";
    public static final String EXTRA_NAME_EXERCISE = "name";
    public static final String EXTRA_LAST_SET_WEIGHT_EXERCISE = "last_set_weight";
    public static final String EXTRA_AIM_WEIGHT_EXERCISE = "aim_weight";
    public static final String EXTRA_FROM_INFO_EXERCISE_ACTIVITY = "info";

    public static final String GET_STUCK_CONST = "0.9";
    public static final String SPECIAL_CONST_FOR_RM_CALCULATION = "0.0333";

    public static final int[] ARRAY_REPS_WARM_UP = {5, 5, 5};//default warm up
    public static final int[] ARRAY_REPS_WARM_UP_DELOAD = {5, 5, 5};//warm up deload

    public static final int[] ARRAY_REPS_WORKOUT_1 = {5, 5, 5};// 555 week reps
    public static final int[] ARRAY_REPS_WORKOUT_2 = {3, 3, 3};// 333 week reps
    public static final int[] ARRAY_REPS_WORKOUT_3 = {5, 3, 1};// 531 week reps

    public static final int[] ARRAY_REPS_WORKOUT_DELOAD = {5, 5, 5};// deload reps


    public static final float[] ARRAY_WEIGHT_WARM_UP = {0.4f, 0.5f, 0.6f};
    public static final float[] ARRAY_WEIGHT_WARM_UP_DELOAD = {0.4f, 0.4f, 0.5f};

    public static final float[] ARRAY_WEIGHT_WORKOUT_1 = {0.65f, 0.75f, 0.85f};// 555 week weight
    public static final float[] ARRAY_WEIGHT_WORKOUT_2 = {0.70f, 0.80f, 0.90f};// 333 week weight
    public static final float[] ARRAY_WEIGHT_WORKOUT_3 = {0.75f, 0.85f, 0.95f};// 531 week weight

    public static final float[] ARRAY_WEIGHT_WORKOUT_DELOAD = {0.5f, 0.6f, 0.6f};

    public static final float COEFFICIENT_BORING_BUT_BIG = 0.5f; //BigButBoring weight coefficient
    public static final int[] ARRAY_REPS_BORING_BUT_BIG = {10, 10, 10, 10, 10};// BigButBoring reps
}
