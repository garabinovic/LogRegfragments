package fragments.android.exemple.com.logregfragments;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        tv = (TextView) findViewById(R.id.AboutText);
        tv.setText(getResources().getString(R.string.aboutText));


    }
}
