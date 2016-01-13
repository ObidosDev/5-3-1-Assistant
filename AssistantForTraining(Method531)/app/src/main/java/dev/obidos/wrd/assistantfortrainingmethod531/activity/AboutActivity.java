package dev.obidos.wrd.assistantfortrainingmethod531.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import dev.obidos.wrd.assistantfortrainingmethod531.R;

/**
 * Created by vobideyko on 8/27/15.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        findViewById(R.id.ivBack).setOnClickListener(this);
        findViewById(R.id.ivDo4a).setOnClickListener(this);
        findViewById(R.id.ivIcons8).setOnClickListener(this);

        init();
    }

    private void init() {
        setMediumFont(findViewById(R.id.tvTitleActivity));
        setMediumFont(findViewById(R.id.tvTitleThanksTo));
        setMediumFont(findViewById(R.id.tvAppVersion));

        setRegularFont(findViewById(R.id.tvAppName));
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
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
