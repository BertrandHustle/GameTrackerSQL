package com.theironyard;

import com.theironyard.GameTrackerService.GameTrackerService;
import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.halt;

public class Main {

    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) throws SQLException{

        //creates server
        Server server = Server.createTcpServer("-baseDir", "./data").start();

        //creates connection
        String jdbcUrl = "jdbc:h2:" + server.getURL() + "/main";
        System.out.println(jdbcUrl);
        Connection connection = DriverManager.getConnection(jdbcUrl, "", null);

        //creates/configures web service
        GameTrackerService gts =  new GameTrackerService(connection);

        //init database
        gts.initDatabase();

        //Spark.init();
        Spark.get(
                "/",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());

                    //returns arraylist of all games

                    ArrayList<Game> games = new ArrayList<>();
                    games = gts.selectGames();

                    HashMap m = new HashMap<>();
                    if (user == null) {
                        return new ModelAndView(m, "login.mustache");
                    }
                    else {
                        m.put("games", games);
                        m.put("user", user);
                        return new ModelAndView(m, "home.mustache");
                    }
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    User user = users.get(name);
                    if (user == null) {
                        user = new User(name);
                        users.put(name, user);
                    }

                    Session session = request.session();
                    session.attribute("userName", name);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/create-game",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());
                    if (user == null) {
                        //throw new Exception("com.theironyard.User is not logged in");
                        halt(403);
                    }

                    int gameId = 0;
                    String gameName = request.queryParams("gameName");
                    String gameGenre = request.queryParams("gameGenre");
                    String gamePlatform = request.queryParams("gamePlatform");
                    int gameYear = Integer.valueOf(request.queryParams("gameYear"));
                    Game game = new Game(gameId, gameName, gameGenre, gamePlatform, gameYear);

                    //inserts game into database
                    gts.insertGame(game);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/delete",
                (request, response) -> {

                    int ID = Integer.parseInt(request.queryParams("id"));
                    gts.deleteGame(ID);
                    response.redirect("/");
                    halt();
                    return null;
                }
        );

        Spark.post(
                "/edit",
                (request, response) -> {

                    int ID = Integer.parseInt(request.queryParams("id"));
                    String name = request.queryParams("name");
                    String genre = request.queryParams("genre");
                    String platform = request.queryParams("platform");
                    int releaseYear = Integer.parseInt(request.queryParams("releaseYear"));

                    gts.updateGame(ID, name, genre, platform, releaseYear);
                    response.redirect("/");
                    halt();
                    return null;

                }

        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );

        Spark.get(
                "/search",
                (request, response) -> {
                    HashMap hash = new HashMap();

                    String query = request.queryParams("search");
                    ArrayList<Game> searchGame = new ArrayList<>();

                    searchGame = gts.searchGame(query);

                    hash.put("games", searchGame);
                    return new ModelAndView(hash, "home.mustache");
                },
        new MustacheTemplateEngine()
        );
    }

    static User getUserFromSession(Session session) {
        String name = session.attribute("userName");
        return users.get(name);
    }
}
