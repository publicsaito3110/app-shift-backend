package com.example.domain.service;

import com.example.common.Const;
import com.example.domain.model.bean.LoginAuthBean;
import com.example.domain.model.entity.UserEntity;
import com.example.domain.repository.UserRepository;
import com.example.form.LoginAuthForm;
import com.example.security.JwtAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final DaoAuthenticationProvider daoAuthenticationProvider;

    private final UserRepository userRepository;


    /**
     * [Service] ログイン認可機能 (/login/auth)
     *
     * <p>
     * ログイン情報からログイン認可を行う
     *
     * <li> ログインに成功したとき、JWTトークンを返却
     * <li> ログインに失敗したとき、HTTPステータス403を返却
     *
     * @param form フロントからの入力値
     * @return LoginBean ログイン結果
     */
    public LoginAuthBean loginAuth(LoginAuthForm form) {
        // DaoAuthenticationProviderを用いた認証を行う
        daoAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(form.getId(), form.getPassword())
        );

        // ユーザ情報を取得
        UserEntity userEntity = userRepository.selectId(form.getId(), Const.USER_DEL_FLG_DELETED);

        // JWTトークンの生成
        JwtAuth jwtAuths = new JwtAuth();
        String jwt = jwtAuths.jwtCreate(userEntity.getId(), userEntity.getAdminFlgFormatRole());

        return new LoginAuthBean(true, jwt);
    }
}
