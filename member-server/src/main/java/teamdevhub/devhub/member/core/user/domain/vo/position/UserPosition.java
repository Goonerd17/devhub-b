package teamdevhub.devhub.member.core.user.domain.vo.position;

import lombok.Builder;

@Builder
public record UserPosition(String userGuid, String positionCd) {}
