package com.example.sparksloginapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private LoginButton fbloginButton;
    private CircleImageView circleImageView;
    private TextView txtName, txtEmail;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbloginButton = findViewById(R.id.login_button);
        txtName = findViewById(R.id.name);
        txtEmail = findViewById(R.id.email_id);
        circleImageView = findViewById(R.id.profile_image);

        callbackManager = CallbackManager.Factory.create();

        fbloginButton.setPermissions(Arrays.asList("email", "public_profile"));
        checkLoginStatus();


        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override

            public void onSuccess(LoginResult loginResult)
            {
                AccessToken accessToken= loginResult.getAccessToken();
                    loaduserprofile(accessToken);
            }

            @Override
            public void onCancel()
            {
                Toast.makeText(MainActivity.this, "Cancelled!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error)
            {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                txtName.setText("");
                txtEmail.setText("");
                circleImageView.setImageResource(0);
                Toast.makeText(MainActivity.this, "User Logged Out", Toast.LENGTH_LONG).show();
            } else {
                loaduserprofile(currentAccessToken);
            }

        }
    };

    private void loaduserprofile(AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String name = first_name + " " + last_name;

                    String img_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    txtName.setText(first_name + " " + last_name);
                    txtEmail.setText(email);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Glide.with(MainActivity.this).load(img_url).into(circleImageView);
                    txtEmail.setText(email);
                    txtName.setText(name);

                   /* Intent i = new Intent(MainActivity.this, Homepage.class);
                    i.putExtra("name", name);
                    i.putExtra("email", email);
                    i.putExtra("img_url", img_url);

                    startActivity(i);*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }
    private void checkLoginStatus()
    {
        if(AccessToken.getCurrentAccessToken()!=null)
            loaduserprofile(AccessToken.getCurrentAccessToken());
    }
}

