package me.Cooltimmetje.StarBot.Database;

import com.zaxxer.hikari.HikariDataSource;
import me.Cooltimmetje.StarBot.Main;
import me.Cooltimmetje.StarBot.Utilities.Constants;
import me.Cooltimmetje.StarBot.Utilities.Logger;
import sx.blah.discord.handle.obj.IRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for interfacing with the locally hosted database.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class MySqlManager {

    private static HikariDataSource hikari = null;

    /**
     * Set's up the database connection.
     *
     * @param user Username of the account that should be used to login.
     * @param pass Password of the account that should be used to login.
     */
    public static void setupHikari(String user, String pass){
        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(10);

        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", "localhost");
        hikari.addDataSourceProperty("port", 3306);
        hikari.addDataSourceProperty("databaseName", "starbot");
        hikari.addDataSourceProperty("user", user);
        hikari.addDataSourceProperty("password", pass);
    }

    /**
     * Closes the database connection.
     */
    public static void disconnect(){
        hikari.close();
    }

    /**
     * Load all bot admins from the Database.
     */
    public static void loadAdmins(){
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM admins;";

        try {
            c = hikari.getConnection();
            ps = c.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()){
                Constants.admins.add(rs.getString(1));
                Logger.info("[Admins] Loaded admin: " + Main.getInstance().getStarBot().getUserByID(rs.getString(1)).getName() + " (ID: " + rs.getString(1) + ")");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Saves a game to the database.
     *
     * @param role The role that is associated with the game.
     */
    public static void addGame(IRole role){
        Connection c = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO games VALUES(?,?);";

        try {
            c = hikari.getConnection();
            ps = c.prepareStatement(query);

            ps.setString(1, role.getName());
            ps.setString(2, role.getID());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Removes a game from the database.
     *
     * @param game The name of the game that we want to delete.
     */
    public static void deleteGame(String game){
        Connection c = null;
        PreparedStatement ps = null;

        String query = "DELETE FROM games WHERE name=?;";

        try {
            c = hikari.getConnection();
            ps = c.prepareStatement(query);

            ps.setString(1, game);

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Changes the name of a game.
     *
     * @param game The name of the game that we want to edit.
     * @param newName The new name of the game.
     */
    public static void updateGame(String game, String newName){
        Connection c = null;
        PreparedStatement ps = null;

        String query = "UPDATE games SET name=? WHERE name=?;";

        try {
            c = hikari.getConnection();
            ps = c.prepareStatement(query);

            ps.setString(1, newName);
            ps.setString(2, game);

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Load all games from the database.
     */
    public static void loadGames(){
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM games;";

        try {
            c = hikari.getConnection();
            ps = c.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()){
                Constants.games.put(rs.getString(1), rs.getString(2));
                Logger.info("[Games] Loaded game: " + rs.getString(1));
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
