package com.web.web_shop.DAO;

import com.web.web_shop.beans.OnlineRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * OnlineRecordRepository
 */
@Repository
public interface OnlineRecordRepository extends JpaRepository<OnlineRecord,Long> {

}
