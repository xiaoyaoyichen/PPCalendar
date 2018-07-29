package com.example.mycalendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.example.mycalendar.db.UserRegister;
import com.example.mycalendar.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private Button register;
    private Button lostPassword;
    private CheckBox rememberPass;
    private static int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = this.getSharedPreferences("LogIn",MODE_PRIVATE);

        boolean isLogin = pref.getBoolean("isLogin",false);
        //判断登陆状态，如果已经登陆，那么直接进入calendar界面
        if (isLogin){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.app_bar_log);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
        }

        initView();
        boolean isRemember = pref.getBoolean("remember_password",false);
        if (isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
    }

    private void initView(){
        accountEdit = (EditText)findViewById(R.id.account_edit);
        passwordEdit = (EditText)findViewById(R.id.password_edit);
        rememberPass = (CheckBox)findViewById(R.id.rememberpass_checkbox);
        login = (Button)findViewById(R.id.login_button);
        register = (Button)findViewById(R.id.register_button);
        lostPassword = (Button)findViewById(R.id.forgetpass_button);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        lostPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                sendToServer();
                if (flag == 1){
                    Toast.makeText(LoginActivity.this,"网络链接失败！请稍后重试！",Toast.LENGTH_SHORT).show();
                }
                else if (flag == 2){
                    Toast.makeText(LoginActivity.this,"账号或密码错误!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_button:
                Intent intent_register = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent_register);
                break;
            case R.id.forgetpass_button:
                Intent intent_findpass = new Intent(LoginActivity.this,FindpasswordActivity.class);
                startActivity(intent_findpass);
                break;
        }
    }

    //发送账号和密码给服务器，验证账号、密码是否有错
    private void sendToServer(){
        final String account = accountEdit.getText().toString();
        final String password = passwordEdit.getText().toString();
        UserRegister userLogin = new UserRegister();
        userLogin.setPassword(password);
        //userLogin.setUserBirth(null);
        userLogin.setUserId(account);
        //userLogin.setUserName(null);
        Gson gson = new Gson();
        String request_data = gson.toJson(userLogin);
        HttpUtil.sendOkHttpRequest("http://118.24.47.121/calendar/account/login.php", request_data, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                flag = 1;
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = "";
                if (response.isSuccessful()){
                    responseData = response.body().string();
                    Log.i("LoginActivity",responseData);
                    Gson responseGson = new Gson();
                    Map<String,String> map = responseGson.fromJson(responseData,HashMap.class);
                    String code = map.get("code");
                    String msg = ( String )  map.get("msg");
                    if (code.equals("fail")) {
                        Log.i("RegisterActivity", "onResponse: "+msg);
                        flag = 2;
                    }
                    else if (code.equals("success")){
                        Log.i("LoginActivity", "onResponse: 1"+msg);
                        flag = 3;
                        editor = pref.edit();
                        if (rememberPass.isChecked()){
                            editor.putBoolean("remember_password",true);
                            editor.putString("account",account);
                            editor.putString("password",password);
                        }
                        else{
                            editor.clear();
                        }
                        editor.putBoolean("isLogin",true);
                        editor.apply();
                        Intent intent_calendar = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent_calendar);//启动CalendaActivity
                        finish();
                    }
                }
                else{
                   Log.i("LoginActivity","接收失败");
                }
            }
        });
        Log.i("sendTo","tttt");
    }

}

