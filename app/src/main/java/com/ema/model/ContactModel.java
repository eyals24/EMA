package com.ema.model;

import android.graphics.Bitmap;

import java.io.Serializable;

import lombok.Data;

@Data
public class ContactModel implements Serializable {

    private String id, fullName, firstName, lastName, number, email;
    private transient Bitmap bitmap;
}
