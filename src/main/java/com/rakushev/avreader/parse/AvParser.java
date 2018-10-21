package com.rakushev.avreader.parse;

import com.rakushev.avreader.cache.Cache;
import com.rakushev.avreader.service.Publisher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AvParser implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(AvParser.class);
    private static final String AV_URL = "https://cars.av.by/search?brand_id%5B0%5D=6&model_id%5B0%5D=2126&year_from=2009&year_to=&currency=USD&price_from=&price_to=14000&transmission=1&body_id=5&engine_type=1&engine_volume_min=&engine_volume_max=&driving_id=&mileage_min=&mileage_max=200000&region_id=5&city_id=&color_id%5B%5D=4&color_id%5B%5D=40&interior_material=&interior_color=&exchange=&search_time=";

    private Cache<String> cache;
    private Publisher publisher;

    @Autowired
    public AvParser(Cache<String> cache, Publisher publisher) {
        this.cache = cache;
        this.publisher = publisher;
    }

    @PostConstruct
    private void init() {
        try {
            Document doc = Jsoup.connect(AV_URL).get();
            logger.info(doc.title());
            Set<String> ads = doc.select("div .listing-item-image a img")
                    .stream()
                    .map(e -> e.attr("src"))
                    .collect(Collectors.toSet());
            cache.addAll(ads);
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
            Elements elements = doc.select("div .listing-item-image a img");
            String firstElementSrcUrl = elements.get(0).attr("src");
            String secondElementSrcUrl = elements.get(1).attr("src");
            // take and check two last elements in case if two updates happened
            if (!cache.contains(firstElementSrcUrl) || !cache.contains(secondElementSrcUrl)) {
                logger.info("New AD has been found {}", AV_URL);
                cache.add(firstElementSrcUrl);
                cache.add(secondElementSrcUrl);
                publisher.sendNotification(String.format("New AD was added during last 2 minutes. See the top one here: %s", AV_URL));
            }
        } catch (IOException e) {
            logger.error("Error during parsing content from AV.by", e);
        }
    }

    @Override
    public String getLast() {
        Document doc = null;
        try {
            doc = Jsoup.connect(AV_URL).get();
            Elements elements = doc.select("div .listing-item-image a img");
            return elements.first().attr("src");
        } catch (IOException e) {
            logger.error("Error during parsing content from AV.by", e);
        }
        return null;
    }
}
