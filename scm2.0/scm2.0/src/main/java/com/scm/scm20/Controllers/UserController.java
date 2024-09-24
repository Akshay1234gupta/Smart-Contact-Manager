package com.scm.scm20.Controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm20.helper.Message;
import com.scm.scm20.helper.MessageType;
import com.scm.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {
    
    private Logger logger=LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    //user dashboard page

    @RequestMapping(value="/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }

    //user profile page
    @RequestMapping(value="/profile")
    public String userProfile(Model model,Authentication authentication) {

       
        return "user/profile";
    }
    

    //user add contacts page

    //open settings handler


    @GetMapping("/settings")
    public String openSetting()
    {
        return "user/settings";
    }

    //change password handler
    
   @PostMapping("/change-password")
    public String changePassword(HttpSession session,@RequestParam String oldPassword, @RequestParam String newPassword, Authentication authentication) {
        String username = authentication.getName(); // Get the current logged-in user's username
        boolean isPasswordChanged = userService.changePassword(username, oldPassword, newPassword);

        if (isPasswordChanged) {
             session.setAttribute("message",Message.builder()
             .content("You have successfully changed a password..")
            .type(MessageType.green)
            .build());
        } else {
            session.setAttribute("message",Message.builder()
            .content("password does not match..")
           .type(MessageType.green)
           .build());
        }
        return "redirect:/user/settings";
    }
}
