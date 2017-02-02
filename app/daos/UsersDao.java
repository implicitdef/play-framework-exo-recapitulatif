package daos;

import objects.User;
import org.springframework.jdbc.core.JdbcTemplate;
import play.db.Database;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.List;

public class UsersDao {

    private JdbcTemplate jdbcTemplate;

    @Inject
    public UsersDao(Database database){
        this.jdbcTemplate = new JdbcTemplate(database.getDataSource());
    }

    public List<User> getUsers(){
        return jdbcTemplate.query("SELECT name, password FROM users", (ResultSet rs, int rowNum) -> {
            return new User(
                    rs.getString("name"),
                    rs.getString("password")
            );
        });
    }

}
