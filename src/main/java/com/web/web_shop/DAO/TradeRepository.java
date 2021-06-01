package com.web.web_shop.DAO;

import com.web.web_shop.beans.Trade;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TradeRepository
 */
public interface TradeRepository extends JpaRepository<Trade,Long> {
    Trade findByTidAndStatus(Long tid, Integer status);

}
