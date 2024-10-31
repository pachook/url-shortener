package dev.foobartech.shortener.service;

import dev.foobartech.shortener.exception.IdAlreadyInUseException;
import dev.foobartech.shortener.model.dto.UrlRequest;
import dev.foobartech.shortener.model.entity.ShortUrl;
import dev.foobartech.shortener.model.repository.ShortUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class UrlService {
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();


    private final ShortUrlRepository shortUrlRepository;

    @Autowired
    public UrlService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public String getOriginalUrl(String id) {
        Optional<ShortUrl> shortUrlOpt = shortUrlRepository.findById(id);
        if (shortUrlOpt.isPresent()) {
            ShortUrl shortUrl = shortUrlOpt.get();
            if (shortUrl.getTtl() == null
                || shortUrl.getCreatedAt().plusSeconds(shortUrl.getTtl()).isAfter(LocalDateTime.now())) {
                return shortUrl.getOriginalUrl();
            }
        }
        return null;
    }

    public String createShortUrl(UrlRequest request) {
        String id = Optional.ofNullable(request.getCustomId()).orElse(generateUniqueId());

        if (shortUrlRepository.existsById(id)) {
            throw new IdAlreadyInUseException("Id already in use");
        }

        ShortUrl shortUrl = ShortUrl.builder()
                .id(id)
                .originalUrl(request.getOriginalUrl())
                .ttl(request.getTtl())
                .createdAt(LocalDateTime.now())
                .build();
        log.info("Created short url: {}", shortUrl);
        shortUrlRepository.save(shortUrl);

        return id;
    }

    public void deleteShortUrl(String id) {
        shortUrlRepository.deleteById(id);
        logDeletionInfo(id);
    }

//    @Scheduled(fixedRate=60*60*1000) //every hour
    @Scheduled(fixedRate=1000) //every hour
    public void removeExpiredEntries() {
        LocalDateTime now = LocalDateTime.now();
        shortUrlRepository.findAllByTtlNotNull().forEach(shortUrl -> {
            if (shortUrl.getCreatedAt().plusSeconds(shortUrl.getTtl()).isBefore(now)) {
                shortUrlRepository.delete(shortUrl);
                logDeletionInfo(shortUrl.getId());
            }
        });

    }

    private static void logDeletionInfo(String shortUrl) {
        log.info("Deleted short url: {}", shortUrl);
    }

    private String generateUniqueId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }
        return sb.toString();
    }
}
