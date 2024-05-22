package com.example.pettrackingplatform.ui.pet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pettrackingplatform.ConnectionClass;
import com.example.pettrackingplatform.MainActivity;
import com.example.pettrackingplatform.R;
import com.example.pettrackingplatform.SharedPreferencesUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class newPet extends AppCompatActivity {
    // 宣告 UI 元件
    private EditText petNameText;
    private CalendarView calendarView;
    private RadioGroup genderRadioGroup;
    private Button submitBtn;
    private Button cancelBtn;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 啟用 Edge to Edge 顯示
        EdgeToEdge.enable(this);

        // 設定此 Activity 的佈局
        setContentView(R.layout.activity_new_pet);

        // 調整視窗內嵌以改善 UI 佈局
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化 UI 元件
        petNameText = findViewById(R.id.petNameText);
        calendarView = findViewById(R.id.calendarView);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        cancelBtn = findViewById(R.id.cancelBtn);
        submitBtn = findViewById(R.id.submitBtn);

        // 設定預設性別選項為男性
        genderRadioGroup.check(R.id.male);

        // 設置取消按鈕點擊監聽器，導航回主畫面
        cancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // 設置提交按鈕點擊監聽器，啟動 AddPetTask
        submitBtn.setOnClickListener(v -> {
            new AddPetTask().execute();
        });
    }

    // 用於在背景執行資料庫操作的 AsyncTask
    private class AddPetTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // 建立資料庫連接
            ConnectionClass connectionClass = new ConnectionClass();
            Connection conn = connectionClass.CONN();

            // 建立 SQL 語句
            Statement statement = null;
            try {
                statement = conn.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // 從 SharedPreferences 獲取使用者 ID
            int userId = SharedPreferencesUtil.getUserId(newPet.this);

            // 從 UI 元件獲取寵物資料
            String name = petNameText.getText().toString();
            int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
            String gender = selectedGenderId == R.id.male ? "male" : "female";
            long date = calendarView.getDate();
            String birthday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(date));

            // 插入新的寵物記錄到資料庫
            String insertPetQuery = "INSERT INTO pet (name, gender, birthday) VALUES ('" + name + "', '" + gender + "', '" + birthday + "')";
            try {
                statement.executeUpdate(insertPetQuery, Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // 獲取新插入的寵物 ID
            ResultSet generatedKeys = null;
            try {
                generatedKeys = statement.getGeneratedKeys();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            int newPetId = 0;
            try {
                if (generatedKeys.next()) {
                    newPetId = generatedKeys.getInt(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                // 將新寵物與當前使用者關聯
                String insertUserPetQuery = "INSERT INTO user_pet (user_id, pet_id) VALUES (" + userId + ", " + newPetId + ")";
                statement.executeUpdate(insertUserPetQuery);

                return true; // 如果操作成功，返回 true

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // 根據資料庫操作結果顯示訊息
            if (result) {
                Toast.makeText(newPet.this, "新增成功", Toast.LENGTH_SHORT).show();

                // 導航到主畫面
                Intent intent = new Intent(newPet.this, MainActivity.class);
                startActivity(intent);

                // 關閉當前 Activity
                finish();
            } else {
                Toast.makeText(newPet.this, "新增失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
