package pjj.ibf2021project.server.repositories;

import static pjj.ibf2021project.server.repositories.SQLs.SQL_INSERT_NEW_USER;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppRepository {
    
    @Autowired
    private JdbcTemplate template;

    private Logger logger = Logger.getLogger(AppRepository.class.getName());

    public boolean insertUser(String username, String password) {
        return template.update(SQL_INSERT_NEW_USER, username, password) > 0;
    }
}
