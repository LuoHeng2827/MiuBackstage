package com.luoheng.miu.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.luoheng.miu.bean.User;
import com.luoheng.miu.bean.UserNotFoundException;
import com.luoheng.miu.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = Configures.MODULE_USER)
public class UserController {
    private UserService userService;
    Gson gson=new Gson();
    Logger logger=Configures.logger;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @see #RESULT_ACTIVE_USER_FAILED 表示用户激活的token错误或者已经激活
     */
    private static final int RESULT_OK=200;
    private static final int RESULT_USER_UN_ACTIVE=300;
    private static final int RESULT_USER_NON_EXISTENT=301;
    private static final int RESULT_USER_EXISTENT=302;
    private static final int RESULT_ACTIVE_USER_FAILED=303;
    private static final int RESULT_SYSTEM_ERROR=400;
    @RequestMapping(value = "/signIn",method = RequestMethod.POST)
    @ResponseBody
    public String signIn(@RequestParam(name = "Mail") String mail, @RequestParam(name = "passwords") String passwords){
        JsonObject response=new JsonObject();
        if(userService.hasUserExist(mail, passwords)){
            User user=userService.findUser(mail, passwords);
            if(user.getState()==User.State.REGISTERED){
                response.addProperty("result",RESULT_OK);
                response.addProperty("data",gson.toJson(user,User.class));
                return response.toString();
            }
            else{
                response.addProperty("result",RESULT_USER_UN_ACTIVE);
                response.addProperty("data","");
                return response.toString();
            }
        }
        else{
            response.addProperty("result",RESULT_USER_NON_EXISTENT);
            response.addProperty("data","");
            return response.toString();
        }
    }

    @RequestMapping(value = "/signUp",method = RequestMethod.POST)
    @ResponseBody
    public String signUp(@RequestParam(name = "mail")String mail,@RequestParam(name = "name")String name,
                         @RequestParam(name = "passwords")String passwords){
        logger.debug("mail: "+mail);
        JsonObject response=new JsonObject();
        if(userService.hasUserExist(mail)){
            logger.debug("test");
            User user=userService.findUser(mail, passwords);
            if(user.getState()==User.State.REGISTERED){
                response.addProperty("result",RESULT_USER_EXISTENT);
                return response.toString();
            }
            else{
                response.addProperty("result",RESULT_USER_UN_ACTIVE);
                return response.toString();
            }
        }
        else{
            User user=new User();
            user.setMail(mail);
            user.setName(name);
            user.setPasswords(passwords);
            user.setState(User.State.UNACTIVE);
            String token=userService.signUser(user);
            userService.sendActiveMail(user.getMail(),token);
            response.addProperty("result",RESULT_OK);
            return response.toString();
        }
    }

    @RequestMapping(value = "/activeUser",method = RequestMethod.GET)
    @ResponseBody
    public String activeUser(@RequestParam(name = "token")String token){
        JsonObject response=new JsonObject();
        try{
            boolean result=userService.activeUser(token);
            if(result){
                response.addProperty("result",RESULT_OK);
                return response.toString();
            }
            else{
                response.addProperty("result",RESULT_ACTIVE_USER_FAILED);
                return response.toString();
            }
        }catch(UserNotFoundException e){
            e.printStackTrace();
            response.addProperty("result",RESULT_SYSTEM_ERROR);
            return response.toString();
        }
    }

}
