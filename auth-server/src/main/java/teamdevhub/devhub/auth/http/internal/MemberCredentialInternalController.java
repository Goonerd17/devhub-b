package teamdevhub.devhub.auth.http.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import teamdevhub.devhub.shared.security.MemberEmailQuery;
import teamdevhub.devhub.shared.security.AdminPasswordReset;

@RestController
@RequestMapping("/internal/member-credentials")
@RequiredArgsConstructor
public class MemberCredentialInternalController {
    private final MemberEmailQuery memberEmailQuery;
    private final AdminPasswordReset adminPasswordReset;

    @GetMapping("/{memberGuid}/email")
    public ResponseEntity<String> email(@PathVariable String memberGuid) {
        return ResponseEntity.of(memberEmailQuery.findEmail(memberGuid));
    }

    @PutMapping("/{memberGuid}/password")
    public ResponseEntity<Void> resetPassword(@PathVariable String memberGuid, @RequestBody PasswordResetRequest request) {
        adminPasswordReset.reset(memberGuid, request.password());
        return ResponseEntity.noContent().build();
    }

    public record PasswordResetRequest(String password) {
    }
}
