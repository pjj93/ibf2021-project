package pjj.ibf2021project.server.repositories;

import static pjj.ibf2021project.server.repositories.SQLs.SQL_GET_USER_BY_USERNAME_AND_PASSWORD;
import static pjj.ibf2021project.server.repositories.SQLs.SQL_INSERT_NEW_USER;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class AppRepository {
    
    @Autowired
    private JdbcTemplate template;

    private Logger logger = Logger.getLogger(AppRepository.class.getName());

    public boolean insertUser(String username, String password) {
        return template.update(SQL_INSERT_NEW_USER, username, password) > 0;
    }

    public boolean hasUser(String username, String password) {
        final SqlRowSet rs = template.queryForRowSet(SQL_GET_USER_BY_USERNAME_AND_PASSWORD, username, password);
        if (rs.next())
            return true;
        return false;
    }

    // public boolean hasUser(String username) {
	// 	final SqlRowSet rs = template.queryForRowSet(
	// 			SQL_GET_USER_BY_USERNAME, username);
	// 	if (rs.next())
	// 		return rs.getInt("user_count") > 0;
	// 	return false;
	// }
}
