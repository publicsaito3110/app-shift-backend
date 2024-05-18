package com.example.security;

import jakarta.xml.bind.DatatypeConverter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;

public class MyPasswordEncoder implements PasswordEncoder {

    private String PASSWORD_HASH_ALGORITHM = "SHA-256";
    private final String PASSWORD_SALT = "J6fWfj35gr4jof9xz";
    private final int PASSWORD_STRETCHING = 17;


    /**
     * パスワードエンコーディング
     *
     * <p>
     * パスワードをハッシュ化させる
     *
     * @param rawPassword 生パスワード
     * @return String ハッシュ化済みのパスワード
     */
    @Override
    public String encode(CharSequence rawPassword) {

        try {
            // 生パスワードにソルトを設定
            String saltPass = PASSWORD_SALT + rawPassword;

            // ハッシュコード⽣成
            MessageDigest messageDigest = MessageDigest.getInstance(PASSWORD_HASH_ALGORITHM);
            byte[] digestByteArr = messageDigest.digest(saltPass.getBytes());

            // ハッシュ値を規定までストレッチング
            for (int i = 0; i < PASSWORD_STRETCHING; i++) {
                digestByteArr = messageDigest.digest(digestByteArr);
            }

            // ハッシュ化パスワードを文字列に変換
            return DatatypeConverter.printHexBinary(digestByteArr).toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * ハッシュ化パスワード一致判定
     *
     * <p>
     * 生パスワードとハッシュ化済みのパスワードが一致しているか判定する
     *
     * <li> true: ハッシュ化済みのパスワードと一致
     * <li> false: ハッシュ化済みのパスワードと不一致
     *
     * @param rawPassword 生パスワード
     * @return boolean ハッシュ化済みのパスワード
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }
}