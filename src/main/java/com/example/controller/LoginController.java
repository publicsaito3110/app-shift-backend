package com.example.controller;

import com.example.domain.model.bean.LoginAuthBean;
import com.example.domain.model.bean.LoginBean;
import com.example.domain.service.LoginService;
import com.example.form.LoginAuthForm;
import com.example.security.SecurityConst;
import jakarta.servlet.http.HttpServletRequest;
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
     * [Controller] ログイン確認機能 (/login)
     *
     * <p>
     * ログイン済みか確認を行う
     *
     * @param request HttpServletRequest
     * @return LoginBean ログイン済みかの結果
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public LoginBean login(HttpServletRequest request) {

        // ヘッダーからJWTを取得
        String jwt = request.getHeader(SecurityConst.JWT_TOKEN_HEADER_REQUEST) + " ";

        if (jwt == null) {
            // JWTがnullのとき、未ログインであることを返す
            return new LoginBean(false);
        } else if (SecurityConst.JWT_AUTHORIZE_CLAIM.equals(jwt)) {
            // JWTがClaim認証されていないとき、未ログインであることを返す
            return new LoginBean(false);
        } else {
            // それ以外のとき、ログイン済みであることを返す
            return new LoginBean(false);
        }
    }


    /**
     * [Controller] ログイン認可機能 (/login/auth)
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
    @RequestMapping(value = "/login/auth", method = RequestMethod.POST)
    public ResponseEntity<String> loginAuth(@RequestBody LoginAuthForm form) {
        LoginAuthBean bean = loginService.loginAuth(form);

            // JWTをHTTPヘッダーに挿入し、クライアントに返す
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(SecurityConst.JWT_TOKEN_HEADER_RESPONSE, bean.getJwt());
            return new ResponseEntity(httpHeaders, HttpStatus.OK);
    }
}
