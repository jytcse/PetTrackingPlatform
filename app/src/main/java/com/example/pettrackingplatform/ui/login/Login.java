package com.example.pettrackingplatform.ui.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pettrackingplatform.ConnectionClass;
import com.example.pettrackingplatform.MainActivity;
import com.example.pettrackingplatform.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //取得畫面元件
        usernameEditText = findViewById(R.id.username_input);
        passwordEditText = findViewById(R.id.password_input);
        Button submitButton = findViewById(R.id.submit);

        //當登入按鈕被點擊
        submitButton.setOnClickListener(v -> {
            new LoginTask().execute();
        });
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            //建立DB連結
            ConnectionClass connectionClass = new ConnectionClass();
            Connection conn = connectionClass.CONN();

            //建立SQL語句
            Statement statement = null;
            try {
                statement = conn.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //下SQL 對比帳號與密碼
            String query = "SELECT * FROM user WHERE username='" + usernameEditText.getText().toString() + "' AND password='" + passwordEditText.getText().toString() + "'";

            //執行SQL語句
            ResultSet resultSet = null;
            try {
                resultSet = statement.executeQuery(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (resultSet.next()) {
                    // 登入成功
                    return true;
                } else {
                    // 登入失敗
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(Login.this, "登入成功", Toast.LENGTH_SHORT).show();
                //轉跳到另一個Activity
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                //關閉當前Activity
                finish();
            } else {
                Toast.makeText(Login.this, "登入失敗，請檢查帳號密碼", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
