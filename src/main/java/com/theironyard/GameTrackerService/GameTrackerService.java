package com.theironyard.GameTrackerService;

import com.theironyard.Game;

import java.sql.*;
import java.util.ArrayList;


public class GameTrackerService {

    private final Connection connection;

    public GameTrackerService(Connection connection) {
        this.connection = connection;
    }

    //init database
    public void initDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        boolean result = statement.execute("CREATE TABLE IF NOT EXISTS game (id IDENTITY, name VARCHAR , platform VARCHAR, genre VARCHAR, releaseYear INT)");
        int i = 0;

    }

    //inserts game into database
    public void insertGame(Game game) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO game VALUES (NULL, ?, ?, ?, ?)");
        statement.setString(1, game.getName());
        statement.setString(2, game.getPlatform());
        statement.setString(3, game.getGenre());
        statement.setInt(4, game.getReleaseYear());
        statement.executeUpdate();
    }

    //delete game from database
    public void deleteGame(Game game) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("DELETE FROM game WHERE id = ?");

    }

    //update game info
    public void updateGame(Game game) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("UPDATE game SET WHERE id = ?");
    }

    //select game(s) from database and return arraylist of Games
    public ArrayList<Game> selectGames() throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM game");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Game> games = new ArrayList<>();

        while(resultSet.next()){

            Game game = new Game(
                    resultSet.getString("name"),
                    resultSet.getString("platform"),
                    resultSet.getString("genre"),
                    resultSet.getInt("releaseYear")
            );

            games.add(game);

        }

        return games;

    }

}
