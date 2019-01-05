package com.ema.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ema.R;
import com.ema.model.ContactModel;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PhoneContactListAdapter extends RecyclerView.Adapter<PhoneContactListAdapter.PhoneContactViewHolder> {

    public static final String EXTRA_REPLY = "com.ema.android.phone.contact.list.REPLY";

    static class PhoneContactViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvContactName, tvPhoneNumber;
        private final ImageView ivContactImage;

        private PhoneContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            ivContactImage = itemView.findViewById(R.id.ivContactImage);
        }
    }

    private final LayoutInflater inflater;
    private List<ContactModel> contactModelArrayList;

    public PhoneContactListAdapter(Context context, List<ContactModel> contactModelArrayList) {
        inflater = LayoutInflater.from(context);
        this.contactModelArrayList = contactModelArrayList;
    }

    @NonNull
    @Override
    public PhoneContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater
                .inflate(R.layout.recyclerview_phone_contact_item, parent, false);
        return new PhoneContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneContactViewHolder holder, int position) {
        if (contactModelArrayList != null) {
            final ContactModel current = contactModelArrayList.get(position);
            holder.tvContactName.setText(current.getFullName());
            holder.tvPhoneNumber.setText(current.getNumber());
            if (current.getBitmap() != null)
                holder.ivContactImage.setImageBitmap(current.getBitmap());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent replyIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("phoneContact", current);
                    replyIntent.putExtra(EXTRA_REPLY, bundle);
                    ((Activity) inflater.getContext()).setResult(RESULT_OK, replyIntent);
                    ((Activity) inflater.getContext()).finish();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contactModelArrayList != null ? contactModelArrayList.size() : 0;
    }

}
