package com.scm.scm20.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="user")
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{

    @Id
     private String userId;

     @Column(nullable=false)
     private String name;

     @Column(unique=true,nullable=false)
     private String email;
     private String password;
     private String about;

    @Column(length=10000)
     private String profile;
     private String phoneNumber;

     //information
     private boolean enabled=false;
     private boolean emailVerified=false;
     private boolean phoneVerified=false;

     //self,facebook,others
     @Enumerated(value=EnumType.STRING)
     private Providers provider=Providers.SELF;
     private String provideUserId;   
     
     //add more fields 

     @OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval=true)
     private List<Contact> contacts = new ArrayList<>();


     @ElementCollection(fetch=FetchType.EAGER)
    private List<String> roleList=new ArrayList<>();


    private String emailToken;

     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {

        //list of roles(user,admin)
        //collection of SimpleGrantedAuthority [roles{admin,user}]
          Collection<SimpleGrantedAuthority> roles=roleList.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList());
          return roles;
    }
 
     @Override
     public String getUsername() {
       return this.email;
     }

     @Override
     public boolean isAccountNonExpired() {
       return true;
     }

     @Override
     public boolean isAccountNonLocked() {
       return true;
     }
     @Override
     public boolean isCredentialsNonExpired() {
       return true;
     }

     @Override
     public boolean isEnabled() {
       return this.enabled;
     }
     @Override
     public String getPassword() {
        return this.password;
     }



    
}
