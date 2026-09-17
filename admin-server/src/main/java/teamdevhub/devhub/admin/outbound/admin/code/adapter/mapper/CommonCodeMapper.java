package teamdevhub.devhub.admin.outbound.admin.code.adapter.mapper;

import teamdevhub.devhub.admin.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.admin.core.admin.code.domain.CommonCodeDetail;
import teamdevhub.devhub.shared.core.common.audit.AuditInfo;
import teamdevhub.devhub.admin.outbound.admin.code.adapter.entity.CommonCodeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonCodeMapper {

    private static AuditInfo toAuditInfo(CommonCodeEntity entity) {
        return AuditInfo.of(
                entity.getRegistrantGuid(),
                entity.getRegisteredDate(),
                entity.getModifierGuid(),
                entity.getModifiedDate()
        );
    }

    public static CommonCodeDetail toDetailDomain(CommonCodeEntity entity) {
        if (entity == null) return null;
        return CommonCodeDetail.builder()
                .isUsed(entity.isUsed())
                .parentCode(entity.getSuperiorCodeId())
                .remarks(entity.getRemarks())
                .code(entity.getCodeId())
                .name(entity.getName())
                .order(entity.getSortOrder())
                .auditInfo(toAuditInfo(entity))
                .build();
    }

    public static CommonCode toDomain(CommonCodeEntity entity) {
        if (entity == null) return null;
        return CommonCode.builder()
                .isUsed(entity.isUsed())
                .parentCode(entity.getSuperiorCodeId())
                .remarks(entity.getRemarks())
                .code(entity.getCodeId())
                .name(entity.getName())
                .order(entity.getSortOrder())
                .auditInfo(toAuditInfo(entity))
                .build();
    }

    public static List<CommonCodeDetail> convertToTree(List<CommonCodeEntity> allCodes) {
        Map<String, CommonCodeDetail> map = allCodes.stream()
                .map(CommonCodeMapper::toDetailDomain)
                .collect(Collectors.toMap(CommonCodeDetail::getCode, domain -> domain));

        List<CommonCodeDetail> result = new ArrayList<>();
        map.values().forEach(domain -> {
            String parentCode = domain.getParentCode();
            if (CommonCode.ROOT_PARENT_CODE.equals(parentCode) || parentCode == null || parentCode.isEmpty()) {
                result.add(domain);
            } else {
                CommonCodeDetail parent = map.get(parentCode);
                if (parent != null) {
                    parent.getChildren().add(domain);
                }
            }
        });

        return result;
    }

    public static CommonCodeEntity toEntity(CommonCode commonCode) {
        return CommonCodeEntity.builder()
                .isUsed(commonCode.isUsed())
                .codeId(commonCode.getCode())
                .superiorCodeId(commonCode.getParentCode())
                .remarks(commonCode.getRemarks())
                .sortOrder(commonCode.getOrder())
                .name(commonCode.getName())
                .build();
    }
}
