package dev.foobartech.shortener.model.repository;

import dev.foobartech.shortener.model.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, String> {
    Optional<ShortUrl> findById(String id);
    List<ShortUrl> findAllByTtlNotNull();
}
