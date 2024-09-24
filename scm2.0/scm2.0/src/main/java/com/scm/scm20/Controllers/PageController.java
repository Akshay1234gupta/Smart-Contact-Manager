package com.scm.scm20.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.scm20.entities.User;
import com.scm.scm20.forms.UserForm;
import com.scm.scm20.helper.Message;
import com.scm.scm20.helper.MessageType;
import com.scm.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;





@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    
    
    @RequestMapping("/home")
    public String home(Model model) {
    System.out.println("home page handler");

    return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model) {
    System.out.println("About page loading");

    return "about";
    }

    @RequestMapping("/services")
    public String servicesPage(Model model) {
    System.out.println("Services page loading");

    return "services";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return "contact";
    }

    //this is shwing login page
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    //registeration form
    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm=new UserForm();
        model.addAttribute("userForm",userForm);
        return "register";
    }
    

    //processing register

    @RequestMapping(value="/do-register",method=RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm,BindingResult result,HttpSession session)
    {
        System.out.println("processing");

        //fetch form data
        //userform 
        //validate from data
        if(result.hasErrors())
        {
            return "register";
        }
        //save to database


        // User user=User.builder()
        //  .name(userForm.getName())
        //  .email(userForm.getEmail())
        //  .password(userForm.getPassword())
        //  .about(userForm.getAbout())
        //  .phoneNumber(userForm.getPhoneNumber())
        //  .profile("")
        //  .build();

        User user=new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        //user.setProfile("http://127.0.0.1:5501/images/Akshay_png.png");
        user.setProfile("scm2.0\\src\\main\\resources\\static\\images\\tele.png");



        User savedUser= userService.saveUser(user);
        System.out.println("user saved:"+savedUser);

        //message
         Message message=Message.builder().content("Registration Successfull !! Check your email for verification..").type(MessageType.green).build();
         session.setAttribute("message",message);

        //redirect login page
        return "redirect:/register";
    }
    

}
