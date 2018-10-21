package com.rakushev.avreader.parse;

import com.rakushev.avreader.cache.Cache;
import com.rakushev.avreader.service.Publisher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;

public class AvParser implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(AvParser.class);
    private static final String AV_URL = "https://cars.av.by/search?brand_id%5B0%5D=6&model_id%5B0%5D=2126&year_from=2009&year_to=&currency=USD&price_from=&price_to=14000&transmission=1&body_id=5&engine_type=1&engine_volume_min=&engine_volume_max=&driving_id=&mileage_min=&mileage_max=200000&region_id=5&city_id=&color_id%5B%5D=4&color_id%5B%5D=40&interior_material=&interior_color=&exchange=&search_time=";

    private Cache cache;
    private Publisher publisher;

    @Autowired
    public AvParser(Cache cache, Publisher publisher) {
        this.cache = cache;
        this.publisher = publisher;
    }

    @PostConstruct
    private void init() {
        try {
            Document doc = Jsoup.connect(AV_URL).get();
            logger.info(doc.title());
            String firstElementSrcUrl = doc.select("div .listing-item-image a img").first().attr("src");
            cache.updateMostRecent(firstElementSrcUrl);
        } catch (IOException e) {
            logger.error("Error setting initial cache value", e);
            throw new BeanInitializationException("Error setting initial cache value");
        }
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    @Override
    public void parse() {
        Document doc = null;
        try {
            doc = Jsoup.connect(AV_URL).get();
            logger.info("Checking ADs {}", LocalDateTime.now());
            String firstElementSrcUrl = doc.select("div .listing-item-image a img").first().attr("src");
            if (!cache.isMostRecent(firstElementSrcUrl)) {
                logger.info("New AD has been found {}", AV_URL);
                cache.updateMostRecent(firstElementSrcUrl);
                publisher.sendNotification(String.format("New AD was added during 2 minutes. See the top one here: %s", AV_URL));
            }
        } catch (IOException e) {
            logger.error("Error during parsing content from AV.by", e);
        }
    }
}
