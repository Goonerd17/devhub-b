package teamdevhub.devhub.auth.core.auth.port.out.password;

public interface EncodedPasswordProvider {

    String encode(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}