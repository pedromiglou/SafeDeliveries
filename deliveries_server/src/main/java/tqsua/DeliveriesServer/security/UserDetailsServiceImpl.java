package tqsua.DeliveriesServer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.repository.RiderRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RiderRepository riderRepository;

    public UserDetailsServiceImpl() {
        // Empty Constructor
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Rider applicationUser = riderRepository.findByEmail(email);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(email);
        }

        return applicationUser;
    }
}