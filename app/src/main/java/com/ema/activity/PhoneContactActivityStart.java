package com.ema.activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ema.R;
import com.ema.model.ContactModel;
import com.ema.view.PhoneContactListAdapter;

import java.util.ArrayList;
import java.util.List;

public class PhoneContactActivityStart extends AppCompatActivity {

    private List<ContactModel> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_contact_start);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createContactsList();

        final RecyclerView recyclerView = findViewById(R.id.recyclerview_phone_contact);
        final PhoneContactListAdapter adapter = new PhoneContactListAdapter(this, contacts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createContactsList() {
        contacts = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver
                .query(ContactsContract
                                .Contacts
                                .CONTENT_URI,
                        new String[]{ContactsContract.Contacts._ID},
                        null,
                        null,
                        null);

        while (cursor.moveToNext()) {
            ContactModel contactModel = new ContactModel();
            String id = cursor
                    .getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            contactModel.setId(id);
            // projection
            String[] projection = new String[]{
                    ContactsContract
                            .CommonDataKinds
                            .StructuredName.FAMILY_NAME,
                    ContactsContract
                            .CommonDataKinds
                            .StructuredName.GIVEN_NAME,
                    ContactsContract
                            .CommonDataKinds
                            .StructuredName.DISPLAY_NAME
            };

            String where = ContactsContract
                    .Data
                    .RAW_CONTACT_ID + " = ? AND " +
                    ContactsContract
                            .Data.MIMETYPE + " = ?";

            String[] whereParams = new String[]{
                    id,
                    ContactsContract
                            .CommonDataKinds
                            .StructuredName.CONTENT_ITEM_TYPE
            };

            Cursor nameCursor = contentResolver
                    .query(ContactsContract.Data.CONTENT_URI,
                            projection,
                            where,
                            whereParams,
                            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

            if (nameCursor.moveToFirst()) {
                String given = nameCursor
                        .getString(nameCursor
                                .getColumnIndex(ContactsContract
                                        .CommonDataKinds.StructuredName.GIVEN_NAME));
                String family = nameCursor
                        .getString(nameCursor
                                .getColumnIndex(ContactsContract
                                        .CommonDataKinds.StructuredName.FAMILY_NAME));
                String display = nameCursor
                        .getString(nameCursor
                                .getColumnIndex(ContactsContract
                                        .CommonDataKinds.StructuredName.DISPLAY_NAME));
                contactModel.setFullName(display);
                contactModel.setFirstName(given);
                contactModel.setLastName(family);
            }
            nameCursor.close();

            Cursor phoneCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{id},
                    null);

            if (phoneCursor.moveToFirst()) {
                String phoneNumber = phoneCursor
                        .getString(
                                phoneCursor
                                        .getColumnIndex(
                                                ContactsContract
                                                        .CommonDataKinds
                                                        .Phone
                                                        .NUMBER));
                contactModel.setNumber(phoneNumber);
            }
            phoneCursor.close();

            Cursor emailCursor = contentResolver.query(ContactsContract
                            .CommonDataKinds
                            .Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{id}, null);

            if (emailCursor.moveToFirst()) {
                String email = emailCursor
                        .getString(emailCursor
                                .getColumnIndex(ContactsContract
                                        .CommonDataKinds.Email.DATA));
                contactModel.setEmail(email);
            }
            emailCursor.close();

            Uri contactUri = ContentUris
                    .withAppendedId(ContactsContract
                            .Contacts.CONTENT_URI, Long.valueOf(id));
            Uri photoUri = Uri
                    .withAppendedPath(contactUri, ContactsContract
                            .Contacts.Photo.CONTENT_DIRECTORY);

            try (Cursor photoCursor = getContentResolver().query(photoUri,
                    new String[]{ContactsContract.Contacts.Photo.PHOTO},
                    null,
                    null,
                    null)) {
                if (photoCursor.moveToFirst()) {
                    byte[] data = photoCursor.getBlob(0);
                    if (data != null) {
                        contactModel
                                .setBitmap(BitmapFactory
                                        .decodeByteArray(data, 0, data.length));
                    }
                }
            }

            contacts.add(contactModel);
        }
        cursor.close();
    }
}
