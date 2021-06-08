package tqsua.DeliveriesServer.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.service.RiderService;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    @Autowired
    private RiderService riderService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
        this.authenticationManager = authenticationManager;
        this.riderService= ctx.getBean(RiderService.class);
        setFilterProcessesUrl("/api/login"); 
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            Rider creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Rider.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject( ( (Rider) auth.getPrincipal() ).getEmail() )
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        HashMap<String, String> map = new HashMap<>();
        
        Rider updatedRider = (Rider) auth.getPrincipal();
        updatedRider.setStatus("Online");
        riderService.updateRider(updatedRider.getId(), updatedRider);

        map.put("id", String.valueOf(updatedRider.getId()));
        map.put("firstname", updatedRider.getFirstname());
        map.put("lastname", updatedRider.getLastname());
        map.put("email", updatedRider.getEmail());
        map.put("status", updatedRider.getStatus());
        map.put("rating", String.valueOf(updatedRider.getRating()));
        map.put("token", token);
        String json = new ObjectMapper().writeValueAsString(map);
        res.getWriter().write(json);
        res.getWriter().flush();
    }
}
