package teamdevhub.devhub.member.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.port.out.UserRepository;
import teamdevhub.devhub.member.api.CurrentMemberRoleQuery;
import teamdevhub.devhub.member.api.MemberRole;

@Service
@RequiredArgsConstructor
public class CurrentMemberRoleService implements CurrentMemberRoleQuery {
    private final UserRepository userRepository;

    @Override
    public MemberRole findCurrentRole(String memberGuid) {
        UserRole role = userRepository.findByUserGuid(memberGuid).getUserRole();
        return MemberRole.valueOf(role.name());
    }
}
