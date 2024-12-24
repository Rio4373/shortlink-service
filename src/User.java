import java.io.Serializable;

/**
 * Класс, представляющий пользователя.
 */
class User implements Serializable {
    private String uuid;
    private String name;

    public User(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}