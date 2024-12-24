import java.io.Serializable;
import java.time.Instant;

/**
 * Класс, представляющий короткую ссылку.
 */
class Link implements Serializable {
    private String originalUrl;
    private String shortUrl;
    private String ownerUuid;
    private Instant expiry;
    private int clickLimit;
    private int clicks;

    public Link(String originalUrl, String shortUrl, String ownerUuid, Instant expiry, int clickLimit) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.ownerUuid = ownerUuid;
        this.expiry = expiry;
        this.clickLimit = clickLimit;
        this.clicks = 0;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public Instant getExpiry() {
        return expiry;
    }

    public int getClickLimit() {
        return clickLimit;
    }

    public void setClickLimit(int clickLimit) {
        this.clickLimit = clickLimit;
    }

    public int getClicks() {
        return clicks;
    }

    public void incrementClicks() {
        this.clicks++;
    }
}
