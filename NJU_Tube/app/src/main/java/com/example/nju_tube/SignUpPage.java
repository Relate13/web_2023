package com.example.nju_tube;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
public class SignUpPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        Button signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(view -> trySignup());
    }

    private void trySignup() {
        String userName = ((EditText) findViewById(R.id.username)).getText().toString();
        String passwd = ((EditText) findViewById(R.id.password)).getText().toString();
        String confirmPasswd = ((EditText) findViewById(R.id.confirm_password)).getText().toString();
        String registerURL = getString(R.string.server_url)+getString(R.string.register_url);

        Handler mainHandler = new Handler();

        if (passwd.equals(confirmPasswd)) {
            Thread registerThread = new Thread(() -> {
                try {
                    HttpPostMultipart httpPostMultipart = new HttpPostMultipart(registerURL, "utf-8");
                    httpPostMultipart.addFormField("username", userName);
                    httpPostMultipart.addFormField("password", passwd);
                    JSONObject jsonObject = new JSONObject(httpPostMultipart.finish());

                    int statusCode = jsonObject.getJSONObject("response").getInt("status_code");
                    if (statusCode != 0) {
                        signUpFail(statusCode, mainHandler);
                        return;
                    }

                    int userId = (int) jsonObject.get("user_id");
                    String token = (String) jsonObject.get("token");
                    ((NJUTube) getApplication()).setUserId(userId);
                    ((NJUTube) getApplication()).setToken(token);
                    ((NJUTube) getApplication()).setUserName(userName);

                    signUpSuccess(mainHandler);
                }
                catch (IOException | JSONException ignored) { signUpFail(-1, mainHandler); }
            });
            registerThread.start();
        }
        else {
            signUpFail(-2, mainHandler);
        }
    }

    private void signUpSuccess(Handler mainHandler) {
        mainHandler.post(() -> {
            Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        });
    }

    private void signUpFail(int statusCode, Handler mainHandler) {
        mainHandler.post(() -> {
            switch (statusCode) {
                case -2:
                    Toast.makeText(this, getString(R.string.signup_passwd_not_match), Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(this, getString(R.string.network_hint), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(this, getString(R.string.user_exists), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(this, getString(R.string.register_token_fail), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

}