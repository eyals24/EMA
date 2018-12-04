package com.ema.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ema.R.*;

public class ContactActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.contactlistsql.REPLY";

    private EditText contactFirstName, contactLastName, contactPhone, contactEmail;
    private Long contactId, eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_contact);
        contactFirstName = findViewById(id.edit_contact_first_name);
        contactLastName = findViewById(id.edit_contact_last_name);
        contactPhone = findViewById(id.edit_contact_phone);
        contactEmail = findViewById(id.edit_contact_email);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("contactDataForUpdate");
        if (bundle != null) {
            contactFirstName.setText(bundle.getString("contactFirstName"));
            contactLastName.setText(bundle.getString("contactLastName"));
            contactPhone.setText(bundle.getString("contactPhone"));
            contactEmail.setText(bundle.getString("contactEmail"));
            contactId = bundle.getLong("contactId");
            eventId = bundle.getLong("eventId");
        }

        final Button button = findViewById(id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(contactFirstName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    Bundle bundle1 = new Bundle();
                    if (contactId != null)
                        bundle1.putLong("contactId", contactId);
                    if (eventId != null)
                        bundle1.putLong("eventId", eventId);
                    bundle1.putString("contactFirstName", contactFirstName.getText().toString());
                    bundle1.putString("contactLastName", contactLastName.getText().toString());
                    bundle1.putString("contactPhone", contactPhone.getText().toString());
                    bundle1.putString("contactEmail", contactEmail.getText().toString());
                    replyIntent.putExtra(EXTRA_REPLY, bundle1);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
