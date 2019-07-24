package com.secretsanta.securitytemplate.configuration;

import com.secretsanta.securitytemplate.models.Role;
import com.secretsanta.securitytemplate.models.User;
import com.secretsanta.securitytemplate.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUSD implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    public SSUSD(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            User thisUser = userRepo.findByUsername(username);
            if (thisUser == null){
                return null;
            }
            return new org.springframework.security.core.userdetails.User(
                    thisUser.getUsername(),
                    thisUser.getPassword(),
                    getAuthorites(thisUser));
        }catch (Exception e){
            throw new UsernameNotFoundException("User Not Found");
        }
    }

    private Set<GrantedAuthority> getAuthorites(User thisUser){
        Set<GrantedAuthority>authorities = new HashSet<>();
        for(Role eachRole : thisUser.getRoles()){
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(eachRole.getRoleName());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}
