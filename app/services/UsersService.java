package services;

import com.fasterxml.jackson.databind.JsonNode;
import objects.User;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class UsersService {

    private List<User> allUsers = new ArrayList<>();

    @Inject
    private WSClient wsClient;


    public UsersService() {
        allUsers.add(new User("Emmanuel", "123"));
        allUsers.add(new User("Berta", "123"));
        allUsers.add(new User("Donald", "123"));
    }

    public List<User> getAllUsers() {
        return allUsers;
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
                    users.addAll(allUsers);
                    return users;
                } else {
                    throw new RuntimeException("Bad response");
                }
            });
    }

    public Boolean isValidUser(User user){
        return allUsers.contains(user);
    }


}
