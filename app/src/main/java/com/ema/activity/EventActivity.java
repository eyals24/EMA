package com.ema.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ema.R;
import com.ema.fragment.DateTimePickerFragment;


public class EventActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.eventlistsql.REPLY";

    private EditText eventName, eventPlace, eventBudget, eventDate;
    private Integer eventId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventName = findViewById(R.id.edit_event_name);
        eventPlace = findViewById(R.id.edit_event_place);
        eventDate = findViewById(R.id.edit_event_date);
        eventBudget = findViewById(R.id.edit_event_budget);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("eventDataForUpdate");
        if (bundle != null) {
            eventName.setText(bundle.getString("eventName"));
            eventPlace.setText(bundle.getString("eventPlace"));
            eventBudget.setText(bundle.getString("eventBudget"));
            eventDate.setText(bundle.getString("eventDate"));
            eventId = bundle.getInt("eventId");
        }

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePickerFragment(eventDate)
                        .show(getFragmentManager(), "date_time_picker");
            }
        });


        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(eventName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    Bundle bundle1 = new Bundle();
                    if (eventId != null)
                        bundle1.putInt("eventId", eventId);
                    bundle1.putString("eventName", eventName.getText().toString());
                    bundle1.putString("eventPlace", eventPlace.getText().toString());
                    if (!eventBudget.getText().toString().isEmpty())
                        bundle1.putInt("eventBudget",
                                Integer.valueOf(eventBudget.getText().toString()));
                    bundle1.putString("eventDate", eventDate.getText().toString());
                    replyIntent.putExtra(EXTRA_REPLY, bundle1);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
