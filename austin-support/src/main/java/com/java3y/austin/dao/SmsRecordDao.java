package com.java3y.austin.dao;

import com.java3y.austin.domain.SmsRecord;
import org.springframework.data.repository.CrudRepository;

public interface SmsRecordDao extends CrudRepository<SmsRecord, Long> {


}
