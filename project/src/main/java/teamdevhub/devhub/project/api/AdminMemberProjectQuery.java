package teamdevhub.devhub.project.api;

import java.util.List;

public interface AdminMemberProjectQuery {
    AdminMemberProjectPage findRegisteredProjects(String userGuid, int page, int size);
    AdminMemberProjectPage findAppliedProjects(String userGuid, int page, int size);
}
