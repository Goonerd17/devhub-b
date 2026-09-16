package teamdevhub.devhub.member.core.user.domain.vo.command;

import lombok.Builder;
import java.util.List;

@Builder
public record CreateUserCommand(
        String userGuid,
        String username,
        String introduction,
        List<String> positionList,
        List<String> skillList
) {

}
