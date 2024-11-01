package dev.foobartech.shortener.service;

import dev.foobartech.shortener.ShortenerApplication;
import dev.foobartech.shortener.model.dto.UrlRequest;
import dev.foobartech.shortener.model.entity.ShortUrl;
import dev.foobartech.shortener.model.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShortenerApplication.class})
@ActiveProfiles("test")
@Sql("/data-test.sql")
class UrlServiceTest {

    @Autowired
    private ShortUrlRepository repository;
    @Autowired
    private UrlService urlService;

    @Test
    void shouldReturnValidOriginalUrl() {

        String result = urlService.getOriginalUrl("test1");
        assertEquals("https://www.example.com/test1", result);
    }

    @Test
    void shouldReturnNullForInvalidOriginalUrl() {

        String result = urlService.getOriginalUrl("test3");
        assertNull(result);
    }

    @Test
    void shouldCreateShortUrlWithCustomId() {
        String shortId = "sample1";
        UrlRequest sampleRequest = new UrlRequest();
        sampleRequest.setOriginalUrl("https://www.sample.dev/test");
        sampleRequest.setCustomId(shortId);

        Optional<ShortUrl> result = repository.findById(shortId);
        assertFalse(result.isPresent());

        String createdShortId = urlService.createShortUrl(sampleRequest);
        assertEquals(createdShortId, shortId);

        Optional<ShortUrl> savedResult = repository.findById(shortId);
        assertTrue(savedResult.isPresent());
    }

    @Test
    void shouldCreateShortUrlWithGeneratedId() {
        UrlRequest sampleRequest = new UrlRequest();
        sampleRequest.setOriginalUrl("https://www.sample.dev/test");

        int prevSize = repository.findAll().size();

        String createdShortId = urlService.createShortUrl(sampleRequest);
        int currSize = repository.findAll().size();
        assertEquals(prevSize + 1, currSize);

        Optional<ShortUrl> savedResult = repository.findById(createdShortId);
        assertTrue(savedResult.isPresent());
    }

    @Test
    void deleteShortUrl() {
        String shortId = "test1";

        Optional<ShortUrl> existingResult = repository.findById(shortId);
        assertTrue(existingResult.isPresent());

        urlService.deleteShortUrl(shortId);

        Optional<ShortUrl> deletedResult = repository.findById(shortId);
        assertFalse(deletedResult.isPresent());

    }
}