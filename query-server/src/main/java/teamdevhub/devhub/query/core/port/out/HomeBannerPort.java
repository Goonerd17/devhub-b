package teamdevhub.devhub.query.core.port.out;
import java.time.LocalDate; import java.util.List; import teamdevhub.devhub.shared.home.HomeBannerView;
public interface HomeBannerPort { List<HomeBannerView> findExposable(boolean mainBanner, LocalDate today); }
