package teamdevhub.devhub.fake.pure.application.port.in.usecase.home;

import teamdevhub.devhub.readmodel.core.home.domain.Banner;
import teamdevhub.devhub.readmodel.core.home.port.in.query.HomeBoardQuery;
import teamdevhub.devhub.readmodel.core.home.port.in.query.HomeProjectQuery;
import teamdevhub.devhub.readmodel.core.home.port.in.usecase.HomeQueryUseCase;
import teamdevhub.devhub.readmodel.core.home.port.out.LoadHomeBoardPort;
import teamdevhub.devhub.readmodel.core.home.port.out.LoadHomeProjectPort;

import java.util.ArrayList;
import java.util.List;

public class FakeHomeQueryUseCase implements HomeQueryUseCase {

    private List<Banner> mainBanners = new ArrayList<>();
    private List<Banner> subBanners = new ArrayList<>();
    private List<LoadHomeProjectPort.HomeProjectResult> projects = new ArrayList<>();
    private List<LoadHomeBoardPort.HomeBoardResult> boards = new ArrayList<>();

    public void givenMainBanners(List<Banner> banners) {
        this.mainBanners = new ArrayList<>(banners);
    }

    public void givenSubBanners(List<Banner> banners) {
        this.subBanners = new ArrayList<>(banners);
    }

    public void givenProjects(List<LoadHomeProjectPort.HomeProjectResult> results) {
        this.projects = new ArrayList<>(results);
    }

    public void givenBoards(List<LoadHomeBoardPort.HomeBoardResult> results) {
        this.boards = new ArrayList<>(results);
    }

    @Override
    public List<Banner> getMainBanners() {
        return List.copyOf(mainBanners);
    }

    @Override
    public List<Banner> getSubBanners() {
        return List.copyOf(subBanners);
    }

    @Override
    public List<LoadHomeProjectPort.HomeProjectResult> getRecentProjects(HomeProjectQuery query) {
        return projects.stream().limit(query.limit()).toList();
    }

    @Override
    public List<LoadHomeBoardPort.HomeBoardResult> getPopularBoards(HomeBoardQuery query) {
        return boards.stream().limit(query.limit()).toList();
    }
}
