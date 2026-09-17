package teamdevhub.devhub.admin.outbound.admin.code.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.admin.outbound.admin.code.adapter.entity.CommonCodeEntity;

import java.util.List;

public interface JpaCommonCodeRepository extends JpaRepository<CommonCodeEntity, String> {

    List<CommonCodeEntity> findAllByOrderBySortOrderAsc();
    List<CommonCodeEntity> findBySuperiorCodeIdOrderBySortOrderAsc(String superiorCodeId);

}
