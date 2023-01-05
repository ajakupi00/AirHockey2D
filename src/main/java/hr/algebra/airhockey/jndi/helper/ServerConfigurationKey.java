package hr.algebra.airhockey.jndi.helper;

public enum ServerConfigurationKey {

    RMI_SERVER_PORT("rmi.port"),
    RANDOM_PORT("random.port"),
    GAME_SERVER_IP("game.server.ip"),
    GAME_SERVER_PORT("game.server.port"),
    SERVER_SOCKET_PORT("thread.server.socket.port");

    private String key;

    private ServerConfigurationKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
