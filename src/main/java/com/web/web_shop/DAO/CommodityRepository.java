package com.web.web_shop.DAO;

import java.util.List;

import com.web.web_shop.beans.Commodity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * CommodityRepository
 */
public interface CommodityRepository extends JpaRepository<Commodity,Long> {

    Commodity findByCidAndStatus(Long cid, Integer status);
    Commodity findByCidAndStatusAndComStatus(Long cid, Integer status, Integer com_status);
    List<Commodity> findByStatus(Integer status);

    Page<Commodity> findByUidAndStatusAndComStatus(Long uid, Integer com_status,Integer status, Pageable pageable);
    Page<Commodity> findByUidAndStatus(Long uid, Integer status, Pageable pageable);
    Page<Commodity> findByStatusAndComStatus(Integer status, Integer com_status, Pageable pageable);
    Page<Commodity> findByStatusAndComStatusAndType(Integer status, Integer com_status, Integer type, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update status =?2 from commodity where cid =?1",nativeQuery = true)
    int updateStatusByCid(Long cid, Integer status);


}
