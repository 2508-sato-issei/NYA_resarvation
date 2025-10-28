package com.example.NYA_reservation.security;

import com.example.NYA_reservation.repository.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
//DBのUserをSpring Security形式に変換
public class LoginUserDetails implements UserDetails {
    private final Integer id;
    private final String account;
    private final String password;
    private final String name;
    private final Integer authority;
    private final boolean isStopped;
    private final Collection<? extends GrantedAuthority> authorities;

    public LoginUserDetails(User user){
        this.id = user.getId();
        this.account = user.getAccount();
        this.password = user.getPassword();
        this.name = user.getName();
        this.authority = user.getAuthority();
        this.isStopped = user.isStopped();

        // 権限判定
        if (authority == 1) {
            // サイト管理者
            this.authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        } else {
            // 一般会員
            this.authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    //ログイン名を返す（社員番号をString型へ変換）
    @Override
    public String getUsername(){
        return account;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isEnabled(){
        if(!isStopped){
            return true;
        } else {
            return false;
        }
    }
}
