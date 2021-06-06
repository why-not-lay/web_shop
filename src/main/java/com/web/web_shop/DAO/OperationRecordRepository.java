package com.web.web_shop.DAO;

import java.util.List;

import com.web.web_shop.beans.OperationRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * OperationTypeRepository
 */
@Repository
public interface OperationRecordRepository extends JpaRepository<OperationRecord,Long> {
    //List<OperationRecord> findall();
}
