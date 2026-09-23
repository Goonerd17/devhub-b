package teamdevhub.devhub.admin.core.port.out;

import teamdevhub.devhub.shared.member.*;

public interface AdminMemberPort {
    AdminMemberPage search(AdminMemberSearch search, int page, int size);
    AdminMemberView detail(String userGuid);
    void update(AdminMemberUpdate update);
    void ban(AdminMemberBan ban);
    void unban(String userGuid);
}
