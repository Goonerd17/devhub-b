package teamdevhub.devhub.admin.outbound.member;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.member.*;

@Component
public class AdminMemberHttpClient implements AdminMemberGateway {
    private final RestClient client;

    public AdminMemberHttpClient(RestClient.Builder builder,
            @Value("${services.member.base-url:http://member-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public AdminMemberPage search(AdminMemberSearch search, int page, int size) {
        return client.post().uri(uri -> uri.path("/internal/admin/members/search")
                .queryParam("page", page).queryParam("size", size).build())
                .body(search).retrieve().body(AdminMemberPage.class);
    }

    public AdminMemberView detail(String userGuid) {
        return client.get().uri("/internal/admin/members/{userGuid}", userGuid)
                .retrieve().body(AdminMemberView.class);
    }

    public void update(AdminMemberUpdate update) {
        client.put().uri("/internal/admin/members/{userGuid}", update.userGuid())
                .body(update).retrieve().toBodilessEntity();
    }

    public void ban(AdminMemberBan ban) {
        client.post().uri("/internal/admin/members/{userGuid}/ban", ban.userGuid())
                .body(ban).retrieve().toBodilessEntity();
    }

    public void unban(String userGuid) {
        client.post().uri("/internal/admin/members/{userGuid}/unban", userGuid)
                .retrieve().toBodilessEntity();
    }
}
