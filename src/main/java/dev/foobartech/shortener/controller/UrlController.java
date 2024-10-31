package dev.foobartech.shortener.controller;


import dev.foobartech.shortener.exception.IdAlreadyInUseException;
import dev.foobartech.shortener.model.dto.UrlRequest;
import dev.foobartech.shortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUrl(@PathVariable String id) {
        urlService.deleteShortUrl(id);
        return ResponseEntity.ok("URL deleted successfully");
    }



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
