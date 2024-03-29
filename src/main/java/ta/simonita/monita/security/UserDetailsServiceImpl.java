package ta.simonita.monita.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ta.simonita.monita.model.FaskesModel;
import ta.simonita.monita.repository.FaskesDb;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private FaskesDb faskesDb;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FaskesModel user = faskesDb.findByUsername(username);


        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

}
