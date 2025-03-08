package com.laundrygo.shorturl.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@ToString
public class ShortUrl {
    private Long id;
    private String oriUrl;
    private String shortUrl;
    private int requestCount = 1;
}