package com.java3y.austin.dao;

import com.java3y.austin.domain.SmsRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * 短信记录的Dao
 * @author 3y
 *
 */
public interface SmsRecordDao extends CrudRepository<SmsRecord, Long> {


}
