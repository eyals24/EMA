package com.ema.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ema.R.*;
import com.ema.fragment.DateTimePickerFragment;

public class MissionActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.missionlistsql.REPLY";

    private EditText missionName, missionPlace, missionStartDate, missionEndDate, missionDescription;
    private Long missionId, eventId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_mission);
        missionName = findViewById(id.edit_mission_name);
        missionPlace = findViewById(id.edit_mission_place);
        missionStartDate = findViewById(id.edit_mission_start_date);
        missionEndDate = findViewById(id.edit_mission_end_date);
        missionDescription = findViewById(id.edit_mission_description);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("missionDataForUpdate");
        if (bundle != null) {
            missionName.setText(bundle.getString("missionName"));
            missionPlace.setText(bundle.getString("missionPlace"));
            missionStartDate.setText(bundle.getString("missionStartDate"));
            missionEndDate.setText(bundle.getString("missionEndDate"));
            missionDescription.setText(bundle.getString("missionDescription"));
            missionId = bundle.getLong("missionId");
            eventId = bundle.getLong("eventId");
        }

        missionStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePickerFragment(missionStartDate)
                        .show(getFragmentManager(), "date_time_picker");
            }
        });

        missionEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePickerFragment(missionEndDate)
                        .show(getFragmentManager(), "date_time_picker");
            }
        });

        final Button button = findViewById(id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(missionName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    Bundle bundle1 = new Bundle();
                    if (missionId != null)
                        bundle1.putLong("missionId", missionId);
                    if (eventId != null)
                        bundle1.putLong("eventId", eventId);
                    bundle1.putString("missionName", missionName.getText().toString());
                    bundle1.putString("missionPlace", missionPlace.getText().toString());
                    bundle1.putString("missionStartDate", missionStartDate.getText().toString());
                    bundle1.putString("missionEndDate", missionEndDate.getText().toString());
                    bundle1.putString("missionDescription", missionDescription.getText().toString());
                    replyIntent.putExtra(EXTRA_REPLY, bundle1);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
