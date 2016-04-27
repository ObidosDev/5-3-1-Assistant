package dev.obidos.wrd.assistantfortrainingmethod531.dropbox;

import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import dev.obidos.wrd.assistantfortrainingmethod531.activity.BaseActivity;

/**
 * Created by vobideyko on 4/21/16.
 */
public class DropBoxManager {

    private final static String APP_KEY = "cjm7592hf6uq183";
    private final static String APP_SECRET = "yi3nji0ff99igt9";

    private DropboxAPI<AndroidAuthSession> mDBApi;

    private String m_strAccessToken = null;

    public DropBoxManager(BaseActivity baseActivity){
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().startOAuth2Authentication(baseActivity);
    }

    public String getAccessTokenOnResume(){
        String accessToken = null;
        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                mDBApi.getSession().finishAuthentication();
                accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.e("DbAuthLog", "Error authenticating", e);
            }
        }
        m_strAccessToken = accessToken;
        return accessToken;
    }

    public void loadToDropbox(File file, ProgressListener progressListener){
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        DropboxAPI.Entry response = null;
        try {
            response = mDBApi.putFile("/magnum-opus.txt", inputStream, file.length(), null, progressListener);
        } catch (DropboxException e) {
            e.printStackTrace();
        }

        Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
    }
}
