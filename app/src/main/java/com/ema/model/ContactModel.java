package com.ema.model;

import android.graphics.Bitmap;

import lombok.Data;

@Data
public class ContactModel {

    private String fullName, firstName, lastName, number, email;
    private Bitmap bitmap;
}
