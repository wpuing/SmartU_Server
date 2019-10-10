package com.jimi.su.server.entity;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 邮箱用户认证
 *@author 几米物联自动化部-韦姚忠
 *@date   2019年7月13日
 */
public class SUAuthenticator extends Authenticator {
	
    private  String  userName;
    
    private  String  password;

    public SUAuthenticator() {

    }

    public SUAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}


