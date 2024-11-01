package dev.foobartech.shortener.controller;


import dev.foobartech.shortener.exception.IdAlreadyInUseException;
import dev.foobartech.shortener.model.dto.UrlRequest;
import dev.foobartech.shortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(UrlController.URL_PREFIX)
public class UrlController {
    public static final String URL_PREFIX = "/api/url/";


    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Operation(summary = "Gets original url for provided short url",
            description = "Returns 404 in case of missing short url")
    @GetMapping("{id}")
    public ResponseEntity<Void> redirectUrl(@PathVariable String id) {
        String originalUrl = urlService.getOriginalUrl(id);
        if (originalUrl != null) {

            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(originalUrl))
                    .build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Deletes short url's entry")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUrl(@PathVariable String id) {
        urlService.deleteShortUrl(id);
        return ResponseEntity.ok("URL deleted successfully");
    }


    @Operation(summary = "Creates short url's entry",
            description = "TTL and CustomId fields are optional. Returns shortId")
    @PostMapping("shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody UrlRequest request) {
        try {
            String shortUrl = urlService.createShortUrl(request);
            return ResponseEntity.ok(shortUrl);
        } catch (IdAlreadyInUseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("ID already in use");
        }

    }
}
