package com.luoheng.miu.service;

import com.luoheng.miu.bean.User;
import com.luoheng.miu.bean.UserNotFoundException;
import com.luoheng.miu.bean.UserRegister;
import com.luoheng.miu.dao.UserDao;
import com.luoheng.miu.dao.UserRegisterDao;
import com.luoheng.miu.mail.LMailer;
import com.luoheng.miu.mail.Mail;
import com.luoheng.miu.web.Configures;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    private UserDao userDao;
    private UserRegisterDao userRegisterDao;
    Logger logger=Configures.logger;
    private static final String MAIL_SENDER_ACCOUNT="";
    private static final String MAIL_SENDER_PASSWORDS="";

    public String signUser(User user){
        String token=UUID.randomUUID().toString().replace("-","");
        userDao.add(user);
        UserRegister userRegister=new UserRegister(user.getMail(),token);
        userRegisterDao.add(userRegister);
        return token;
    }

    public void sendActiveMail(String to,String token){
        String url=Configures.HOST+"/"+Configures.PROJECT_NAME+Configures.MODULE_USER+"/activeUser?token="+token;
        Mail mail=new Mail(MAIL_SENDER_ACCOUNT,to,"激活账户",url,MAIL_SENDER_PASSWORDS);
        LMailer.sendEmail(mail);
    }

    public boolean activeUser(String token) throws UserNotFoundException{
        UserRegister userRegister=userRegisterDao.findByToken(token);
        if(userRegister==null)
            return false;
        User user=userDao.findByMail(userRegister.getMail());
        if(user==null) {
            throw new UserNotFoundException("the user who's Mail is " + userRegister.getMail() + " is not exist!");
        }
        user.setState(User.State.REGISTERED);
        userDao.updateUserState(user);
        userRegisterDao.deleteByMail(userRegister.getMail());
        return true;
    }

    public boolean hasUserExist(String mail){
        Map<String,String> params=new HashMap<>();
        params.put("mail", mail);
        List<User> userList=userDao.find(params);
        return userList.size()>0;
    }

    public boolean hasUserExist(String mail, String passwords){
        Map<String,String> params=new HashMap<>();
        params.put("mail", mail);
        params.put("passwords",passwords);
        List<User> userList=userDao.find(params);
        return userList.size()>0;
    }

    public User findUser(String mail, String passwords){
        Map<String,String> params=new HashMap<>();
        params.put("mail", mail);
        params.put("passwords",passwords);
        List<User> userList=userDao.find(params);
        if(userList.size()>0){
            return userList.get(0);
        }
        else
            return null;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUserRegisterDao(UserRegisterDao userRegisterDao) {
        this.userRegisterDao = userRegisterDao;
    }
}
