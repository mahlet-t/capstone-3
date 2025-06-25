package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {
    private ProfileDao profileDao;
    private UserDao userDao;
    @Autowired

    public ProfileController(ProfileDao profileDao,UserDao userDao) {
        this.profileDao = profileDao;
       this.userDao=userDao;
    }
    @GetMapping
    public Profile getProfile(Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userid = user.getId();
           return profileDao.getByUserId(userid);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops....our bad ");
        }
    }
    @PostMapping
    public Profile creatProfile(Profile profile,Principal principal){
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userid = user.getId();
            profile.setUserId(userid);
            return profileDao.create(profile);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Oops...our bad");
        }
    }
    @PutMapping
    public void updateProfile(Profile profile,Principal principal){
        try {
            String username= principal.getName();
            User user=userDao.getByUserName(username);
            int userid= user.getId();
            profileDao.update(profile,userid);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Oops ....Our bad");
        }
    }

}
