package com.example.nju_tube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("admin")&&password.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,HomePage.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),"登陆失败，请检查用户名与密码",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 设置忘记密码事件

        TextView forgot = findViewById(R.id.forgotPassword);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"我们由衷地感到遗憾",Toast.LENGTH_SHORT).show();
            }
        });

        // 设置用户注册事件
        TextView newUser = findViewById(R.id.newUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUpPage.class);
                startActivity(intent);
            }
        });
    }
}