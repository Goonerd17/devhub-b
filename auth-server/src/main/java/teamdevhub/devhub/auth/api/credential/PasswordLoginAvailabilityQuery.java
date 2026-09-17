package teamdevhub.devhub.auth.api.credential;

public interface PasswordLoginAvailabilityQuery {

    boolean isPasswordLoginAvailable(String memberGuid);
}
