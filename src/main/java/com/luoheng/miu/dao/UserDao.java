package com.luoheng.miu.dao;

import com.luoheng.miu.bean.User;
import com.luoheng.miu.web.Configures;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao extends Dao<User> {
    private static final String SQL_ADD_USER="INSERT INTO t_user(name,mail,passwords,state) VALUES (?,?,?,?)";
    private static final String SQL_UPDATE_USER="UPDATE t_user SET name=?,mail=?,passwords=?,state=? WHERE id=?";
    private static final String SQL_FIND_USERS="SELECT * FROM t_user %s";
    private static String SQL_MATCH_EXIST_USER_COUNT="select count(*) from t_user where Mail=? and passwords=?";
    private JdbcTemplate jdbcTemplate;
    private Logger logger=Configures.logger;

    public User findByMail(String mail){
        Map<String,String> params=new HashMap<>();
        params.put("mail",mail);
        List<User> userList=find(params);
        if(userList.size()>0)
            return userList.get(0);
        return null;
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement=connection.prepareStatement(SQL_ADD_USER,Statement.RETURN_GENERATED_KEYS);
                statement.setString(1,user.getName());
                statement.setString(2,user.getMail());
                statement.setString(3,user.getPasswords());
                statement.setString(4,user.getStateString());
                return statement;
            }
        },keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User update(User user) {
        Object[] args={user.getName(),user.getMail(),user.getPasswords(),user.getStateString(),user.getId()};
        jdbcTemplate.update(SQL_UPDATE_USER,args);
        return user;
    }

    @Override
    public void delete(Map<String, String> params) {

    }

    @Override
    public List<User> find(Map<String, String> params) {
        List<User> userList=new ArrayList<>();
        String condition=generateCondition(params);
        logger.debug(String.format(SQL_FIND_USERS, condition));
        userList=jdbcTemplate.query(String.format(SQL_FIND_USERS, condition), new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user=new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setMail(resultSet.getString("Mail"));
                user.setPasswords(resultSet.getString("passwords"));
                user.setState(resultSet.getString("state"));
                return user;
            }
        });
        return userList;
    }
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
