package teamdevhub.devhub.member.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.port.out.UserPositionRepository;
import teamdevhub.devhub.member.core.user.port.out.UserRepository;
import teamdevhub.devhub.member.core.user.port.out.UserSkillRepository;
import teamdevhub.devhub.member.api.MemberRegistrationCommand;
import teamdevhub.devhub.member.api.MemberRegistrationUseCase;
import teamdevhub.devhub.member.api.MemberRole;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberRegistrationService implements MemberRegistrationUseCase {
    private final UserRepository userRepository;
    private final UserPositionRepository userPositionRepository;
    private final UserSkillRepository userSkillRepository;

    @Override
    public void register(MemberRegistrationCommand command) {
        CreateUserCommand create = new CreateUserCommand(command.userGuid(), command.username(),
                command.introduction(), command.positionList(), command.skillList());
        User user = command.role() == MemberRole.ADMIN ? User.createAdminUser(create) : User.createGeneralUser(create);
        userPositionRepository.saveAll(command.positionList().stream()
                .map(position -> new UserPosition(command.userGuid(), position)).collect(Collectors.toUnmodifiableSet()));
        userSkillRepository.saveAll(command.skillList().stream()
                .map(skill -> new UserSkill(command.userGuid(), skill)).collect(Collectors.toUnmodifiableSet()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean adminExists() {
        return userRepository.existsByUserRole(UserRole.ADMIN);
    }
}
