package teamdevhub.devhub.shared.home;

import java.util.List;

public interface HomeBoardGateway {
    List<HomeBoardView> findPopular(int limit, boolean sortByLike);
}
