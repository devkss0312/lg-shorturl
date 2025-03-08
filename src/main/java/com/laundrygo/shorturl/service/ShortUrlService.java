package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.domain.ShortUrl;
import com.laundrygo.shorturl.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final ShortUrlRepository repository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int URL_LENGTH = 8;

    public boolean isValidUrl(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private String generateShortUrl() {
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder();
        while (true) {
            shortUrl.setLength(0);
            for (int i = 0; i < URL_LENGTH; i++) {
                shortUrl.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            if (!repository.findByShortUrl(shortUrl.toString()).isPresent()) {
                break;
            }
        }
        return shortUrl.toString();
    }

    @Transactional
    public ShortUrl shortenUrl(String oriUrl) {
        if (!isValidUrl(oriUrl)) {
            throw new IllegalArgumentException("Invalid original URL");
        }

        Optional<ShortUrl> existing = repository.findByOriUrl(oriUrl);
        if (existing.isPresent()) {
            ShortUrl existingUrl = existing.get();

            repository.incrementRequestCount(existingUrl.getShortUrl());
            return repository.findByOriUrl(oriUrl).get();
        }

        ShortUrl newUrl = new ShortUrl();
        newUrl.setOriUrl(oriUrl);
        newUrl.setShortUrl(generateShortUrl());
        repository.save(newUrl);
        return newUrl;
    }

    @Transactional
    public ShortUrl resolveUrl(String shortUrl) {
        ShortUrl shortUrlEntity = repository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new IllegalArgumentException("URL not found"));
        repository.incrementRequestCount(shortUrl);
        return repository.findByShortUrl(shortUrl).orElseThrow();
    }
}
