package teamdevhub.devhub.admin.core.admin.code.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.shared.core.common.audit.AuditInfo;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CommonCodeDetail {

    private String code;
    private String parentCode;
    private String name;
    private String order;
    private boolean isUsed;
    private String remarks;

    @Builder.Default
    private List<CommonCodeDetail> children = new ArrayList<>();

    private AuditInfo auditInfo;
}
