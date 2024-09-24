package com.scm.scm20.Controllers;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm20.entities.User;
import com.scm.scm20.helper.Message;
import com.scm.scm20.helper.MessageType;
import com.scm.scm20.repositories.UserRepo;
import com.scm.scm20.services.EmailService;

import jakarta.servlet.http.HttpSession;






@Controller
public class ForgotController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    

    Random random=new Random(1000);

    //email id from open handler

    @RequestMapping("/forgot")
    public String openEmailForm()
    {
        return "forgot_email_form";
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email,HttpSession session)
    {
        System.out.println("email:"+email);



        //generate otp of for digit

        int otp=random.nextInt(99999);

        System.out.println("otp:"+otp);

        
           //write code for send to email..

    
            User user=this.userRepo.getUserByEmail(email);

             if(user==null)
             {
                 //send error message
                 session.setAttribute("message",Message.builder()
                 .content("User does not exist with this email..")
                 .type(MessageType.red)
                 .build());
                 return "forgot_email_form";
             }
             else
            {
                String subject="OTP From SCM";
                String body="<h1> OTP = "+otp+" </h1>";
                String to=email;

                boolean flag=this.emailService.sendEmail(to, subject, body);

                if(flag)
                {
                  session.setAttribute("myotp", otp);
                  session.setAttribute("email", email);
     
                  session.setAttribute("message",Message.builder()
                         .content("We Have Send Otp to your verified Email..")
                         .type(MessageType.green)
                         .build());
                         return "verify-otp";
                     }
             else{
                     session.setAttribute("message",Message.builder()
                            .content("Enter Valid Email..")
                            .type(MessageType.red)
                            .build());
                            return "forgot_email_form";
                }
            }


          
    }


    //verify otp
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") Integer otp,HttpSession session) {

        int myotp=(int)session.getAttribute("myotp");

        String email=(String)session.getAttribute("email");

        if(myotp==otp)
        {

            Optional<User> user=this.userRepo.findByEmail(email);

            if(user==null)
            {
                //send error message
                session.setAttribute("message",Message.builder()
                .content("User does not exist with this email..")
                .type(MessageType.red)
                .build());
                return "forgot_password_form";
            }
            else{
                //send password change form
                session.setAttribute("message",Message.builder()
                    .content("Enter Otp is verified..")
                    .type(MessageType.green)
                    .build());
                return "change_password_form";
            }
        }
        else{
            session.setAttribute("message",Message.builder()
                       .content("You have entered wrong otp..")
                       .type(MessageType.red)
                       .build());
            return "verify-otp";
        }
        
    }

    //change password
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newPassword") String newPassword,HttpSession session) {
        

        String email=(String)session.getAttribute("email");
        User user=this.userRepo.getUserByEmail(email);

        user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
        this.userRepo.save(user);

        session.setAttribute("message",Message.builder()
        .content("Password updated successfuly..")
        .type(MessageType.green)
        .build());

        return "redirect:/login";

    }
    
    
}
