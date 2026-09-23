package teamdevhub.devhub.project.core.port.out;
import java.util.Optional;
public interface MemberEmailPort { Optional<String> findEmail(String memberGuid); }
