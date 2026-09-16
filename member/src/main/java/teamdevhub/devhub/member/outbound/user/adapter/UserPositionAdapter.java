package teamdevhub.devhub.member.outbound.user.adapter;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserPositionEntity;
import teamdevhub.devhub.member.outbound.user.adapter.mapper.UserPositionMapper;
import teamdevhub.devhub.member.outbound.user.persistence.JpaUserPositionRepository;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.port.out.UserPositionRepository;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserPositionAdapter extends UserRelationAdapter<UserPosition> implements UserPositionRepository {

    private final JpaUserPositionRepository jpaUserPositionRepository;

    public UserPositionAdapter(JpaUserPositionRepository jpaUserPositionRepository,
                               IdentifierProvider identifierProvider) {
        super(identifierProvider);
        this.jpaUserPositionRepository = jpaUserPositionRepository;
    }

    @Override
    public void saveAll(Set<UserPosition> positions) {
        if (positions.isEmpty()) {
            return;
        }
        List<UserPositionEntity> entities = positions.stream()
                .map(position -> UserPositionMapper.toEntity(identifierProvider.generateIdentifier(), position))
                .toList();
        jpaUserPositionRepository.saveAll(entities);
    }

    @Override
    public Set<UserPosition> findByUserGuid(String userGuid) {
        return jpaUserPositionRepository.findByUserGuid(userGuid)
                .stream()
                .map(UserPositionMapper::toRecord)
                .collect(Collectors.toSet());
    }

    @Override
    public void replace(Set<UserPosition> previousPositions, Set<UserPosition> currentPositions) {
        if (currentPositions == null || currentPositions.isEmpty()) {
            return;
        }
        syncItems(previousPositions, currentPositions);
    }

    @Override
    protected String extractUserGuid(Set<UserPosition> items) {
        return items.iterator().next().userGuid();
    }

    @Override
    protected Set<String> extractCodes(Set<UserPosition> items) {
        return items.stream().map(UserPosition::positionCd).collect(Collectors.toSet());
    }

    @Override
    protected void deleteByUserGuidAndCodes(String userGuid, Set<String> codes) {
        if (!codes.isEmpty()) {
            jpaUserPositionRepository.deleteByUserGuidAndPositionCdIn(userGuid, codes);
        }
    }

    @Override
    protected void insertByUserGuidAndCodes(String userGuid, Set<String> codes) {
        if (codes.isEmpty()) {
            return;
        }
        List<UserPositionEntity> entities = codes.stream()
                .map(code -> UserPositionEntity.builder()
                        .userPositionGuid(identifierProvider.generateIdentifier())
                        .userGuid(userGuid)
                        .positionCd(code)
                        .build())
                .toList();
        jpaUserPositionRepository.saveAll(entities);
    }
}
