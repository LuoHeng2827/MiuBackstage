package com.luoheng.miu.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.luoheng.miu.Util;
import com.luoheng.miu.bean.Discuss;
import com.luoheng.miu.bean.DiscussImage;
import com.luoheng.miu.dao.DiscussImageDao;
import com.luoheng.miu.service.DiscussService;
import com.luoheng.miu.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = Configures.MODULE_DISCUSS)
public class DiscussController {
    private static final int RESULT_OK=200;
    private static final int RESULT_REPEAT_DO_LIKE=300;
    private static final int RESULT_USER_NON_EXISTENT=301;
    private static final int RESULT_DISCUSS_NON_EXISTENT=302;
    private static final int RESULT_ACCESS_DENIED=303;
    private UserService userService;
    private DiscussService discussService;
    private DiscussImageDao discussImageDao;
    private Gson gson=new Gson();
    private Logger logger=Configures.logger;

    @RequestMapping(value = "/push",method = RequestMethod.GET)
    @ResponseBody
    public String push(){
        JsonObject response=new JsonObject();
        List<Discuss> discussList=discussService.pushDiscussByLikeCount();
        String data=gson.toJson(discussList);
        response.addProperty("result",RESULT_OK);
        response.addProperty("data",data);
        return response.toString();
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam(name = "authorMail")String authorMail,
                         @RequestParam(name = "passwords")String passwords,
                         @RequestParam(name = "content")String content,
                         @RequestParam(name = "images",required = false)MultipartFile[] images,
                         HttpServletRequest request) throws IOException {
        JsonObject response=new JsonObject();
        String pathRoot = request.getSession().getServletContext().getRealPath("");
        if(!userService.hasUserExist(authorMail,passwords)){
            response.addProperty("result",RESULT_ACCESS_DENIED);
            return response.toString();
        }
        Discuss discuss=new Discuss(authorMail,content,new Date());
        discuss=discussService.saveDiscuss(discuss);
        for(MultipartFile image:images){
            if(!image.isEmpty()){
                String fileName=image.getOriginalFilename();
                String path="/static/images/"+ authorMail +"/"+discuss.getId()+"/"+fileName;
                File file=new File(Util.getRealFilePath(pathRoot+path));
                file.mkdirs();
                image.transferTo(file);
                discussImageDao.add(new DiscussImage(discuss.getId(),pathRoot+path));
            }
        }
        response.addProperty("result",RESULT_OK);
        response.addProperty("data","上传成功");
        return response.toString();
    }

    @RequestMapping(value = "/doLike",method = RequestMethod.POST)
    @ResponseBody
    public String doLike(@RequestParam(name = "discussId")String discussId,
                         @RequestParam(name = "userMail")String userMail,
                         @RequestParam(name = "passwords") String passwords){
        JsonObject response=new JsonObject();
        if(!userService.hasUserExist(userMail,passwords)){
            response.addProperty("result",RESULT_ACCESS_DENIED);
            response.addProperty("data","访问拒绝");
            return response.toString();
        }
        if(discussService.hasUserDoLike(discussId, userMail)){
            response.addProperty("result",RESULT_REPEAT_DO_LIKE);
            response.addProperty("data","已经点过赞了");
            return response.toString();
        }
        else{
            discussService.doLike(discussId, userMail);
            response.addProperty("result",RESULT_OK);
            response.addProperty("data","点赞成功");
            return response.toString();
        }
    }

    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    @ResponseBody
    public String comment(@RequestParam(name = "discussId")String discussId,
                          @RequestParam(name = "userMail")String userId,
                          @RequestParam(name = "passwords")String passwords,
                          @RequestParam(name = "content")String content){
        JsonObject response=new JsonObject();
        if(!userService.hasUserExist(userId,passwords)){
            response.addProperty("result",RESULT_ACCESS_DENIED);
            return response.toString();
        }
        if(!discussService.hasDiscussExist(discussId)){
            response.addProperty("result",RESULT_DISCUSS_NON_EXISTENT);
            return response.toString();
        }
        discussService.comment(discussId, userId, content);
        response.addProperty("result",RESULT_OK);
        return response.toString();
    }

    @Autowired
    public void setDiscussService(DiscussService discussService) {
        this.discussService = discussService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDiscussImageDao(DiscussImageDao discussImageDao) {
        this.discussImageDao = discussImageDao;
    }
}
