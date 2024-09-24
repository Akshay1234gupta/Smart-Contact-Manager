package com.scm.scm20.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.scm20.entities.User;
import com.scm.scm20.helper.AppConstants;
import com.scm.scm20.helper.Helper;
import com.scm.scm20.helper.ResourceNotFoundException;
import com.scm.scm20.repositories.UserRepo;
import com.scm.scm20.services.EmailService;
import com.scm.scm20.services.UserService;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private EmailService emailService;

    private Logger logger=LoggerFactory.getLogger(this.getClass());


    @Override
    public User saveUser(User user) {

        //user id have to generate
        String userId=UUID.randomUUID().toString();
        user.setUserId(userId);

        //passsword encode
        //user.setPassword(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //set the user role
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        logger.info(user.getProvider().toString());



        String emailToken=UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User savedUser= userRepo.save(user);
        String emailLink=Helper.getLinkForEmailVerification(emailToken);
        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart Contact Manager", emailLink);

        return savedUser;

    }



    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }



    @Override
    public Optional<User> updateUser(User user) {
        User user2=userRepo.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("user not found"));

        //update karenge user2 from user
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfile(user.getProfile());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProvideUserId(user.getProvideUserId());

        //save thr user in database

        User save=userRepo.save(user2);
        return Optional.ofNullable(save);

    }


    @Override
    public void deleteUser(String id) {
        User user2=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found"));

        userRepo.delete(user2);
    }



    @Override
    public boolean isUserExist(String userId) {
        User user2=userRepo.findById(userId).orElse(null);
        return user2 != null ? true : false;

    }


    @Override
    public boolean isUserExistByEmail(String email) {
        User user=userRepo.findByEmail(email).orElse(null);
        return user != null ? true : false;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }
    @Autowired
    private SecurityCustomUserDetailService userDetailsService;
    


    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // Load the user from the database
        User user = (User) userDetailsService.loadUserByUsername(username);

        // Check if old password matches
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            // Encode and set the new password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            return true; // Password change successful
        } else {
            return false; // Old password does not match
        }
    }
    
}
