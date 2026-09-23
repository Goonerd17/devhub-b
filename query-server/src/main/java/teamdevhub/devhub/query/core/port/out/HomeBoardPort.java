package teamdevhub.devhub.query.core.port.out;
import java.util.List; import teamdevhub.devhub.shared.home.HomeBoardView;
public interface HomeBoardPort { List<HomeBoardView> findPopular(int limit, boolean sortByLike); }
