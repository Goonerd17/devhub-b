package teamdevhub.devhub.administration.core.admin.code.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.administration.core.admin.code.port.in.command.CommonCodeCommand;
import teamdevhub.devhub.platform.core.common.audit.AuditInfo;

@Getter
@Builder
public class CommonCode {

    public static final String ROOT_PARENT_CODE = "0000";

    private String code;
    private String parentCode;
    private String name;
    private String order;
    private boolean isUsed;
    private String remarks;
    private AuditInfo auditInfo;

    public static CommonCode createCommonCode(CommonCodeCommand commonCodeCommand) {
        return builder()
                .code(commonCodeCommand.code())
                .parentCode(resolveParentCode(commonCodeCommand.parentCode()))
                .name(commonCodeCommand.name())
                .order(commonCodeCommand.order())
                .isUsed(commonCodeCommand.isUsed())
                .remarks(commonCodeCommand.remarks())
                .auditInfo(AuditInfo.empty())
                .build();
    }

    private static String resolveParentCode(String parentCode) {
        if (parentCode == null || parentCode.isBlank()) {
            return ROOT_PARENT_CODE;
        }
        return parentCode;
    }

}
