package com.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class AuthorizeFilter extends OncePerRequestFilter {


    /**
     * [Filter] JWT認証フィルター
     *
     * <p>
     * リクエスト時のJWT認証
     * リクエストヘッダーからJWTを取得し、認証を行う
     *
     * <li> ログイン済みかつ認証が成功したとき、新しいJWTをレスポンスヘッダーに挿入し処理を開始
     * <li> 未ログインのとき、403HTTPステータスエラーを返却
     * <li> エラー発生したとき、500HTTPステータスエラーを返却
     *
     *
     * @param request Http Request
     * @param response Http Response
     * @param filterChain filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // ヘッダーのキーからトークンを取得
        String jwt = request.getHeader(SecurityConst.JWT_TOKEN_HEADER_REQUEST);

        // 現在のURIを取得
        String nowUri = request.getRequestURI();

        JwtAuth jwtAuths = new JwtAuth();
        boolean isAuthorization = jwtAuths.jwtAuthorize(jwt, nowUri);

        if (!isAuthorization) {
            // 未認証のトークンのとき、何もせずクライアントへリターン
            filterChain.doFilter(request, response);
            return;
        }

        // 新しいJWTを生成
        String username = jwtAuths.jwtClaimUser(jwt);
        String role = jwtAuths.jwtClaimRole(jwt);
        String newJwt = jwtAuths.jwtCreate(username, role);

        // 再生成したJWTをレスポンスヘッダーへ更新
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(SecurityConst.JWT_TOKEN_HEADER_RESPONSE, newJwt);

        // ログイン状態の設定
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>()));
        filterChain.doFilter(request, response);
    }
}
