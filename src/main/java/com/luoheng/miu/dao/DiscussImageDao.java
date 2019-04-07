package com.luoheng.miu.dao;

import com.luoheng.miu.bean.DiscussImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class DiscussImageDao extends Dao<DiscussImage> {
    private static final String SQL_ADD_DISCUSS_PICTURE="INSERT INTO t_discuss_image(discuss_id,image_url) VALUES (?,?)";
    private static final String SQL_FIND_DISCUSS_PICTURE="SELECT * FROM t_discuss_image %s";
    private JdbcTemplate jdbcTemplate;
    @Override
    public DiscussImage add(DiscussImage discussImage) {
        Object[] args={discussImage.getDiscussId(),discussImage.getImageUrl()};
        jdbcTemplate.update(SQL_ADD_DISCUSS_PICTURE,args);
        return discussImage;
    }


    @Override
    public void update(Map<String, String> tp, Map<String, String> cp) {

    }

    @Override
    public void delete(Map<String, String> params) {

    }

    @Override
    public List<DiscussImage> find(Map<String, String> params) {
        List<DiscussImage> discussPictureList;
        String condition=generateCondition(params);
        discussPictureList=jdbcTemplate.query(String.format(SQL_FIND_DISCUSS_PICTURE, condition), new RowMapper<DiscussImage>() {
            @Override
            public DiscussImage mapRow(ResultSet rs, int rowNum) throws SQLException {
                DiscussImage discussPicture=new DiscussImage();
                discussPicture.setDiscussId(rs.getString("discuss_id"));
                discussPicture.setImageUrl(rs.getString("picture_url"));
                return discussPicture;
            }
        });
        return discussPictureList;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
