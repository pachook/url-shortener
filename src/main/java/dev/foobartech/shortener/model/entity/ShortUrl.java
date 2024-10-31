package dev.foobartech.shortener.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrl {
    @Id
    private String id;
    private String originalUrl;
    private LocalDateTime createdAt;
    private Long ttl;
}
