package com.yasam.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent callerIntent = getIntent();
        if(callerIntent!=null && callerIntent.hasExtra(Intent.EXTRA_TEXT)){
            String forecast = callerIntent.getStringExtra(Intent.EXTRA_TEXT);

            TextView txtVw_detail = (TextView) findViewById(R.id.txtVw_detail);

            if(txtVw_detail!=null)
                txtVw_detail.setText(forecast);
        }
    }
}
