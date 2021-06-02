package com.web.web_shop.DAO;

import java.util.List;

import com.web.web_shop.beans.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserRepository
 */
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUid(Long uid);
    User findByUidAndStatus(Long uid, Integer status);
    User findByUsernameAndStatus(String username, Integer status);
    User findByUsernameAndTypeAndStatus(String username, Integer type, Integer status);
    List<User> findByParentAndStatus(Long parent, Integer status);

    @Transactional
    @Modifying
    @Query(value = "update user set status = ?2 where uid = ?1",nativeQuery = true)
    int updateStatusByUid(Long uid, Integer status);
}
