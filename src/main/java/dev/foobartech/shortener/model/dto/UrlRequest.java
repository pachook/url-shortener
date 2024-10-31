package dev.foobartech.shortener.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UrlRequest {
    @NotBlank(message = "Original URL is required.")
    private String originalUrl;

    // Optional custom ID for the shortened URL
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Custom ID must be alphanumeric.")
    private String customId;

    // Optional time-to-live in seconds; must be positive or zero if provided
    @PositiveOrZero(message = "TTL must be a positive number.")
    private Long ttl;
}
