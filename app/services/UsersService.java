package services;

import com.fasterxml.jackson.databind.JsonNode;
import daos.UsersDao;
import objects.User;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class UsersService {

    @Inject
    private WSClient wsClient;

    @Inject
    private UsersDao usersDao;


    public List<User> getAllUsers() {
        return usersDao.getUsers();
    }

    public CompletionStage<List<User>> getAllUsersWithApiCall() {
        return wsClient
            .url("https://jsonplaceholder.typicode.com/users")
            .get()
            .thenApply((wsResponse) -> {
                if (wsResponse.getStatus() >= 200 && wsResponse.getStatus() < 300){
                    JsonNode json = wsResponse.asJson();
                    List<User> users = new ArrayList<>();
                    for (JsonNode userJson : json){
                        String name = userJson.path("name").asText();
                        User user = new User(name, "passwordDéfaut");
                        users.add(user);
                    }
                    users.addAll(getAllUsers());
                    return users;
                } else {
                    throw new RuntimeException("Bad response");
                }
            });
    }

    public Boolean isValidUser(User user){
        return getAllUsers().contains(user);
    }


}
