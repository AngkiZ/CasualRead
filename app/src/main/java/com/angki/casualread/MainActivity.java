package com.angki.casualread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView title;

    private Button menu;

    private Button more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.title_textView);
        menu = (Button) findViewById(R.id.menu_button);
        more = (Button) findViewById(R.id.more_button);
    }
}
