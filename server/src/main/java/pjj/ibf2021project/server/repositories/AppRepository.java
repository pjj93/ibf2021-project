package pjj.ibf2021project.server.repositories;

import static pjj.ibf2021project.server.repositories.SQLs.SQL_INSERT_NEW_USER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppRepository {
    
    @Autowired
    private JdbcTemplate template;

    public int addNewUser(String username, String password) {
        return this.template.update(SQL_INSERT_NEW_USER, username, password);
    }
}
