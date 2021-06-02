package com.web.web_shop.DAO;

import com.web.web_shop.beans.ViewRecord;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ViewRecordRepository
 */
public interface ViewRecordRepository extends JpaRepository<ViewRecord,Long> {
    Page<ViewRecord> findByUidSellerAndStatus(Long uidSeller, Integer status, Pageable pageable);
}
