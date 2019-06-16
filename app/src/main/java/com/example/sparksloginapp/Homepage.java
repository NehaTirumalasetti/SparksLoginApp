package com.example.sparksloginapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.widget.LoginButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class Homepage extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        CircleImageView circleImageView;
         TextView txtName, txtEmail;
        LoginButton fbloginButton;

        fbloginButton = findViewById(R.id.login_button);
        txtName = findViewById(R.id.name);
        txtEmail = findViewById(R.id.email_id);
        circleImageView = findViewById(R.id.profile_image);

        Intent i= getIntent();
        String name = i.getStringExtra("name");
        String email = i.getStringExtra("email");
        String img_url = i.getStringExtra("img_url");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();

        Glide.with(Homepage.this).load(img_url).into(circleImageView);
        txtEmail.setText(email);
        txtName.setText(name);


    }
}
