package com.rakushev.avreader.configuration;

import com.rakushev.avreader.cache.Cache;
import com.rakushev.avreader.cache.InMemorySingleElementCache;
import com.rakushev.avreader.parse.AvParser;
import com.rakushev.avreader.parse.Parser;
import com.rakushev.avreader.service.Publisher;
import com.rakushev.avreader.service.SnsPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class GlobalConfiguration {

    @Bean
    public Cache cache() {
        return new InMemorySingleElementCache();
    }

    @Bean
    public Publisher publisher() {
        return new SnsPublisher();
    }

    @Bean
    public Parser parser() {
        Parser parser = new AvParser(cache(), publisher());
        return parser;
    }
}
