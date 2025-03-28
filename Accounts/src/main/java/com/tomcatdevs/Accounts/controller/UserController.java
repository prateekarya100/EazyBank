package com.tomcatdevs.Accounts.controller;

import com.tomcatdevs.Accounts.controller.JWT.User;
import com.tomcatdevs.Accounts.controller.JWT.UserService;
import com.tomcatdevs.Accounts.service.Impl.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class UserController {

    @Autowired
    private UserService userService;

//  http://localhost:8081/users
    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    //  http://localhost:8081/loggedin-user
    @GetMapping(value = "/loggedin-user")
    public String getLoggedInUser(Principal principal){
        return principal.getName();
    }


}
