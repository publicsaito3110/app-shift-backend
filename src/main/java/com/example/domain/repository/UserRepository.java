package com.example.domain.repository;

import com.example.domain.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query(value = "SELECT * FROM user WHERE id = :id AND del_flg != :delFlg", nativeQuery = true)
    public UserEntity selectId(String id, String delFlg);
}
