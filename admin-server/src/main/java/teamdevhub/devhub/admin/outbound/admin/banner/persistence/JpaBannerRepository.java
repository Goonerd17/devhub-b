package teamdevhub.devhub.admin.outbound.admin.banner.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamdevhub.devhub.admin.outbound.admin.banner.adapter.entity.BannerEntity;

import java.time.LocalDate;
import java.util.List;

public interface JpaBannerRepository extends JpaRepository<BannerEntity, String> {

    List<BannerEntity> findByMainBannerAndUsedTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderBySortOrder(
            boolean mainBanner, LocalDate startDate, LocalDate endDate);

    @Query("""
            SELECT b FROM BannerEntity b
            WHERE (:keyword IS NULL OR b.title LIKE %:keyword% OR b.description LIKE %:keyword%)
            AND (:isUsed IS NULL OR b.used = :isUsed)
            AND (:isMainBanner IS NULL OR b.mainBanner = :isMainBanner)
            AND (:alwaysPublication IS NULL OR (
                (:alwaysPublication = true AND b.endDate = :maxDate)
                OR
                (:alwaysPublication = false AND b.endDate <> :maxDate)
            ))
            AND (:startDate IS NULL OR b.startDate <= :startDate)
            AND (:endDate IS NULL OR b.endDate >= :endDate)
            """)
    Page<BannerEntity> findByComplexCondition(
            @Param("username") String keyword,
            @Param("isUsed") Boolean isUsed,
            @Param("isMainBanner") Boolean isMainBanner,
            @Param("alwaysPublication") Boolean alwaysPublication,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("maxDate") LocalDate maxDate,
            Pageable pageable
    );

}
