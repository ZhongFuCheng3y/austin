package com.java3y.austin.service.deduplication.build;

import com.java3y.austin.service.deduplication.DeduplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


/**
 * @author huskey
 * @date 2022/1/18
 */

@Service
public class BuilderFactory {


    private Map<String, Builder> builderFactory = new HashMap<>(4);

    @Autowired
    private Builder contentDeduplicationBuilder;

    @Autowired
    private Builder frequencyDeduplicationBuilder;

    @PostConstruct
    public void init() {
        builderFactory.put(DeduplicationConstants.CONTENT_DEDUPLICATION, contentDeduplicationBuilder);
        builderFactory.put(DeduplicationConstants.FREQUENCY_DEDUPLICATION, frequencyDeduplicationBuilder);
    }


    public Builder select(String key) {
        return builderFactory.get(key);
    }

}
