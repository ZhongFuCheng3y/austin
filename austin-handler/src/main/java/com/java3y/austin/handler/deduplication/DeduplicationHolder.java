package com.java3y.austin.handler.deduplication;

import com.java3y.austin.handler.deduplication.build.Builder;
import com.java3y.austin.handler.deduplication.service.DeduplicationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * @author huskey
 * @date 2022/1/18
 */
@Service
public class DeduplicationHolder {

    private Map<Integer, Builder> builderHolder = new HashMap<>(4);
    private Map<Integer, DeduplicationService> serviceHolder = new HashMap<>(4);

    public Builder selectBuilder(Integer key) {
        return builderHolder.get(key);
    }

    public DeduplicationService selectService(Integer key) {
        return serviceHolder.get(key);
    }

    public void putBuilder(Integer key, Builder builder) {
        builderHolder.put(key, builder);
    }

    public void putService(Integer key, DeduplicationService service) {
        serviceHolder.put(key, service);
    }
}
