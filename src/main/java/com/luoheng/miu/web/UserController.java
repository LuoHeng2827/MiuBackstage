package com.luoheng.miu.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.luoheng.miu.Util;
import com.luoheng.miu.bean.User;
import com.luoheng.miu.bean.UserNotFoundException;
import com.luoheng.miu.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

import static com.luoheng.miu.web.Configures.*;

@Controller
@RequestMapping(value = Configures.MODULE_USER)
public class UserController {
    private UserService userService;
    private Gson gson=new Gson();
    private Logger logger=Configures.logger;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }



    @RequestMapping(value = "/signIn",method = RequestMethod.POST)
    @ResponseBody
    public String signIn(@RequestParam(name = "mail") String mail, @RequestParam(name = "passwords") String passwords){
        JsonObject response=new JsonObject();
        if(userService.hasUserAvailable(mail, passwords)){
            User user=userService.findUser(mail, passwords);
            if(user.getState()==User.State.REGISTERED){
                response.addProperty("result",RESULT_OK);
                response.addProperty("data",gson.toJson(user,User.class));
                return response.toString();
            }
            else{
                response.addProperty("result",RESULT_USER_UN_ACTIVE);
                response.addProperty("data","用户未激活");
                return response.toString();
            }
        }
        else if(userService.hasUserExist(mail)){
            response.addProperty("result",RESULT_PASSWORDS_ERROR);
            response.addProperty("data","账户或密码错误");
            return response.toString();
        }
        else{
            response.addProperty("result",RESULT_USER_NON_EXISTENT);
            response.addProperty("data","用户不存在");
            return response.toString();
        }
    }

    @RequestMapping(value = "/signUp",method = RequestMethod.POST)
    @ResponseBody
    public String signUp(@RequestParam(name = "mail")String mail,@RequestParam(name = "name")String name,
                         @RequestParam(name = "passwords")String passwords){
        JsonObject response=new JsonObject();
        if(userService.hasUserExist(mail)){
            User user=userService.findUser(mail, passwords);
            if(user.getState()==User.State.REGISTERED){
                response.addProperty("result",RESULT_USER_EXISTENT);
                response.addProperty("data","用户已注册");
                return response.toString();
            }
            else{
                response.addProperty("result",RESULT_USER_UN_ACTIVE);
                response.addProperty("data","用户已注册，需要激活");
                return response.toString();
            }
        }
        else{
            User user=new User(mail,name,passwords,User.State.UNACTIVE);
            String token=userService.signUser(user);
            userService.sendActiveMail(user.getMail(),token);
            response.addProperty("result",RESULT_OK);
            response.addProperty("data","注册成功,请在邮箱的链接激活账户");
            return response.toString();
        }
    }

    @RequestMapping(value = "/activeUser",method = RequestMethod.GET)
    @ResponseBody
    public String activeUser(@RequestParam(name = "token")String token){
        JsonObject response=new JsonObject();
        try{
            if(userService.activeUser(token)){
                response.addProperty("result",RESULT_OK);
                response.addProperty("data","激活成功");
                return response.toString();
            }
            else{
                response.addProperty("result",RESULT_ACTIVE_USER_FAILED);
                response.addProperty("data","激活失败");
                return response.toString();
            }
        }catch(UserNotFoundException e){
            e.printStackTrace();
            response.addProperty("result",RESULT_SYSTEM_ERROR);
            response.addProperty("data","系统出错，请联系管理员");
            return response.toString();
        }
    }

    @RequestMapping(value = "/uploadPic",method = RequestMethod.POST)
    @ResponseBody
    public String uploadUserPic(@RequestParam(name = "mail") String mail,
                                @RequestParam(name = "passwords") String passwords,
                                @RequestParam(name = "pic")MultipartFile pic,
                                HttpServletRequest request) throws IOException {
        String pathRoot = request.getSession().getServletContext().getRealPath("");
        JsonObject response=new JsonObject();
        if(!userService.hasUserAvailable(mail,passwords)){
            response.addProperty("result",RESULT_ACCESS_DENIED);
            response.addProperty("data","访问拒绝");
            return response.toString();
        }
        User user=userService.findUser(mail, passwords);
        String contentType=pic.getContentType();
        String path="/static/pic/"+mail+"."+contentType.substring(contentType.lastIndexOf("/")+1);
        File file=new File(Util.getRealFilePath(pathRoot+path));
        if(file.exists()){
            file.delete();
        }
        file.mkdirs();
        pic.transferTo(file);
        String url=Configures.HOST+"/"+Configures.PROJECT_NAME+Configures.MODULE_USER +"/pic/"+file.getName();
        userService.updateUserPic(user,url);
        response.addProperty("result",RESULT_OK);
        response.addProperty("data","上传成功");
        response.addProperty("pic",userService.findUser(mail, passwords).getPicUrl());
        return response.toString();
    }

}
