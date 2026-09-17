package teamdevhub.devhub.fake.pure.application.port.out.project;

import teamdevhub.devhub.project.core.project.port.out.ProjectMemberRepository;

import java.util.HashSet;
import java.util.Set;

public class FakeProjectMemberRepository implements ProjectMemberRepository {

    private final Set<String> memberKeys = new HashSet<>();

    public void givenMember(String projectGuid, String userGuid) {
        memberKeys.add(key(projectGuid, userGuid));
    }

    @Override
    public boolean isMember(String projectGuid, String userGuid) {
        return memberKeys.contains(key(projectGuid, userGuid));
    }

    private String key(String projectGuid, String userGuid) {
        return projectGuid + ":" + userGuid;
    }
}
