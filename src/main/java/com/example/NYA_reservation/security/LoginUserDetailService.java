package com.example.NYA_reservation.security;

import com.example.NYA_reservation.repository.UserRepository;
import com.example.NYA_reservation.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Optional<User> _user = userRepository.findByAccount(account);
        return _user.map(user -> new LoginUserDetails(user))
                .orElseThrow(() -> new UsernameNotFoundException("not found account : " + account));
    }
}
