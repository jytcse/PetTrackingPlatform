package com.example.pettrackingplatform.ui.dashboard;

import com.example.pettrackingplatform.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PetRepository {
    public static List<Pet> getPetListForUser(int userId) {
        List<Pet> petList = new ArrayList<>();
        ConnectionClass connectionClass = new ConnectionClass();
        Connection conn = connectionClass.CONN();
        if (conn == null) {
            return petList;
        }

        try {
            Statement statement = conn.createStatement();
            String query = "SELECT pet.ID, pet.name, pet.gender, pet.birthday " +
                    "FROM pet " +
                    "INNER JOIN user_pet ON pet.ID = user_pet.pet_id " +
                    "WHERE user_pet.user_id = " + userId;

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Pet pet = new Pet();
                pet.setId(resultSet.getInt("ID"));
                pet.setName(resultSet.getString("name"));
                pet.setGender(resultSet.getString("gender"));
                pet.setBirthday(resultSet.getString("birthday"));
                petList.add(pet);
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
        return petList;
    }
}
