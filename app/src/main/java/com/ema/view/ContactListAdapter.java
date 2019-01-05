package com.ema.view;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ema.R.id;
import com.ema.R.layout;
import com.ema.R.menu;
import com.ema.activity.ContactActivity;
import com.ema.activity.EventActivityStart;
import com.ema.db.entity.Contact;
import com.ema.viewmodel.ContactViewModel;

import java.util.List;

import static com.ema.activity.ContactActivityStart.CONTACT_ACTIVITY_REQUEST_CODE;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewContactFirstName, textViewContactLastName,
                textViewContactPhone, textViewContactEmail;
        private final ImageView imageViewContactMenu;


        private ContactViewHolder(View itemView) {
            super(itemView);
            textViewContactFirstName = itemView.findViewById(id.textViewContactFirstName);
            textViewContactLastName = itemView.findViewById(id.textViewContactLastName);
            textViewContactPhone = itemView.findViewById(id.textViewContactPhone);
            textViewContactEmail = itemView.findViewById(id.textViewContactEmail);
            imageViewContactMenu = itemView.findViewById(id.imageViewContactMenu);
        }
    }

    private final LayoutInflater inflater;
    private List<Contact> contacts;
    private final ContactViewModel contactViewModel;

    public ContactListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        contactViewModel = ViewModelProviders
                .of((FragmentActivity) context)
                .get(ContactViewModel.class);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater
                .inflate(layout.recyclerview_contact_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactViewHolder holder, int position) {
        if (contacts != null) {
            final Contact current = contacts.get(position);
            holder.textViewContactFirstName.setText(current.getContactFirstName());
            holder.textViewContactLastName.setText(current.getContactLastName());
            holder.textViewContactPhone.setText(current.getContactPhone());
            holder.textViewContactEmail.setText(current.getContactEmail());
            holder.imageViewContactMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(inflater.getContext(),
                            holder.imageViewContactMenu);
                    popupMenu.inflate(menu.menu_contact);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case id.contactMenuItemDelete:
                                    contactViewModel.delete(current);
                                    break;
                                case id.contactMenuItemEdit: {
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("contactId", current.getId());
                                    bundle.putString("contactFirstName", holder.textViewContactFirstName.getText().toString());
                                    bundle.putString("contactLastName", holder.textViewContactLastName.getText().toString());
                                    bundle.putString("contactPhone", holder.textViewContactPhone.getText().toString());
                                    bundle.putString("contactEmail", holder.textViewContactEmail.getText().toString());
                                    Intent intent = new Intent(v.getContext(), ContactActivity.class);
                                    intent.putExtra("contactDataForUpdate", bundle);
                                    ((Activity) v.getContext()).startActivityForResult(intent, CONTACT_ACTIVITY_REQUEST_CODE);
                                    break;
                                }
                                case id.contactMenuShowEvents: {
                                    Intent intent = new Intent(v.getContext(),
                                            EventActivityStart.class);
                                    intent.putExtra("contactId", current.getId());
                                    v.getContext().startActivity(intent);
                                    break;
                                }
                            }

                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }
}
