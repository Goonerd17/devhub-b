package teamdevhub.devhub.shared.home;

import java.time.LocalDate;
import java.util.List;

public interface HomeProjectGateway {
    List<HomeProjectView> findRecent(LocalDate today, int limit);
}
