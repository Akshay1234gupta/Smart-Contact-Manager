package com.scm.scm20.helper;



import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    

    public static String getEmailOfLoggedInUser(Authentication authentication)

    {

        //agar email is password se login kiya hai to : email kaise nikalenge

        if(authentication instanceof  OAuth2AuthenticationToken)
        {

            var aOAuth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;
            var clientId=aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User=(OAuth2User)authentication.getPrincipal();
            String username="";
            //sign with google

            if(clientId.equalsIgnoreCase("google"))
            {
                System.out.println("Getting email from google");
                username=oauth2User.getAttribute("email").toString();
            }

            //sign with github
            else if(clientId.equalsIgnoreCase("github"))
            {
                System.out.println("Getting email from github");

                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
                : oauth2User.getAttribute("login").toString() + "@gmail.com";
            }
        return username;
    }
    else{
        System.out.println("getting data from loacl database");
        return authentication.getName();
    }
    }


    public static String getLinkForEmailVerification(String emailToken)
    {

        String link="http://127.0.0.1:8080/auth/verify-email?token="+emailToken;

        return link;
    }



}
