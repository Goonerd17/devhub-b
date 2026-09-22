package teamdevhub.devhub.shared.home;

import java.time.LocalDate;
import java.util.List;

public interface HomeBannerGateway {
    List<HomeBannerView> findExposable(boolean mainBanner, LocalDate today);
}
