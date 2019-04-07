package com.luoheng.miu.dao;

import com.luoheng.miu.bean.DiscussLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DiscussLikeDao extends Dao<DiscussLike> {
    private static final String SQL_ADD_DO_LIKE="INSERT INTO t_discuss_do_like(discuss_id,user_mail) VALUES (?,?);";
    private static final String SQL_FIND_DISCUSS_LIKE ="SELECT * FROM t_discuss_do_like %s";
    private JdbcTemplate jdbcTemplate;

    public List<String> findLikeUserId(String discuss_id){
        List<String> userIdList=new ArrayList<>();
        Map<String,String> params=new HashMap<>();
        params.put("discuss_id",discuss_id);
        List<DiscussLike> discussLikeList=find(params);
        for(DiscussLike discussLike:discussLikeList){
            userIdList.add(discussLike.getUserMail());
        }
        return userIdList;
    }

    @Override
    public DiscussLike add(DiscussLike discussLike) {
        jdbcTemplate.update(SQL_ADD_DO_LIKE,new Object[]{discussLike.getDiscussId(),discussLike.getUserMail()});
        return discussLike;
    }

    @Override
    public void update(Map<String, String> tp, Map<String, String> cp) {

    }

    @Override
    public void delete(Map<String, String> params) {

    }

    @Override
    public List<DiscussLike> find(Map<String, String> params) {
        List<DiscussLike> discussLikeList=new ArrayList<>();
        String condition=generateCondition(params);
        discussLikeList=jdbcTemplate.query(String.format(SQL_FIND_DISCUSS_LIKE, condition), new RowMapper<DiscussLike>() {
            @Override
            public DiscussLike mapRow(ResultSet resultSet, int i) throws SQLException {
                DiscussLike discussLike=new DiscussLike();
                discussLike.setDiscussId(resultSet.getString("discuss_id"));
                discussLike.setUserMail(resultSet.getString("user_mail"));
                return discussLike;
            }
        });
        return discussLikeList;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
