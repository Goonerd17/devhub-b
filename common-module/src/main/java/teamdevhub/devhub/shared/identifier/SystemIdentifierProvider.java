package teamdevhub.devhub.shared.identifier;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class SystemIdentifierProvider implements IdentifierProvider {

    public String generateIdentifier() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }
}
