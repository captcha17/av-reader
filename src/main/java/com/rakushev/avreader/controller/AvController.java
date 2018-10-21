package com.rakushev.avreader.controller;

import com.rakushev.avreader.cache.Cache;
import com.rakushev.avreader.parse.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dzmitry_Rakushau on 10/21/2018.
 */
@RestController
@RequestMapping("/av-reader")
public class AvController {
    private static final Logger logger = LoggerFactory.getLogger(AvController.class);

    @Autowired
    private Cache<String> cache;

    @Autowired
    private Parser parser;

    @GetMapping()
    public void cleanCache() {
        String last = parser.getLast();
        if (last != null) {
            cache.remove(last);
            logger.info("Update cache value has been performed");
        }
    }
}
