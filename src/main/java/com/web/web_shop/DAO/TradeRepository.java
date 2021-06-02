package com.web.web_shop.DAO;

import java.util.List;

import com.web.web_shop.beans.Trade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * TradeRepository
 */
public interface TradeRepository extends JpaRepository<Trade,Long> {
    Trade findByTidAndStatus(Long tid, Integer status);
    List<Trade> findBySellerUidAndFinished(Long sellerUid, Integer finished);

    Page<Trade> findBySellerUidAndStatusAndFinished(Long sellerUid, Integer status, Integer finished,Pageable pageable);
}
