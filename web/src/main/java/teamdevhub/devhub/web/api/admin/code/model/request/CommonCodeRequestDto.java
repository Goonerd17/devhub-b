package teamdevhub.devhub.web.api.admin.code.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.administration.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.administration.core.admin.code.port.in.command.CommonCodeCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonCodeRequestDto {

    private String code;
    private String parentCode;
    private String name;
    private String order;
    private boolean used;
    private boolean insert;
    private String remarks;

    public static CommonCodeRequestDto fromDomain(CommonCode commonCode) {
        return builder()
                .code(commonCode.getCode())
                .parentCode(commonCode.getParentCode())
                .name(commonCode.getName())
                .used(commonCode.isUsed())
                .build();
    }

    public CommonCodeCommand toCommonCodeCommand() {
        return CommonCodeCommand.builder()
                .code(code)
                .parentCode(parentCode)
                .name(name)
                .isUsed(used)
                .order(order)
                .remarks(remarks)
                .build();
    }
}
