package com.luoheng.miu.service;

import com.luoheng.miu.bean.Discuss;
import com.luoheng.miu.bean.DiscussComment;
import com.luoheng.miu.bean.DiscussLike;
import com.luoheng.miu.dao.DiscussCommentDao;
import com.luoheng.miu.dao.DiscussDao;
import com.luoheng.miu.dao.DiscussLikeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DiscussService {
    DiscussDao discussDao;
    DiscussLikeDao discussLikeDao;
    DiscussCommentDao discussCommentDao;
    public Discuss saveDiscuss(Discuss discuss){
        return discussDao.add(discuss);
    }

    public void comment(String discussId,String userId,String content){
        DiscussComment discussComment=new DiscussComment(discussId,userId,content,new Date());
        discussCommentDao.add(discussComment);
    }

    public boolean hasUserDoLike(String discussId,String userId){
        List<String> userIdList=discussLikeDao.findLikeUserId(discussId);
        if(userIdList.contains(userId)){
            return true;
        }
        return false;
    }

    public void doLike(String discussId,String userId){
        DiscussLike discussLike=new DiscussLike(discussId,userId);
        discussDao.addLikeCount(discussId);
        discussLikeDao.add(discussLike);
    }

    public List<Discuss> pushDiscussByLikeCount(){
        List<Discuss> discussList=discussDao.find(null);
        discussList.sort(new Comparator<Discuss>() {
            @Override
            public int compare(Discuss o1, Discuss o2) {
                if(o1.getLikeCount()>=o2.getLikeCount())
                    return 1;
                else
                    return -1;
            }
        });
        return discussList;
    }

    public boolean hasDiscussExist(String discussId){
        Discuss discuss=discussDao.findById(discussId);
        if(discuss==null)
            return false;
        return true;
    }

    @Autowired
    public void setDiscussDao(DiscussDao discussDao) {
        this.discussDao = discussDao;
    }

    @Autowired
    public void setDiscussLikeDao(DiscussLikeDao discussLikeDao) {
        this.discussLikeDao = discussLikeDao;
    }

    @Autowired
    public void setDiscussCommentDao(DiscussCommentDao discussCommentDao) {
        this.discussCommentDao = discussCommentDao;
    }
}
