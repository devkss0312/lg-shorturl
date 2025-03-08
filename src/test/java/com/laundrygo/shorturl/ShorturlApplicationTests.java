package com.laundrygo.shorturl;

import com.laundrygo.shorturl.domain.ShortUrl;
import com.laundrygo.shorturl.repository.ShortUrlRepository;
import com.laundrygo.shorturl.service.ShortUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ShorturlApplicationTests {

    @Autowired
    private ShortUrlService shortUrlService;

    @Autowired
    private ShortUrlRepository repository;


    @Test
    void isValidUrl_shouldReturnTrueForValidUrl() {
        assertThat(shortUrlService.isValidUrl("http://example.com")).isTrue();
        assertThat(shortUrlService.isValidUrl("https://google.com")).isTrue();
    }

    @Test
    void isValidUrl_shouldReturnFalseForInvalidUrl() {
        assertThat(shortUrlService.isValidUrl("invalid-url")).isFalse();
        assertThat(shortUrlService.isValidUrl("htp:example.com")).isFalse();
    }

    @Test
    void shortenUrl_shouldThrowExceptionForInvalidUrl() {
        String invalidUrl = "not-a-valid-url";
        assertThrows(IllegalArgumentException.class, () -> shortUrlService.shortenUrl(invalidUrl));
    }

    @Test
    void shortenUrl_shouldCreateNewShortUrlForFirstTimeUri() {
        String oriUrl = "http://new-example1.com";

        ShortUrl result = shortUrlService.shortenUrl(oriUrl);

        // DB에서 직접 확인
        ShortUrl saved = repository.findByOriUrl(oriUrl).orElseThrow();
        assertThat(saved.getShortUrl()).isEqualTo(result.getShortUrl());
        assertThat(saved.getRequestCount()).isEqualTo(1);
    }


    @Test
    void shouldResolveShortUrlToOriginalUrl() {
        String oriUrl = "http://example2.com";
        ShortUrl shortened = shortUrlService.shortenUrl(oriUrl);
        ShortUrl result = shortUrlService.resolveUrl(shortened.getShortUrl());
        assertThat(result.getOriUrl()).isEqualTo(oriUrl);
    }

    @Test
    void resolveUrl_shouldIncrementRequestCount() {
        // Given
        String oriUrl = "http://example3.com";
        ShortUrl shortened = shortUrlService.shortenUrl(oriUrl);

        // When
        for (int i = 0; i < 5; i++) {
            shortUrlService.resolveUrl(shortened.getShortUrl());
        }

        // Then
        ShortUrl result = shortUrlService.resolveUrl(shortened.getShortUrl());
        assertThat(result.getRequestCount()).isEqualTo(7);
    }



    @Test
    void resolveUrl_shouldThrowExceptionForUnknownShortUrl() {
        String unknownShortUrl = "unknown123";

        assertThrows(IllegalArgumentException.class, () -> shortUrlService.resolveUrl(unknownShortUrl));
    }
}
