package com.theironyard.GameTrackerService;

import com.theironyard.Game;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class GameTrackerService {

    private final Connection connection;

    public GameTrackerService(Connection connection) {
        this.connection = connection;
    }

    //init database
    public void initDatabase() throws SQLException {
        Statement statement = connection.createStatement();
    }

    /*

    //inserts game into database
    public insertGame(Game game) throws SQLException {

    }

    //delete game from database
    public deleteGame(Game game) throws SQLException{

    }

    //select game(s) from database
    public selectGame(Game game) throws SQLException{

    }
    */
}
