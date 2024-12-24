import java.util.*;
import java.util.concurrent.*;
import java.time.*;

/**
 * Класс для управления короткими ссылками.
 */
class LinkManager {

    public static void createShortLink(Scanner scanner, User user, Map<String, Link> links, ScheduledExecutorService scheduler, Properties config) {
        try {
            System.out.println("Введите оригинальный URL:");
            String originalUrl = scanner.nextLine();

            System.out.println("Введите время жизни ссылки (в секундах):");
            int userLifetime = Integer.parseInt(scanner.nextLine());
            if (userLifetime <= 0) throw new IllegalArgumentException("Время жизни должно быть положительным!");

            int defaultLifetime = Integer.parseInt(config.getProperty("defaultLifetimeSeconds"));
            int lifetime = Math.min(userLifetime, defaultLifetime);

            System.out.println("Введите лимит переходов:");
            int userClickLimit = Integer.parseInt(scanner.nextLine());
            if (userClickLimit <= 0) throw new IllegalArgumentException("Лимит переходов должен быть положительным!");

            int defaultClickLimit = Integer.parseInt(config.getProperty("defaultClickLimit"));
            int clickLimit = Math.min(userClickLimit, defaultClickLimit);

            String shortUrl = generateShortUrl(links);
            Instant expiry = Instant.now().plusSeconds(lifetime);

            Link link = new Link(originalUrl, shortUrl, user.getUuid(), expiry, clickLimit);
            links.put(shortUrl, link);

            System.out.println("Короткая ссылка создана: " + shortUrl);

            scheduler.schedule(() -> {
                links.remove(shortUrl);
                System.out.println("Срок действия ссылки " + shortUrl + " истёк, она была удалена.");
            }, lifetime, TimeUnit.SECONDS);

        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода: Ожидалось число. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        }

    }


    public static void viewLinks(Scanner scanner, User user, Map<String, Link> links) {
        System.out.println("Ваши ссылки:");

        for (Link link : links.values()) {
            if (link.getOwnerUuid().equals(user.getUuid())) {
                System.out.println(link.getShortUrl() + " -> " + link.getOriginalUrl() + " (Клики: " + link.getClicks() + "/" + link.getClickLimit() + ")");
            }
        }
    }

    public static void updateClickLimit(Scanner scanner, User user, Map<String, Link> links) {
        System.out.println("Введите короткую ссылку для изменения лимита переходов:");
        String shortUrl = scanner.nextLine();

        Link link = links.get(shortUrl);
        if (link == null || !link.getOwnerUuid().equals(user.getUuid())) {
            System.out.println("Ссылка не найдена или у вас нет прав на её изменение.");
            return;
        }

        System.out.println("Введите новый лимит переходов:");
        int newLimit = Integer.parseInt(scanner.nextLine());
        link.setClickLimit(newLimit);

        System.out.println("Лимит переходов обновлён.");
    }

    public static void deleteLink(Scanner scanner, User user, Map<String, Link> links) {
        System.out.println("Введите короткую ссылку для удаления:");
        String shortUrl = scanner.nextLine();

        Link link = links.get(shortUrl);
        if (link == null || !link.getOwnerUuid().equals(user.getUuid())) {
            System.out.println("Ссылка не найдена или у вас нет прав на её удаление.");
            return;
        }

        links.remove(shortUrl);
        System.out.println("Ссылка удалена.");
    }

    private static String generateShortUrl(Map<String, Link> links) {
        String shortUrl;
        do {
            shortUrl = "krat.ko/" + UUID.randomUUID().toString().substring(0, 8);
        } while (links.containsKey(shortUrl));
        return shortUrl;
    }

}
