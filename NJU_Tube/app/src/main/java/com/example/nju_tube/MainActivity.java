package com.example.nju_tube;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // 获取用户名与密码文本控件
        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);

        // 绑定登陆按钮监听事件
        MaterialButton loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            Handler mainHandler = new Handler();

            // 登录判断
            Thread loginThread = new Thread(() -> {
                if (userLogin(username.getText().toString(), password.getText().toString())) {
                    mainHandler.post(this::userLoginSuccess);
                } else {
                    mainHandler.post(this::userLoginFail);
                }
            });
            loginThread.start();
        });

        // 设置忘记密码事件
        TextView forgot = findViewById(R.id.forgotPassword);
        forgot.setOnClickListener(view -> Toast.makeText(getApplicationContext(),getString(R.string.forget_toast),Toast.LENGTH_SHORT).show());

        // 设置用户注册事件
        TextView newUser = findViewById(R.id.newUser);
        newUser.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,SignUpPage.class);
            startActivity(intent);
        });
    }

    private void userLoginSuccess() {
        Toast.makeText(getApplicationContext(),getString(R.string.success_login)+", "+
                ((NJUTube) getApplication()).getUserName(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,HomePage.class);
        startActivity(intent);
    }

    private void userLoginFail() {
        Toast.makeText(getApplicationContext(), getString(R.string.fail_login),Toast.LENGTH_SHORT).show();
    }

    private boolean userLogin(String userName, String passwd) {
        String loginURL = getString(R.string.server_url)+getString(R.string.login_url);
        try {
            HttpPostMultipart httpPostMultipart = new HttpPostMultipart(loginURL, "utf-8");
            httpPostMultipart.addFormField("username", userName);
            httpPostMultipart.addFormField("password", passwd);
            JSONObject jsonObject = new JSONObject(httpPostMultipart.finish());
            int statusCode = (int) jsonObject.get("status_code");
            if (statusCode != 0) {
                return false;
            }

            int userId = (int) jsonObject.get("user_id");
            String token = (String) jsonObject.get("token");
            ((NJUTube) getApplication()).setUserId(userId);
            ((NJUTube) getApplication()).setToken(token);
            ((NJUTube) getApplication()).setUserName(userName);
            return true;
        }
        catch (IOException | JSONException ignored) {
            return false;
        }
    }
}