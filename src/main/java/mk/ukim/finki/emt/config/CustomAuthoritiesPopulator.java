package mk.ukim.finki.emt.config;

import mk.ukim.finki.emt.model.jpa.User;
import mk.ukim.finki.emt.persistence.UserRepository;
import mk.ukim.finki.emt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by marijo on 09/05/17.
 */
//ldap class
public class CustomAuthoritiesPopulator implements LdapAuthoritiesPopulator {


    private UserRepository userRepository;


    public CustomAuthoritiesPopulator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
        Collection<GrantedAuthority> gas = new HashSet<GrantedAuthority>();
        User user = userRepository.findByUsername(username);
        if(user!=null)
            gas.add(new SimpleGrantedAuthority(user.type.toString()));
        else
            gas.add(new SimpleGrantedAuthority("CUSTOMER"));
        return gas;
    }
}
