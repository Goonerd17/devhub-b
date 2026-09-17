package teamdevhub.devhub.query.core.home.port.in.usecase;

import teamdevhub.devhub.query.core.home.domain.Banner;
import teamdevhub.devhub.query.core.home.port.in.query.HomeBoardQuery;
import teamdevhub.devhub.query.core.home.port.in.query.HomeProjectQuery;
import teamdevhub.devhub.query.core.home.port.out.LoadHomeBoardPort;
import teamdevhub.devhub.query.core.home.port.out.LoadHomeProjectPort;

import java.util.List;

public interface HomeQueryUseCase {

    List<Banner> getMainBanners();

    List<Banner> getSubBanners();

    List<LoadHomeProjectPort.HomeProjectResult> getRecentProjects(HomeProjectQuery query);

    List<LoadHomeBoardPort.HomeBoardResult> getPopularBoards(HomeBoardQuery query);
}
