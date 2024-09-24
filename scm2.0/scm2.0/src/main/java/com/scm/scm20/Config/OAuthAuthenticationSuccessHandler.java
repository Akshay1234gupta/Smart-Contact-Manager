package com.scm.scm20.Config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scm20.entities.Providers;
import com.scm.scm20.entities.User;
import com.scm.scm20.helper.AppConstants;
import com.scm.scm20.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
 
    Logger logger=LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSuccessHandler");

        //identify the provider

        var oauth2AuthenticationToken=(OAuth2AuthenticationToken) authentication;

        String authorizedClientRegisterationId=oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegisterationId);

        var oauthUser=(DefaultOAuth2User) authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key,value)->{
        logger.info("{}=>{}",key,value);
             });

        User user=new User();     
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEnabled(true);
        user.setEmailVerified(true);   
        user.setPassword("dummy");

        if(authorizedClientRegisterationId.equalsIgnoreCase("google"))
        {
        // google
        //google attributes
        user.setEmail(oauthUser.getAttribute("email").toString());
        user.setProfile(oauthUser.getAttribute("picture").toString());
        user.setName(oauthUser.getAttribute("name").toString());
        user.setProvideUserId(oauthUser.getName());
        user.setProvider(Providers.GOOGLE);
        user.setAbout("This account is created using google.");



        }
        else if(authorizedClientRegisterationId.equalsIgnoreCase("github")){
            // github
            // github attributes
            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
            : oauthUser.getAttribute("login").toString() + "@gmail.com";

            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

             user.setEmail(email);
             user.setProfile(picture);
             user.setName(name);
             user.setProvideUserId(providerUserId);
             user.setProvider(Providers.GITHUB);
             user.setAbout("This account is created using github.");

        }

        else if (authorizedClientRegisterationId.equalsIgnoreCase("linkedin")) {

        }

        else {
            logger.info("OAuthAuthenicationSuccessHandler: Unknown provider");
        }

        /* 
        DefaultOAuth2User user=(DefaultOAuth2User) authentication.getPrincipal();

        // logger.info(user.getName());

        // user.getAttributes().forEach((key,value)->{
        //     logger.info("{}=>{}",key,value);
        // });
        // logger.info(user.getAuthorities().toString());

        @SuppressWarnings("null")
        String email=user.getAttribute("email").toString();
        @SuppressWarnings("null")
        String name=user.getAttribute("name").toString();
        @SuppressWarnings("null")
        String picture=user.getAttribute("picture").toString();

        //create user and save in database
        User user1=new User();
        user1.setEmail(email);
        user1.setName(name);
        user1.setProfile(picture);
        user1.setPassword("password");
        user1.setUserId(UUID.randomUUID().toString());
        user1.setProvider(Providers.GOOGLE);
        user1.setEnabled(true);

        user1.setEmailVerified(true);
        user1.setProvideUserId(user.getName());
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        user1.setAbout("this account is created using google..");

        User user2=userRepo.findByEmail(email).orElse(null);

        if(user2==null)
        {
            userRepo.save(user1);
            logger.info("user saved:"+email);
        }
            */

        User user2=userRepo.findByEmail(user.getEmail()).orElse(null);

        if(user2==null)
        {
            userRepo.save(user);
            System.out.println("user saved:" + user.getEmail());

        }
        //data database save
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
    


}
