package com.example.pettrackingplatform.ui.record;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pettrackingplatform.R;

import java.util.List;

public class RecordList extends AppCompatActivity {

    private int petId;
    private RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        // 初始化 RecyclerView
        RecyclerView recyclerView = findViewById(R.id.record_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordAdapter = new RecordAdapter();
        recyclerView.setAdapter(recordAdapter);

        // 从 Intent 中获取 petID
        petId = getIntent().getIntExtra("petID", -1);
        //Toast.makeText("",Toast.LENGTH_LONG);

        // 检查 petID 是否有效
        if (petId == -1) {
            // 在这里处理无效的 petID，例如显示错误消息或跳转回前一页
            return;
        }

        // 根据 petID 获取记录数据
        fetchRecordData();
    }

    private void fetchRecordData() {
        // 使用 AsyncTask 异步获取数据
        new FetchRecordDataTask(petId, recordAdapter).execute();
    }

    private static class FetchRecordDataTask extends AsyncTask<Void, Void, List<Record>> {
        private int petId;
        private RecordAdapter recordAdapter;

        public FetchRecordDataTask(int petId, RecordAdapter recordAdapter) {
            this.petId = petId;
            this.recordAdapter = recordAdapter;
        }

        @Override
        protected List<Record> doInBackground(Void... voids) {
            // 从 RecordRepository 获取记录数据
            return RecordRepository.getRecordsForPet(petId);
        }

        @Override
        protected void onPostExecute(List<Record> records) {
            if (records != null) {
                // 更新适配器的数据
                recordAdapter.setRecords(records);
            }
        }
    }
}
