package com.laundrygo.shorturl.repository;

import com.laundrygo.shorturl.domain.ShortUrl;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Optional;

@Mapper
public interface ShortUrlRepository {
    void save(ShortUrl shortUrl);
    Optional<ShortUrl> findByOriUrl(String oriUrl);
    Optional<ShortUrl> findByShortUrl(String shortUrl);

    void incrementRequestCount(String shortUrl);

}