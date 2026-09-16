package teamdevhub.devhub.administration.core.admin.code.port.in.command;

import lombok.Builder;

@Builder
public record CommonCodeCommand(
        String code,
        String parentCode,
        String name,
        String order,
        boolean isUsed,
        String remarks
) {
    public CommonCodeCommand {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("코드는 필수입니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("명칭은 필수입니다.");
        }
    }
}
