package teamdevhub.devhub.readmodel.outbound.home.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class HomeBannerQueryDaoImpl implements HomeBannerQueryDao {

    private final EntityManager entityManager;

    @Override
    public List<BannerReadProjection> findExposableBanners(boolean mainBanner, LocalDate today) {
        return entityManager.createQuery("select b.bannerGuid, b.title, b.imageFileGuid, b.linkUrl, b.mainBanner, b.used, b.startDate, b.endDate, b.sortOrder from BannerEntity b where b.mainBanner = :main and b.used = true and b.startDate <= :today and b.endDate >= :today order by b.sortOrder", Object[].class)
                .setParameter("main", mainBanner).setParameter("today", today).getResultList().stream()
                .map(row -> new BannerReadProjection((String) row[0], (String) row[1], (String) row[2], (String) row[3],
                        (Boolean) row[4], (Boolean) row[5], (LocalDate) row[6], (LocalDate) row[7], ((Number) row[8]).intValue())).toList();
    }
}
