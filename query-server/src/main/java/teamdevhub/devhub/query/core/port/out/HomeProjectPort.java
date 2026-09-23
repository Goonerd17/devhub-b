package teamdevhub.devhub.query.core.port.out;
import java.time.LocalDate; import java.util.List; import teamdevhub.devhub.shared.home.HomeProjectView;
public interface HomeProjectPort { List<HomeProjectView> findRecent(LocalDate today, int limit); }
