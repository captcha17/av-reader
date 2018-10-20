package com.rakushev.avreader.controller;

import com.rakushev.avreader.cache.Cache;
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
    
    @Autowired
    private Cache cache;
    
    @GetMapping()
    public void cleanCache() {
        cache.updateMostRecent("TestValue");
    }
}
