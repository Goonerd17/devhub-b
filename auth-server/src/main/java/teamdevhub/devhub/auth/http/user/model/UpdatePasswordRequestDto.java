package teamdevhub.devhub.auth.http.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.auth.api.credential.UpdatePasswordCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequestDto {

    private String currentPassword;
    private String newPassword;

    public UpdatePasswordCommand toUpdatePasswordCommand(String userGuid) {
        return new UpdatePasswordCommand(
                userGuid,
                currentPassword,
                newPassword
        );
    }
}

