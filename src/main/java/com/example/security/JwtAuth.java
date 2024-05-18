package com.example.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.security.auth.message.AuthException;

import java.util.Date;

public class JwtAuth {


    /**
     * JWT生成
     *
     * <p>
     * ユーザから秘密鍵を用いて、JWTを生成する
     *
     * <li> JWTにはユーザ情報のみが含まれる
     * <li> JWTの有効期限が生成時点から1時間後に設定
     *
     * @param username　JWTに含めるユーザ情報
     * @return String　生成したJWT
     */
    public String jwtCreate(String username, String role) {

        // JWTトークンの生成
        return JWT.create()
                // ユーザ名
                .withClaim(SecurityConst.JWT_CLAIM_USERNAME, username)
                // 有効期限
                .withExpiresAt(new Date(new Date().getTime() + 1000 * 60 * 60))
                // ロール情報
                .withClaim(SecurityConst.JWT_CLAIM_ROLE, role)
                // 暗号化アルゴリズム
                .sign(Algorithm.HMAC256(SecurityConst.JWT_SECRET_KEY));
    }


    /**
     * リクエスト時のJWT認証
     *
     * <p>
     * JWTから認証を行う
     *
     * <li> 認証可能なとき、true
     * <li> 未認証および認証不要なURIのとき、false
     * <li> それ以外のとき、エラーとなる
     *
     * @param jwt 認証したいJWT
     * @param nowUri 認証時にリクエストされたURI
     * @return boolean 認証結果
     */
    public boolean jwtAuthorize(String jwt, String nowUri) {

        if (jwt == null || !jwt.startsWith(SecurityConst.JWT_AUTHORIZE_CLAIM)) {
            // Bearer未認証のJWTのとき
            return false;
        } else if (nowUri.matches(SecurityConst.PATTERN_JWT_IGNORE_URI_ARRAY)) {
            // 現在のURIがトークンのとき
            return false;
        }

        // トークンの検証
        String jwtToken = trimBearerHeaderJwt(jwt);
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SecurityConst.JWT_SECRET_KEY)).build().verify(jwtToken);
        Date nowDate = new Date();
        Date expiresDate = decodedJWT.getExpiresAt();

        // トークンの有効期限が過ぎているとき
        if (nowDate.after(expiresDate)) {
            throw new RuntimeException(new AuthException("認証エラー"));
        }

        // 認証成功時、true
        return true;
    }


    /**
     * JWTのユーザ取得
     *
     * <p>
     * JWTをデコードし、ユーザ情報を取得する
     *
     * @param jwt ユーザ情報が含まれたJWT
     * @return String ユーザ情報
     */
    public String jwtClaimUser(String jwt) {
        String JwtToken = trimBearerHeaderJwt(jwt);
        return JWT.require(Algorithm.HMAC256(SecurityConst.JWT_SECRET_KEY))
                .build().verify(JwtToken)
                .getClaim(SecurityConst.JWT_CLAIM_USERNAME).toString();
    }


    /**
     * JWTのユーザ取得
     *
     * <p>
     * JWTをデコードし、ユーザ情報を取得する
     *
     * @param jwt ユーザ情報が含まれたJWT
     * @return String ユーザ情報
     */
    public String jwtClaimRole(String jwt) {
        String JwtToken = trimBearerHeaderJwt(jwt);
        return JWT.require(Algorithm.HMAC256(SecurityConst.JWT_SECRET_KEY))
                .build().verify(JwtToken)
                .getClaim(SecurityConst.JWT_CLAIM_ROLE).toString();
    }


    /**
     * JWTのBearerヘッダー修正
     *
     * <p>
     * JWTのBearerヘッダーを取り除く
     *
     * @param jwt ヘッダーを取り除く対象のJWT
     * @return String ユーザ情報
     */
    private String trimBearerHeaderJwt(String jwt) {
        return jwt.substring(SecurityConst.JWT_AUTHORIZE_CLAIM.length());
    }
}