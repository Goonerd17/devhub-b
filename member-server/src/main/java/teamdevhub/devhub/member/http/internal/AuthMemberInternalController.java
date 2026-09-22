package teamdevhub.devhub.member.http.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.member.api.*;
import teamdevhub.devhub.shared.member.AuthMemberRegistration;
import teamdevhub.devhub.shared.security.MemberRole;

@RestController
@RequestMapping("/internal/auth/members")
@RequiredArgsConstructor
public class AuthMemberInternalController {
    private final MemberRegistrationUseCase registrationUseCase;
    private final MemberLoginActivityUseCase loginActivityUseCase;
    private final CurrentMemberRoleQuery roleQuery;

    @PostMapping
    public void register(@RequestBody AuthMemberRegistration request) {
        registrationUseCase.register(new MemberRegistrationCommand(request.userGuid(), request.username(),
                request.introduction(), request.positionList(), request.skillList(),
                teamdevhub.devhub.member.api.MemberRole.valueOf(request.role().name())));
    }

    @GetMapping("/admin/exists")
    public boolean adminExists() { return registrationUseCase.adminExists(); }

    @PostMapping("/{memberGuid}/login/assert")
    public void assertCanLogIn(@PathVariable String memberGuid) { loginActivityUseCase.assertMemberCanLogIn(memberGuid); }

    @PostMapping("/{memberGuid}/login/success")
    public void recordLogin(@PathVariable String memberGuid) { loginActivityUseCase.recordSuccessfulLogin(memberGuid); }

    @GetMapping("/{memberGuid}/role")
    public MemberRole role(@PathVariable String memberGuid) {
        return MemberRole.valueOf(roleQuery.findCurrentRole(memberGuid).name());
    }
}
