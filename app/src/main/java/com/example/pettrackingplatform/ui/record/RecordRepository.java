package com.example.pettrackingplatform.ui.record;

import com.example.pettrackingplatform.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecordRepository {
    public static List<Record> getRecordsForPet(int petId) {
        List<Record> recordList = new ArrayList<>();
        ConnectionClass connectionClass = new ConnectionClass();
        Connection conn = connectionClass.CONN();
        if (conn == null) {
            return recordList;
        }

        try {
            Statement statement = conn.createStatement();
            String query = "SELECT hr.* " +
                    "FROM pet_record pr " +
                    "JOIN health_record hr ON pr.record_id = hr.ID " +
                    "JOIN user_pet up ON pr.pet_id = up.pet_id " +
                    "WHERE pr.pet_id = " + petId + ";";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Record record = new Record();
                record.setId(resultSet.getInt("ID"));
                record.setType(resultSet.getString("type"));
                record.setCost(resultSet.getInt("cost"));
                recordList.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return recordList;
    }
}
