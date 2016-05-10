package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import dev.obidos.wrd.assistantfortrainingmethod531.R;

/**
 * Created by vobideyko on 8/27/15.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(R.string.title_about);

        Drawable drawableIconNavigation = ContextCompat.getDrawable(this, R.drawable.svg_back);
        drawableIconNavigation.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        mToolbar.setNavigationIcon(drawableIconNavigation);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.ivDo4a).setOnClickListener(this);
        findViewById(R.id.ivIcons8).setOnClickListener(this);

        init();
    }

    private void init() {

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

        TextView tvVersion = (TextView) findViewById(R.id.tvAppVersion);
        tvVersion.setText(getResources().getString(R.string.app_version) + " " + strVersionApp + " (" + nVersionCode + ")");

        setMediumFont(findViewById(R.id.tvTitleThanksTo));
        setMediumFont(tvVersion);
        setMediumFont(findViewById(R.id.tvAppName));

        setRegularFont(findViewById(R.id.tvAuthorName));
    }

    @Override
    public void onClick(View v) {
        String url;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (v.getId()){
            case R.id.ivIcons8:
                url = "http://www.icons8.com";
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.ivDo4a:
                url = "http://vk.com/your_future";
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
        }
    }
}
