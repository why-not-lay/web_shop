package com.web.web_shop.DAO;

import com.web.web_shop.beans.OperationRecord;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * OperationTypeRepository
 */
public interface OperationRecordRepository extends JpaRepository<OperationRecord,Long> {


}
