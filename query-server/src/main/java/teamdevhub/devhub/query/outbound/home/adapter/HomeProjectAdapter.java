package teamdevhub.devhub.query.outbound.home.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.query.core.home.port.in.query.HomeProjectQuery;
import teamdevhub.devhub.query.core.home.port.out.LoadHomeProjectPort;
import teamdevhub.devhub.query.outbound.home.persistence.HomeProjectQueryDao;
import teamdevhub.devhub.query.outbound.home.persistence.HomeProjectQueryDao.HomeProjectDto;
import teamdevhub.devhub.project.api.ProjectRecruitStatus;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HomeProjectAdapter implements LoadHomeProjectPort {

    private final HomeProjectQueryDao homeProjectQueryDao;

    @Override
    public List<HomeProjectResult> loadRecentProjects(HomeProjectQuery query) {
        LocalDate today = LocalDate.now();
        return homeProjectQueryDao.findHomeProjects(today, PageRequest.of(0, query.limit()))
                .stream()
                .map(dto -> new HomeProjectResult(
                        dto.projectGuid(),
                        dto.title(),
                        dto.category(),
                        dto.username(),
                        dto.imageFileGuid(),
                        dto.recruitmentStartDate() != null ? dto.recruitmentStartDate().toString() : null,
                        dto.recruitmentEndDate() != null ? dto.recruitmentEndDate().toString() : null,
                        resolveRecruitStatus(dto, today),
                        false
                ))
                .toList();
    }

    // Project.getRecruitStatus()와 동일한 규칙을 적용한다 (capacityClosed -> 날짜 미정 -> 시작 전 -> 종료 후 -> 모집중).
    private String resolveRecruitStatus(HomeProjectDto dto, LocalDate today) {
        if (dto.capacityClosed()) {
            return ProjectRecruitStatus.COMPLETED.getCode();
        }
        if (dto.recruitmentStartDate() == null || dto.recruitmentEndDate() == null) {
            return ProjectRecruitStatus.WAITING.getCode();
        }
        if (today.isBefore(dto.recruitmentStartDate())) {
            return ProjectRecruitStatus.WAITING.getCode();
        }
        if (today.isAfter(dto.recruitmentEndDate())) {
            return ProjectRecruitStatus.COMPLETED.getCode();
        }
        return ProjectRecruitStatus.RECRUITING.getCode();
    }
}
