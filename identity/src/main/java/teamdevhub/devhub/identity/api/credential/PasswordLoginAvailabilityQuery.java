package teamdevhub.devhub.identity.api.credential;

public interface PasswordLoginAvailabilityQuery {

    boolean isPasswordLoginAvailable(String memberGuid);
}
