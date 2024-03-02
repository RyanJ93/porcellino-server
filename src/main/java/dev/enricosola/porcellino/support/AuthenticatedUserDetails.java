package dev.enricosola.porcellino.support;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import dev.enricosola.porcellino.entity.User;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class AuthenticatedUserDetails implements UserDetails {
    private List<GrantedAuthority> authorityList;
    private final String username;
    private final String password;

    private void generateAuthorities(){
        this.authorityList = new ArrayList<>();
        this.authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public AuthenticatedUserDetails(User user){
        this.password = user.getPassword();
        this.username = user.getEmail();
        this.generateAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return this.authorityList;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public String getUsername(){
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
