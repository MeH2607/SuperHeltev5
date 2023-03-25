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

            int zipCode = 0;

            int heroID = 0;

            List<Integer> powerIDs = new ArrayList<>();

// find city_id

            String SQL1 = "select zipcode from city where cityName = ?;";

            PreparedStatement pstmt = con.prepareStatement(SQL1);

            pstmt.setString(1, heroFormDTO.getCity());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                zipCode = rs.getInt("zipCode");
            }


            // insert row in superhero table

            String SQL2 = "insert into superhero (heroName, realName, creationYear, zipCode) " +

                    "values(?, ?, ?, ?);";

            pstmt = con.prepareStatement(SQL2, Statement.RETURN_GENERATED_KEYS); // return autoincremented key

            pstmt.setString(1, heroFormDTO.getHeroName());

            pstmt.setString(2, heroFormDTO.getRealName());

            pstmt.setInt(3, heroFormDTO.getCreationYear());

            pstmt.setInt(4, zipCode);

            int rows = pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {

                heroID = rs.getInt(1);

            }

            //TODO
            // find power_ids

            String SQL3 = "select powerID from superpower where powerName = ?;";

            pstmt = con.prepareStatement(SQL3);

            for (String power : heroFormDTO.getPowerList()) {

                pstmt.setString(1, power);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    powerIDs.add(rs.getInt("powerID"));
                }

            }


            String SQL4 = "insert into superpower_superhero values (?,?);";

            pstmt = con.prepareStatement(SQL4);

            for (int i = 0; i < powerIDs.size(); i++) {

                pstmt.setInt(1, heroID);

                pstmt.setInt(2, powerIDs.get(i));

                rows = pstmt.executeUpdate();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteHero(HeroFormDTO heroFormDTO){
       int heroID = heroFormDTO.getHeroID();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/superherodb", "root", "mohamed")){
            String sql1 = "delete from superpower_superhero where heroID = ?";

            PreparedStatement pstm1 = con.prepareStatement(sql1);

            pstm1.setInt(1,heroID);

            ResultSet rs = pstm1.executeQuery();


            String sql2 = "delete from superhero where heroID = ?";

            PreparedStatement pstm2 = con.prepareStatement(sql2);

            rs = pstm2.executeQuery();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
