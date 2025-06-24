package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {
    private ProfileDao profileDao;
    private UserDao userDao;
    @Autowired

    public ProfileController(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }
    @GetMapping
    public Profile getProfile(Principal principal){
       String username= principal.getName();
       User user=userDao.getByUserName(username);
       int userid= user.getId();
       return profileDao.getByUserId(userid);
    }

}
