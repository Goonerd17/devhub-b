package teamdevhub.devhub.fake.pure.application.port.out.home;

import teamdevhub.devhub.readmodel.core.home.port.in.query.HomeProjectQuery;
import teamdevhub.devhub.readmodel.core.home.port.out.LoadHomeProjectPort;

import java.util.ArrayList;
import java.util.List;

public class FakeLoadHomeProjectPort implements LoadHomeProjectPort {

    private final List<HomeProjectResult> projects = new ArrayList<>();

    public void givenProjects(List<HomeProjectResult> results) {
        projects.clear();
        projects.addAll(results);
    }

    @Override
    public List<HomeProjectResult> loadRecentProjects(HomeProjectQuery query) {
        return projects.stream()
                .limit(query.limit())
                .toList();
    }
}
