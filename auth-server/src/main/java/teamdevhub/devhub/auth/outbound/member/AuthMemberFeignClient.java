package teamdevhub.devhub.auth.outbound.member;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;
import teamdevhub.devhub.auth.core.port.out.AuthMemberPort;
import teamdevhub.devhub.shared.member.AuthMemberRegistration;
import teamdevhub.devhub.shared.security.MemberRole;

@FeignClient(name = "member-server", configuration = InternalFeignConfiguration.class)
public interface AuthMemberFeignClient extends AuthMemberPort {
    @Override
    @PostMapping("/internal/auth/members")
    void register(@RequestBody AuthMemberRegistration registration);

    @Override
    @GetMapping("/internal/auth/members/admin/exists")
    boolean adminExists();

    @Override
    @PostMapping("/internal/auth/members/{memberGuid}/login/assert")
    void assertCanLogIn(@PathVariable String memberGuid);

    @Override
    @PostMapping("/internal/auth/members/{memberGuid}/login/success")
    void recordSuccessfulLogin(@PathVariable String memberGuid);

    @Override
    @GetMapping("/internal/auth/members/{memberGuid}/role")
    MemberRole findCurrentRole(@PathVariable String memberGuid);
}
