package tqsua.OrdersServer.security;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tqsua.OrdersServer.model.User;
import tqsua.OrdersServer.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl() {}

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User applicationUser = userRepository.findByEmail(email);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(email);
        }

        return applicationUser;
    }
}