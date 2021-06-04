package com.web.web_shop.DAO;

import java.util.List;

import com.web.web_shop.beans.Commodity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * CommodityRepository
 */
@Repository
public interface CommodityRepository extends JpaRepository<Commodity,Long> {

    Commodity findByCidAndStatus(Long cid, Integer status);
    Commodity findByCidAndStatusAndComStatus(Long cid, Integer status, Integer com_status);
    Commodity findByCid(Long cid);
    List<Commodity> findByStatus(Integer status);

    Page<Commodity> findByUidAndStatusAndComStatus(Long uid, Integer com_status,Integer status, Pageable pageable);
    Page<Commodity> findByUidAndStatus(Long uid, Integer status, Pageable pageable);
    Page<Commodity> findByStatusAndComStatus(Integer status, Integer com_status, Pageable pageable);
    Page<Commodity> findByStatusAndComStatusAndType(Integer status, Integer com_status, Integer type, Pageable pageable);

    @Query(value = "select * from commodity where status = 0 and com_status = 1 order by(case when type = ?1 then 1 else 0 end) desc",nativeQuery = true)
    Page<Commodity> findByfavouriteType(Integer type,Pageable pageable);


    @Transactional
    @Modifying
    @Query(value = "update commodity set status =?2 where cid=?1",nativeQuery = true)
    int updateStatusByCid(Long cid, Integer status);

    @Query(value = "select * from commodity where name LIKE CONCAT('%',?1,'%') and status = 0 and com_status = 1", nativeQuery = true)
    List<Commodity> searchByKey(String key);

    @Query(value = "select * from commodity where name LIKE CONCAT('%',?1,'%') and type = ?2 and status = 0 and com_status = 1", nativeQuery = true)
    List<Commodity> searchByKeyAndType(String key, Integer type);

}
