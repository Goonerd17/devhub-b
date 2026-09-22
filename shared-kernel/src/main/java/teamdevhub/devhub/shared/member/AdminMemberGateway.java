package teamdevhub.devhub.shared.member;

public interface AdminMemberGateway {
    AdminMemberPage search(AdminMemberSearch search, int page, int size);
    AdminMemberView detail(String userGuid);
    void update(AdminMemberUpdate update);
    void ban(AdminMemberBan ban);
    void unban(String userGuid);
}
