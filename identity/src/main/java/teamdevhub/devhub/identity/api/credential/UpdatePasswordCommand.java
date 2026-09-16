package teamdevhub.devhub.identity.api.credential;

import lombok.Builder;

@Builder
public record UpdatePasswordCommand(String userGuid,
                                    String currentPassword,
                                    String newPassword) {
}
