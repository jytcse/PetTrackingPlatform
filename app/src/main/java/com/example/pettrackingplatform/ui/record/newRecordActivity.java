package com.example.pettrackingplatform.ui.record;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
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
import com.example.pettrackingplatform.ui.dashboard.DashboardFragment;
import com.example.pettrackingplatform.ui.pet.NewPet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class newRecordActivity extends AppCompatActivity {
    // 宣告 UI 元件
    private EditText recordTypeText;
    private EditText costTextNumber;
    private Button submitBtn;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // 初始化 UI 元件
        recordTypeText = findViewById(R.id.recordTypeText);
        costTextNumber = findViewById(R.id.costTextNumber);

        cancelBtn = findViewById(R.id.cancelBtn);
        submitBtn = findViewById(R.id.submitBtn);



        // 設置取消按鈕點擊監聽器，導航回主畫面
        cancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // 設置提交按鈕點擊監聽器，啟動 AddPetTask
        submitBtn.setOnClickListener(v -> {
            new AddRecordTask().execute();
        });



    }
    private class AddRecordTask extends AsyncTask<Void, Void, Boolean> {

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

            String type = recordTypeText.getText().toString();
            int cost = Integer.parseInt(costTextNumber.getText().toString());

            Log.d("test", "doInBackground: " + type + " " + cost);

            try {
                // 插入新的健康記錄到資料庫

                String insertHealthRecordQuery = "INSERT INTO health_record (type, cost) VALUES ('"+ type +"', "+ cost +")";

                try {
                    statement.executeUpdate(insertHealthRecordQuery, Statement.RETURN_GENERATED_KEYS);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                // 獲取新插入的健康記錄 ID
                ResultSet generatedKeys = statement.getGeneratedKeys();

                int newHealthRecordId = 0;
                if (generatedKeys.next()) {
                    newHealthRecordId = generatedKeys.getInt(1);
                }
                Log.d("test", "doInBackground: "+ newHealthRecordId);

                // Get petID from Intent
                int petId = getIntent().getIntExtra("petID", -1);


                // Check if petID is valid
                if (petId == -1) {
                    // Handle invalid petID, e.g., show error message or navigate back
                    throw new RuntimeException();
                }

                // 將新寵物與新的健康記錄關聯
                String insertPetRecordQuery = "INSERT INTO pet_record (pet_id, record_id) VALUES ('" +  petId  + "', '" + newHealthRecordId + "')";
                statement.executeUpdate(insertPetRecordQuery);

                return true; // 如果操作成功，返回 true


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // 根據資料庫操作結果顯示訊息
            if (result) {
                Toast.makeText(newRecordActivity.this, "新增成功", Toast.LENGTH_SHORT).show();

                // 導航到主畫面
                Intent intent = new Intent(newRecordActivity.this, DashboardFragment.class);
                startActivity(intent);

                // 關閉當前 Activity
                finish();
            } else {
                Toast.makeText(newRecordActivity.this, "新增失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }

}