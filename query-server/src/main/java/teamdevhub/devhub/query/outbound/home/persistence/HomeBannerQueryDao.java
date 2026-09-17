package teamdevhub.devhub.query.outbound.home.persistence;

import java.time.LocalDate;
import java.util.List;

public interface HomeBannerQueryDao {

    List<BannerReadProjection> findExposableBanners(boolean mainBanner, LocalDate today);
}
