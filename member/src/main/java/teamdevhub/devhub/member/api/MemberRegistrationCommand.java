package teamdevhub.devhub.member.api;

import java.util.List;

/** 회원 컨텍스트가 소유하는 등록 입력 계약. HTTP/영속성 타입을 노출하지 않는다. */
public record MemberRegistrationCommand(
        String userGuid,
        String username,
        String introduction,
        List<String> positionList,
        List<String> skillList,
        MemberRole role) {
}
