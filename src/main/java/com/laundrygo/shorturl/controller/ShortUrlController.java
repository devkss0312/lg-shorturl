package com.laundrygo.shorturl.controller;

import com.laundrygo.shorturl.domain.ShortUrl;
import com.laundrygo.shorturl.service.ShortUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Short URL API", description = "API for creating and resolving short URLs")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlService service;

    @Operation(summary = "Create a short URL", description = "Accepts an original URL and returns a shortened version.")
    @PatchMapping("/shorten")
    public ResponseEntity<?> shorten(@RequestParam String oriUrl) {
        try {
            ShortUrl shortUrl = service.shortenUrl(oriUrl);
            return ResponseEntity.status(shortUrl.getRequestCount() == 1 ? HttpStatus.CREATED : HttpStatus.OK).body(shortUrl);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(summary = "Resolve a short URL", description = "Takes a short URL and returns the original URL.")
    @PatchMapping("/resolve")
    public ResponseEntity<ShortUrl> resolve(@RequestParam String shortUrl) {
        ShortUrl resolvedUrl = service.resolveUrl(shortUrl);
        return ResponseEntity.ok(resolvedUrl);
    }
}
