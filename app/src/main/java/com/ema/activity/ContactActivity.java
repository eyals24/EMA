package com.ema.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ema.R;
import com.ema.R.id;
import com.ema.R.layout;
import com.ema.model.ContactModel;
import com.ema.view.PhoneContactListAdapter;

import static com.ema.activity.ContactActivityStart.CONTACT_ACTIVITY_REQUEST_CODE;

public class ContactActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.contactlistsql.REPLY";
    public static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

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

        contactFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(view.getContext(),
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ContactActivity.this,
                            Manifest.permission.READ_CONTACTS)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.wrong_date_not_saved,
                                Toast.LENGTH_LONG).show();
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(ContactActivity.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(ContactActivity.this,
                            PhoneContactActivityStart.class);
                    startActivityForResult(intent, CONTACT_ACTIVITY_REQUEST_CODE);
                }

            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra(PhoneContactListAdapter.EXTRA_REPLY);
            ContactModel contact = (ContactModel) bundle.getSerializable("phoneContact");
            if (contact != null) {
                contactFirstName.setText(contact.getFirstName());
                contactLastName.setText(contact.getLastName());
                contactPhone.setText(contact.getNumber());
                contactEmail.setText(contact.getEmail());
            }
        }
    }
}
