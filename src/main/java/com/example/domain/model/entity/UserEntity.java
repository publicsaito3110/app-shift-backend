package com.example.domain.model.entity;

import com.example.common.Const;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "nameKana")
    private String nameKana;

    @Column(name = "gender")
    private String gender;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "tel")
    private String tel;

    @Column(name = "email")
    private String email;

    @Column(name = "note")
    private String note;

    @Column(name = "icon_kbn")
    private String iconKbn;

    @Column(name = "admin_flg")
    private String adminFlg;

    @Column(name = "del_flg")
    private String delFlg;


    /**
     * ロール取得処理
     *
     * <p>
     * ロールからロール情報に変換した値を取得する
     *
     * <li> 管理者: ROLE_ADMIN
     * <li> 一般ユーザ: ROLE_GENERAL
     *
     * @return String ロール情報
     */
    public String getAdminFlgFormatRole() {

        if (Const.USER_ADMIN_FLG_ADMIN.equals(adminFlg)) {
            return Const.USER_ADMIN_FLG_ROLE_ADMIN;
        } else {
            return Const.USER_ADMIN_FLG_ROLE_GENERAL;
        }
    }
}
