package pjj.ibf2021project.server.repositories;

public interface SQLs {
    public static final String SQL_INSERT_NEW_USER =
                            "insert into user (username, `password`) values (?, ?)";
    public static final String SQL_GET_USER_BY_USERNAME =
                            "select * from user where username = ?";
}
