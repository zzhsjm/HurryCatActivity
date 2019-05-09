package com.example.asus.ele_me;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    private UserDataManager mUserDataManager;         //用户数据管理类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        Drawable user = getResources().getDrawable(R.drawable.user);
        user.setBounds(0,0,80,80);
        mAccount.setCompoundDrawables(user,null ,null,null);

        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        Drawable pwdone = getResources().getDrawable(R.drawable.password);
        pwdone.setBounds(0,0,80,80);
        mPwd.setCompoundDrawables(pwdone,null ,null,null);

        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);
        Drawable pwdtwo = getResources().getDrawable(R.drawable.password);
        pwdtwo.setBounds(0,0,80,80);
        mPwdCheck.setCompoundDrawables(pwdtwo,null ,null,null);

        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_register_Listener);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
    }
    View.OnClickListener m_register_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:                       //确认按钮的监听事件
                    register_check();
                    break;
                case R.id.register_btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                    Intent intent_Register_to_Login = new Intent(Register.this,Login.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                    break;
            }
        }
    };
    public void register_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();
            //检查用户是否存在
            int count=mUserDataManager.findUserByName(userName);
            //用户已经存在时返回，给出提示文字
            if(count>0){
                Toast.makeText(this, "用户名已存在",Toast.LENGTH_SHORT).show();
                return ;
            }
            if(userPwd.equals(userPwdCheck)==false){     //两次密码输入不一样
                Toast.makeText(this, "密码不正确",Toast.LENGTH_SHORT).show();
                return ;
            } else {
                UserData mUser = new UserData(userName, userPwd);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mUser); //新建用户信息
                if (flag == -1) {
                    Toast.makeText(this, "建立新用户失败",Toast.LENGTH_SHORT).show();
                }else{

                    //Intent intent_Register_to_Login = new Intent(Register.this,Login.class) ;    //切换User Activity至Login Activity
                    Intent intent_Register_to_Login = new Intent();
                    intent_Register_to_Login.putExtra("username", userName);
                    intent_Register_to_Login.putExtra("password", userPwd);
                    setResult(RESULT_OK, intent_Register_to_Login);
                    //startActivity(intent_Register_to_Login);
                    Toast.makeText(this, "建立新用户成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "用户名为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, "确认密码为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}


