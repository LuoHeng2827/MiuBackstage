package com.luoheng.miu.dao;

import com.luoheng.miu.bean.Discuss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DiscussDao extends Dao<Discuss> {
    private static final String SQL_ADD_DISCUSS="INSERT INTO t_discuss(author_id,content,create_date) VALUES(?,?,?)";
    private static final String SQL_FIND_DISCUSSES="SELECT * FROM t_discuss %s";
    private JdbcTemplate jdbcTemplate;

    @Override
    public Discuss add(Discuss discuss) {
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement=connection.prepareStatement(SQL_ADD_DISCUSS);
                statement.setInt(1,discuss.getAuthorId());
                statement.setString(2,discuss.getContent());
                statement.setDate(3,new Date(discuss.getCreateDate().getTime()));
                return statement;
            }
        },keyHolder);
        return null;
    }

    @Override
    public Discuss update(Discuss discuss) {
        return null;
    }

    @Override
    public void delete(Map<String, String> params) {

    }

    @Override
    public List<Discuss> find(Map<String, String> params) {
        List<Discuss> discussList=new ArrayList<>();
        String condition=generateCondition(params);
        discussList=jdbcTemplate.query(String.format(SQL_FIND_DISCUSSES, condition.toString()), new RowMapper<Discuss>() {
            @Override
            public Discuss mapRow(ResultSet resultSet, int i) throws SQLException {
                Discuss discuss=new Discuss();
                discuss.setId(resultSet.getInt("id"));
                discuss.setAuthorId(resultSet.getInt("author_id"));
                discuss.setContent(resultSet.getString("content"));
                discuss.setCreateDate(new java.util.Date(resultSet.getDate("create_date").getTime()));
                return discuss;
            }
        });
        return discussList;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
