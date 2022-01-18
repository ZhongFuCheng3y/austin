package com.java3y.austin.service.deduplication.build;

import com.java3y.austin.domain.DeduplicationParam;

/**
 * @author luohaojie
 * @date 2022/1/18
 */
public interface Builder {

    DeduplicationParam build(String deduplication, String key);
}
