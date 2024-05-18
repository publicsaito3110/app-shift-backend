package com.example.security;

import com.example.common.Const;
import com.example.domain.model.entity.UserEntity;
import com.example.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    /**
     * ログイン認可の検証
     *
     * <p>
     * ログイン時のユーザIDからログイン認可を行う
     * ユーザIDおよびパスワードが一致しているか判定
     *
     * <li> 認可されたとき、結果とユーザ情報を返す
     * <li> それ以外のとき、エラーとなる
     *
     * @param username ログイン入力値のユーザID
     * @return UserDetails　ログイン認可の結果とユーザ情報
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // ログイン入力値から該当のユーザを検索
        UserEntity entity = userRepository.selectId(username, Const.USER_DEL_FLG_DELETED);

        // ユーザが存在しないとき、例外で抜ける
        if (entity == null) {
            throw new UsernameNotFoundException("username or password are not found");
        }

        // ユーザの権限の追加
        HashSet<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(entity.getAdminFlgFormatRole()));

        // ログインパスワードが一致しているか認証
        return new User(username, "", grantedAuthorities);
    }
}
