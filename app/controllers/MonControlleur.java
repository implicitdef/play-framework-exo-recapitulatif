package controllers;

import objects.User;
import play.Configuration;
import play.data.Form;
import play.data.FormFactory;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import services.UsersService;
import views.html.accueil;
import views.html.login;
import views.html.users;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class MonControlleur extends Controller {

    private static String USERNAME_SESSION_KEY = "username";

    @Inject
    private FormFactory formFactory;

    @Inject
    private UsersService usersService;

    @Inject
    private Configuration configuration;

    @Inject
    private WSClient wsClient;

    public Result index() {
        String username = session(USERNAME_SESSION_KEY);
        int nbExclamationPoints = configuration.getInt("welcomeMessage.nbExclamationPoints");
        String title = "Bievenue ";
        for (int i = 0; i < nbExclamationPoints; i++){
            title += "!";
        }
        return ok(accueil.render(username, title));
    }

    public Result login() {
        String username = session(USERNAME_SESSION_KEY);
        if (username != null){
            return redirect(routes.MonControlleur.index());
        }
        Form<User> userForm = formFactory.form(User.class);
        return ok(login.render(null, userForm));
    }

    public Result loginSubmit() {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        if (userForm.hasErrors()) {
            return badRequest(login.render(null, userForm));
        }
        User user = userForm.get();
        if (usersService.isValidUser(user)) {
            session(USERNAME_SESSION_KEY, user.getName());
            return redirect(routes.MonControlleur.index());
        }
        userForm.reject("Invalid login");
        return badRequest(login.render(null, userForm));
    }

    public CompletionStage<Result> users(){
        String username = session(USERNAME_SESSION_KEY);
        if (username == null){
            Result myRedirect = redirect(routes.MonControlleur.index());
            return CompletableFuture.completedFuture(myRedirect);
        }
        return usersService.getAllUsers().thenApply((usersToDisplay) -> {
            return ok(users.render(username, usersToDisplay));
        });
    }

    public Result logout(){
        session().remove(USERNAME_SESSION_KEY);
        return redirect(routes.MonControlleur.index());
    }


}

