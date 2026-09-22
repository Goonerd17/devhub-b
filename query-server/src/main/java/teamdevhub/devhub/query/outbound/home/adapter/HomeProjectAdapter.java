package teamdevhub.devhub.query.outbound.home.adapter;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import teamdevhub.devhub.query.core.home.port.in.query.HomeProjectQuery;
import teamdevhub.devhub.query.core.home.port.out.LoadHomeProjectPort;
import teamdevhub.devhub.shared.home.HomeProjectGateway;
import teamdevhub.devhub.query.outbound.project.persistence.ProjectProjectionRepository;

import java.time.LocalDate;
import java.util.List;

@Component
public class HomeProjectAdapter implements LoadHomeProjectPort {

    private final HomeProjectGateway homeProjectQueryDao;
    private final ProjectProjectionRepository projectProjectionRepository;
    private final String source;

    public HomeProjectAdapter(HomeProjectGateway homeProjectQueryDao,
            ProjectProjectionRepository projectProjectionRepository,
            @Value("${query.home.project-source:projection}") String source) {
        this.homeProjectQueryDao = homeProjectQueryDao;
        this.projectProjectionRepository = projectProjectionRepository;
        this.source = source;
    }

    @Override
    public List<HomeProjectResult> loadRecentProjects(HomeProjectQuery query) {
        LocalDate today = LocalDate.now();
        if ("projection".equalsIgnoreCase(source)) {
            List<HomeProjectResult> projected = projectProjectionRepository.findRecent(query.limit()).stream()
                    .map(project -> new HomeProjectResult(
                            project.getProjectGuid(), project.getTitle(), project.getCategory(), project.getUsername(),
                            project.getImageFileGuid(), project.getRecruitmentStartDate() == null ? null : project.getRecruitmentStartDate().toString(),
                            project.getRecruitmentEndDate() == null ? null : project.getRecruitmentEndDate().toString(),
                            project.getRecruitStatus(), false))
                    .toList();
            if (!projected.isEmpty()) {
                return projected;
            }
        }
        return homeProjectQueryDao.findRecent(today, query.limit())
                .stream()
                .map(dto -> new HomeProjectResult(
                        dto.projectGuid(),
                        dto.title(),
                        dto.category(),
                        dto.username(),
                        dto.imageFileGuid(),
                        dto.recruitmentStartDate() != null ? dto.recruitmentStartDate().toString() : null,
                        dto.recruitmentEndDate() != null ? dto.recruitmentEndDate().toString() : null,
                        dto.recruitStatus(),
                        false
                ))
                .toList();
    }

    // Project.getRecruitStatus()와 동일한 규칙을 적용한다 (capacityClosed -> 날짜 미정 -> 시작 전 -> 종료 후 -> 모집중).
}
