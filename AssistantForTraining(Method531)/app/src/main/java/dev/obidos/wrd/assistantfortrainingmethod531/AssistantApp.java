package dev.obidos.wrd.assistantfortrainingmethod531;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by vobideyko on 8/14/15.
 */
public class AssistantApp extends Application {
    public static final String SHARED_PREFERENCES_NAME = "assistant531";
    private static SharedPreferences m_sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        m_sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

    }

    public static SharedPreferences getSharedPreferences(){
        return m_sharedPreferences;
    }
}
