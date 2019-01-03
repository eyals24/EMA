package com.ema;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ema.activity.ContactActivityStart;
import com.ema.activity.EventActivityStart;
import com.ema.activity.LoginActivity;
import com.ema.activity.MissionActivityStart;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        findViewById(R.id.btn_go_to_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        ContactActivityStart.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_go_to_events).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        EventActivityStart.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_events: {
                Intent intent = new Intent(MainActivity.this,
                        EventActivityStart.class);
                startActivity(intent);
                break;
            }
            case R.id.action_missions: {
                Intent intent = new Intent(MainActivity.this,
                        MissionActivityStart.class);
                startActivity(intent);
                break;
            }
            case R.id.action_contacts: {
                Intent intent = new Intent(MainActivity.this,
                        ContactActivityStart.class);
                startActivity(intent);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
