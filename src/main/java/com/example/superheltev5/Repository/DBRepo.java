package com.example.superheltev5.Repository;

import com.example.superheltev5.DTO.HeroFormDTO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBRepo {


    public ArrayList<HeroFormDTO> getAllHeroesDB() {
        ArrayList<HeroFormDTO> heroList = new ArrayList<>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/superherodb", "root", "mohamed")) {
            String SQL = "select heroID, heroName, realName, creationYEar, cityname, powerName from superhero " +
                    "left join city using (zipcode) " +
                    "left outer join superPower_superhero using(heroID) left outer join superpower using(powerID);";
            Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {

                int id = rs.getInt("heroID");
                String heroName = rs.getString("heroName");
                String realName = rs.getString("realName");
                int creationYear = rs.getInt("creationYear");
                String cityName = rs.getString("cityName");
                List<String>powerList = new ArrayList<>();
                while(rs.next()){
                powerList.add(rs.getString("powerName"));
                }
                heroList.add(new HeroFormDTO(id, heroName, realName, creationYear, cityName, powerList));
            }

            for (HeroFormDTO heroFormDTO : heroList) {
                System.out.println(heroFormDTO.getHeroName());
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return heroList;
    }
}
