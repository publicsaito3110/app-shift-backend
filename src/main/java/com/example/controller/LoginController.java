package com.example.controller;

import com.example.domain.model.bean.LoginBean;
import com.example.domain.service.LoginService;
import com.example.form.LoginForm;
import com.example.security.SecurityConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    /**
     * [Controller] ログイン認可機能 (/login)
     *
     * <p>
     * ログイン情報からログイン認可を行う
     *
     * <li> ログインに成功したとき、JWTトークンを返却
     * <li> ログインに失敗したとき、HTTPステータス403を返却
     *
     * @param form フロントからの入力値
     * @return ResponseEntity<String> ログイン結果が格納されたHTTPヘッダー
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginForm form) {
        LoginBean bean = loginService.login(form);

            // JWTをHTTPヘッダーに挿入し、クライアントに返す
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(SecurityConst.JWT_TOKEN_HEADER_RESPONSE, bean.getJwt());
            return new ResponseEntity(httpHeaders, HttpStatus.OK);
    }
}
