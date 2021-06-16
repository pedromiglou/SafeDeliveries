package tqsua.DeliveriesServer.security;

public class SecurityConstants {

    private SecurityConstants() {
        throw new IllegalStateException("Security class");
    }

    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 1_000_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String PUBLIC_URL = "/api";
    public static final String PRIVATE_URL = "/api/private";
}
