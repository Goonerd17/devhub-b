package teamdevhub.devhub.project.outbound.auth;

import java.util.Optional;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.project.core.port.out.MemberEmailPort;

@Component
public class MemberCredentialHttpClient implements MemberEmailPort {
    private final MemberCredentialFeignClient client;

    public MemberCredentialHttpClient(MemberCredentialFeignClient client) {
        this.client = client;
    }

    @Override
    public Optional<String> findEmail(String memberGuid) {
        return client.findEmail(memberGuid);
    }
}
