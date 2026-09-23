package teamdevhub.devhub.admin.outbound.member;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;
import teamdevhub.devhub.admin.core.port.out.AdminMemberPort;
import teamdevhub.devhub.shared.member.*;

@FeignClient(name = "member-server", configuration = InternalFeignConfiguration.class)
public interface AdminMemberFeignClient extends AdminMemberPort {
    @Override @PostMapping("/internal/admin/members/search")
    AdminMemberPage search(@RequestBody AdminMemberSearch search, @RequestParam int page, @RequestParam int size);
    @Override @GetMapping("/internal/admin/members/{userGuid}")
    AdminMemberView detail(@PathVariable String userGuid);
    @PutMapping("/internal/admin/members/{userGuid}")
    void updateByGuid(@PathVariable String userGuid, @RequestBody AdminMemberUpdate update);
    @Override default void update(AdminMemberUpdate update) { updateByGuid(update.userGuid(), update); }
    @PostMapping("/internal/admin/members/{userGuid}/ban")
    void banByGuid(@PathVariable String userGuid, @RequestBody AdminMemberBan ban);
    @Override default void ban(AdminMemberBan ban) { banByGuid(ban.userGuid(), ban); }
    @Override @PostMapping("/internal/admin/members/{userGuid}/unban")
    void unban(@PathVariable String userGuid);
}
