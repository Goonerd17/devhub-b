package teamdevhub.devhub.auth.api.credential;

import lombok.Builder;

@Builder
public record UpdatePasswordCommand(String userGuid,
                                    String currentPassword,
                                    String newPassword) {
}
