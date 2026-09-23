package teamdevhub.devhub.admin.outbound.member;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.admin.core.port.out.AdminMemberPort;
import teamdevhub.devhub.shared.member.*;

@Component
public class AdminMemberHttpClient implements AdminMemberPort {
    private final AdminMemberFeignClient client;

    public AdminMemberHttpClient(AdminMemberFeignClient client) {
        this.client = client;
    }

    public AdminMemberPage search(AdminMemberSearch search, int page, int size) {
        return client.search(search, page, size);
    }

    public AdminMemberView detail(String userGuid) {
        return client.detail(userGuid);
    }

    public void update(AdminMemberUpdate update) {
        client.update(update);
    }

    public void ban(AdminMemberBan ban) {
        client.ban(ban);
    }

    public void unban(String userGuid) {
        client.unban(userGuid);
    }
}
