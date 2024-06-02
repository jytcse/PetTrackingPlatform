package com.example.pettrackingplatform.ui.record;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pettrackingplatform.R;
import com.example.pettrackingplatform.ui.pet.NewPet;

import java.util.List;

public class RecordList extends AppCompatActivity {

    private int petId;
    private RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.record_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordAdapter = new RecordAdapter();
        recyclerView.setAdapter(recordAdapter);

        // Get petID from Intent
        petId = getIntent().getIntExtra("petID", -1);

        // Check if petID is valid
        if (petId == -1) {
            // Handle invalid petID, e.g., show error message or navigate back
            Toast.makeText(this, "Invalid Pet ID", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Fetch record data based on petID
        fetchRecordData();

        // Set up button to navigate to NewRecordActivity
        Button createButton = findViewById(R.id.newRecordBtn);
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecordList.this, newRecordActivity.class);
            intent.putExtra("petID",petId);
            startActivity(intent);
        });
    }

    private void fetchRecordData() {
        // Use AsyncTask to fetch data asynchronously
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
            // Fetch record data from RecordRepository
            return RecordRepository.getRecordsForPet(petId);
        }

        @Override
        protected void onPostExecute(List<Record> records) {
            if (records != null) {
                // Update adapter with fetched data
                recordAdapter.setRecords(records);
            }
        }
    }
}
