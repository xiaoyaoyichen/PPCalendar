package com.example.mycalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mycalendar.db.UserRegister;
import com.example.mycalendar.util.EmailUtils;
import com.example.mycalendar.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FindpasswordActivity extends AppCompatActivity {

    private EditText emailadrEdit;
    private Button findfinishButton;
    private String emailadr;
    private UserRegister findEmail;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        Toolbar toolbar = findViewById(R.id.app_bar_log);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
        }
        initView();
    }

    private void initView(){
        pref = this.getSharedPreferences("LogIn",MODE_PRIVATE);
        editor = pref.edit();
        findEmail = new UserRegister();
        emailadrEdit = (EditText)findViewById(R.id.emailadr_Edit);
        findfinishButton = (Button)findViewById(R.id.findfinish_btn);
        findfinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailadr = emailadrEdit.getText().toString();
                if (emailadr != null && !emailadr.isEmpty() && EmailUtils.isEmail(emailadr)){
                    findEmail.setUserId(emailadr);
                    Gson gson = new Gson();
                    String emailJson = gson.toJson(findEmail);
                    Log.i("FindpasswordActivity",emailadr);
                    HttpUtil.sendOkHttpRequest("http://118.24.47.121/calendar/account/forget.php",emailJson , new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //Toast.makeText(FindpasswordActivity.this,"网络链接失败！请稍后重试！",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = "";
                            if (response.isSuccessful()){
                                responseData = response.body().string();
                                Log.i("FindpasswordActivity",responseData);
                                Gson responseGson = new Gson();
                                Map<String,String> map = responseGson.fromJson(responseData,HashMap.class);
                                String code = map.get("code");
                                String msg = ( String )  map.get("msg");
                                if (code.equals("fail")) {
                                    Log.i("FindpasswordActivity", "onResponse: "+msg);
                                    //Toast.makeText(FindpasswordActivity.this, msg, Toast.LENGTH_LONG).show();
                                }
                                else if (code.equals("success")){
                                    Log.i("FindpasswordActivity", "onResponse: 1"+msg);
                                    //Toast.makeText(FindpasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    editor.putBoolean("isLogin",false);
                                    editor.apply();
                                    finish();
                                }
                            }
                            else{
                                Log.i("FindpasswordActivity","接收失败");
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(FindpasswordActivity.this,"邮箱号码输入错误！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
