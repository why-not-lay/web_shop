package com.web.web_shop.DAO;

import java.util.List;

import com.web.web_shop.beans.Shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ShopRepository
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {
    Shop findBySid(Long sid);
}
