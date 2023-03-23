package com.example.superheltev5.Repository;

import com.example.superheltev5.DTO.HeroFormDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBRepo {

    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String uid;

    @Value("${spring.datasource.password}")
    private String pwd;


    public ArrayList<HeroFormDTO> getAllHeroesDB() {
        ArrayList<HeroFormDTO> heroList = new ArrayList<>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/superherodb", "root", "mohamed")) {
            String SQL = "select heroID, heroName, realName, creationYEar, cityname, powerName from superhero " +
                    "left join city using (zipcode) " +
                    "left outer join superPower_superhero using(heroID) left outer join superpower using(powerID);";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            String currentName = "";
            HeroFormDTO currentDTO = null;
            while (rs.next()) {

                int id = rs.getInt("heroID");
                String heroName = rs.getString("heroName");
                String realName = rs.getString("realName");
                int creationYear = rs.getInt("creationYear");
                String cityName = rs.getString("cityName");

                if (heroName.equals(currentName)) {
                    currentDTO.addPower(rs.getString("powerName"));
                } else {
                    currentDTO = new HeroFormDTO(id, heroName, realName, creationYear, cityName, new ArrayList<>());
                    currentName = heroName;
                    currentDTO.addPower(rs.getString("powerName"));
                }
                if (!heroList.contains(currentDTO))
                    heroList.add(currentDTO);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return heroList;
    }

    public HeroFormDTO getHeroSearch(String heroName) {

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/superherodb", "root", "mohamed")) {
            String SQL = "select heroID, heroName, realName, creationYEar, cityname, powerName from superhero " +
                    "left join city using (zipcode) " +
                    "left outer join superPower_superhero using(heroID) left outer join superpower using(powerID)" +
                    "where heroName = ?";

            PreparedStatement psmt = con.prepareStatement(SQL);
            psmt.setString(1, heroName);
            ResultSet rs = psmt.executeQuery();
            String currentName = "";
            HeroFormDTO currentDTO = null;
            while (rs.next()) {
                int id = rs.getInt("heroID");
                String hName = rs.getString("heroName");
                String realName = rs.getString("realName");
                int creationYear = rs.getInt("creationYear");
                String cityName = rs.getString("cityName");
                if (heroName.equals(currentName)) {
                    currentDTO.addPower(rs.getString("powerName"));
                } else {
                    currentDTO = new HeroFormDTO(id, hName, realName, creationYear, cityName, new ArrayList<>());
                    currentName = heroName;
                    currentDTO.addPower(rs.getString("powerName"));
                }
            }
            return currentDTO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createHero(HeroFormDTO heroFormDTO) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/superherodb", "root", "mohamed")) {
            // ID's

            int cityId = 0;

            int heroId = 0;

            List<Integer> powerIDs = new ArrayList<>();

// find city_id

            String SQL1 = "select zipcode from city where cityName = ?;";

            PreparedStatement pstmt = con.prepareStatement(SQL1);

            pstmt.setString(1, heroFormDTO.getCity());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                cityId = rs.getInt("city_id");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
