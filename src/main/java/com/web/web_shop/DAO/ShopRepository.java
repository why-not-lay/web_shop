package com.web.web_shop.DAO;

import java.util.List;

import com.web.web_shop.beans.Shop;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ShopRepository
 */
public interface ShopRepository extends JpaRepository<Shop,Long> {
    Shop findBySid(Long sid);
}
