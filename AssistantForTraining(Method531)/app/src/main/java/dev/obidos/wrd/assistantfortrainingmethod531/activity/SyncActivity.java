package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.os.Bundle;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;

import java.io.File;

import dev.obidos.wrd.assistantfortrainingmethod531.R;
import dev.obidos.wrd.assistantfortrainingmethod531.dropbox.DropBoxManager;

/**
 * Created by vobideyko on 4/21/16.
 */
public class SyncActivity extends BaseActivity {

    private DropBoxManager m_dropBoxManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(m_dropBoxManager!=null) {
            if (m_dropBoxManager.getAccessTokenOnResume() != null) {
                //it's ok

                Log.e("Obidos", "its ok");

                File file = new File("working-draft.txt");
                m_dropBoxManager.loadToDropbox(file, new ProgressListener() {
                    @Override
                    public void onProgress(long l, long l1) {

                        Log.e("Obidos", "l: "+l +" l1: "+l1);

                    }
                });

            } else {
                //problem with registration

                Log.e("Obidos", "problem");

                finish();
            }
        } else {
            m_dropBoxManager = new DropBoxManager(this);
        }
    }
}
