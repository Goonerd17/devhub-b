package teamdevhub.devhub.fake.pure.application.port.out.home;

import teamdevhub.devhub.readmodel.core.home.port.in.query.HomeBoardQuery;
import teamdevhub.devhub.readmodel.core.home.port.out.LoadHomeBoardPort;

import java.util.ArrayList;
import java.util.List;

public class FakeLoadHomeBoardPort implements LoadHomeBoardPort {

    private final List<HomeBoardResult> boards = new ArrayList<>();

    public void givenBoards(List<HomeBoardResult> results) {
        boards.clear();
        boards.addAll(results);
    }

    @Override
    public List<HomeBoardResult> loadPopularBoards(HomeBoardQuery query) {
        return boards.stream()
                .limit(query.limit())
                .toList();
    }
}
