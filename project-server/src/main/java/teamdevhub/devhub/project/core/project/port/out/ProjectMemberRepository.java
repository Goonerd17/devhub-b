package teamdevhub.devhub.project.core.project.port.out;

public interface ProjectMemberRepository {

    boolean isMember(String projectGuid, String userGuid);
}
