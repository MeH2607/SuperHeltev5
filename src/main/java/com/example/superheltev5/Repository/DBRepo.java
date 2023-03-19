package com.example.superheltev5.Repository;

import com.example.superheltev5.DTO.HeroFormDTO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class DBRepo {


    public ArrayList<HeroFormDTO> getAllHeroesDB() {
        ArrayList<HeroFormDTO> heroList = new ArrayList<>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/superherodb", "root", "mohamed")) {
            String SQL = "select heroID, heroName, realName, creationYEar, city, power from superhero left outer join c";
            Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {

                int id = rs.getInt("heroID");
                String heroName = rs.getString("heroName");
                String realName = rs.getString("realName");
                int creationYear = rs.getInt("creationYear");
                heroList.add(new HeroFormDTO(id, heroName, realName, creationYear));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return heroList;
    }
}
