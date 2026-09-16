package teamdevhub.devhub.web.api.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.administration.core.admin.code.domain.CommonCodeDetail;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonCodeResponseDto {

	private String code;
    private String parentCode;
    private String name;
    private String remarks;
    private String order;
    private boolean isUsed;
    private List<CommonCodeResponseDto> children;

    public static CommonCodeResponseDto fromDetailDomain(CommonCodeDetail commonCode) {
        return builder()
                .code(commonCode.getCode())
                .parentCode(commonCode.getParentCode())
                .name(commonCode.getName())
                .isUsed(commonCode.isUsed())
                .remarks(commonCode.getRemarks())
                .order(commonCode.getOrder())
                .children(Optional.ofNullable(commonCode.getChildren())
                        .orElseGet(Collections::emptyList).stream().map(CommonCodeResponseDto::fromDetailDomain).toList())
                .build();
    }
}