package teamdevhub.devhub.admin.api;

import java.util.List;
import java.util.Map;

public interface CommonCodeNameQuery {
    Map<String, String> findNames(List<String> codes);
}
