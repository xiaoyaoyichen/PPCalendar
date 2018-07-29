package com.example.mycalendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mycalendar.db.UserRegister;
import com.example.mycalendar.util.DatePickerDialogUtils;
import com.example.mycalendar.util.EmailUtils;
import com.example.mycalendar.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private EditText passwordEdit;
    private EditText usernameEdit;
    private EditText emailadressEdit;
    private EditText confirmpassEdit;
    private Button registerBtn;
    private Button datePickerBtn;
    private String password;
    private String confirmpass;
    private String username;
    private String eamailadress;
    private String birthday;
    private String request_data;
    private DatePickerDialogUtils datePickerDialog;
    private UserRegister userRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.app_bar_log);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
        }
        initView();
    }

    //初始化界面，以及设置按钮点击事件
    private void initView(){
        passwordEdit = (EditText)findViewById(R.id.et_pwd);
        confirmpassEdit = (EditText)findViewById(R.id.et_confirmpwd);
        usernameEdit = (EditText)findViewById(R.id.et_username);
        emailadressEdit = (EditText)findViewById(R.id.et_email);
        registerBtn = (Button)findViewById(R.id.registerOK_btn);
        datePickerBtn = (Button)findViewById(R.id.datepicker_btn);
        datePickerDialog = new DatePickerDialogUtils(RegisterActivity.this);
        userRegister = new UserRegister();
        datePickerBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    //获取注册信息
    private void getRegisterInfo(){
        password = passwordEdit.getText().toString();
        confirmpass = confirmpassEdit.getText().toString();
        username = usernameEdit.getText().toString();
        eamailadress = emailadressEdit.getText().toString();
        birthday = datePickerBtn.getText().toString();
        //Log.i("RegisterActivity","getRegisterInfo");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.datepicker_btn:
                datePickerDialog.initDatePickerDialog(datePickerBtn);
                birthday = datePickerDialog.getDate();
                break;
            case R.id.registerOK_btn:
                getRegisterInfo();
                if (registerInfoJudge()){
                    userRegister.setPassword(password);
                    userRegister.setUserBirth(birthday);
                    userRegister.setUserId(eamailadress);
                    userRegister.setUserName(username);
                    Gson gson = new Gson();
                    request_data = gson.toJson(userRegister);
                    Log.i("RegisterActivity",password+ " "+birthday+" "+username+" "+eamailadress);
                    Log.i("RegisterActivity",request_data);
                    //Log.i("RegisterActivity","registerOk");
                    // Log.i("RegisterActivity","registerOk");
                    HttpUtil.sendOkHttpRequest("http://118.24.47.121/calendar/account/register.php",request_data, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("RegisterActivity","shibai");
                           // Toast.makeText(RegisterActivity.this,"网络链接失败！请稍后重试！",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.i("RegisterAcitivity","lalallalallala");
                            Log.i("RegisterActivity",response.toString());
                            //如果注册成功，就进入主界面
                            String responseData = "";
                            if (response.isSuccessful()){
                                responseData = response.body().string();
                                Log.i("Re",responseData);
                                Gson responseGson = new Gson();
                                Map<String,String> map = responseGson.fromJson(responseData,HashMap.class);
                                String code = map.get("code");
                                String msg = ( String )  map.get("msg");
                                if (code.equals("fail")) {
                                    Log.i("RegisterActivity", "onResponse: "+msg);
                                    //Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                                }
                                else if (code.equals("success")){
                                    Log.i("RegisterActivity", "onResponse: 1"+msg);
                                    //Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    finish();
                               }
                            }
                            else{
                                Log.i("RegisterActivity","接收失败");
                            }
                        }
                    });
                }
                break;
        }
    }

    //判断注册信息正误，决定是否发送服务器
    private boolean registerInfoJudge(){
        if (username == null || username.isEmpty()){
            Toast.makeText(RegisterActivity.this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (eamailadress == null || eamailadress.isEmpty()){
            Toast.makeText(RegisterActivity.this,"邮箱不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (birthday == null || birthday.isEmpty()){
            Toast.makeText(RegisterActivity.this,"出生日期不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (password == null || password.isEmpty()){
            Toast.makeText(RegisterActivity.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (confirmpass == null || confirmpass.isEmpty()){
            Toast.makeText(RegisterActivity.this,"确认密码不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!EmailUtils.isEmail(eamailadress)){
            Toast.makeText(RegisterActivity.this,"邮箱号码有误！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!confirmpass.equals(password)){
            Toast.makeText(RegisterActivity.this,"两次输入的密码不一致!",Toast.LENGTH_SHORT).show();
            return false;
        }
        //如何密码不为空，并且密码长度8-20位，就发送数据给服务器；否则，弹出对话框提示密码长度
        else{
            if (password.length()>=8&&password.length()<=20){
                Log.i("RegisterActivity","Info is right");
                return true;
            }
            else{
                final AlertDialog.Builder passRemind_dialog = new AlertDialog.Builder(RegisterActivity.this);
                passRemind_dialog.setTitle("皮皮日历");
                passRemind_dialog.setCancelable(false);
                passRemind_dialog.setMessage("密码长度有误！密码为8-20位字符！");
                passRemind_dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                passRemind_dialog.show();
                return false;
            }
        }
    }
}
