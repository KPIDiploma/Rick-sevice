package com.volontyr.service;

/**
 * Created by volontyr on 15.03.17.
 */
public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
