package teamdevhub.devhub.project.http.internal;

import static teamdevhub.devhub.project.outbound.project.adapter.entity.QProjectEntity.projectEntity;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.project.api.ProjectRecruitStatus;
import teamdevhub.devhub.shared.home.HomeProjectView;

@RestController
@RequestMapping("/internal/home/projects")
@RequiredArgsConstructor
public class HomeProjectInternalController {
    private final JPAQueryFactory queryFactory;

    @GetMapping
    public List<HomeProjectView> recent(@RequestParam LocalDate today, @RequestParam int limit) {
        return queryFactory.select(Projections.constructor(ProjectProjection.class,
                        projectEntity.projectGuid, projectEntity.title, projectEntity.category, projectEntity.username,
                        projectEntity.imageFileGuid, projectEntity.recruitmentStartDate, projectEntity.recruitmentEndDate,
                        projectEntity.registeredDate, projectEntity.capacityClosed))
                .from(projectEntity)
                .where(projectEntity.deleted.isFalse(), projectEntity.capacityClosed.isFalse(),
                        projectEntity.recruitmentEndDate.isNull().or(projectEntity.recruitmentEndDate.goe(today)))
                .orderBy(new CaseBuilder().when(projectEntity.recruitmentStartDate.loe(today)
                                .and(projectEntity.recruitmentEndDate.goe(today))).then(0).otherwise(1).asc(),
                        projectEntity.registeredDate.desc())
                .limit(limit).fetch().stream()
                .map(project -> new HomeProjectView(project.projectGuid(), project.title(), project.category(),
                        project.username(), project.imageFileGuid(), project.recruitmentStartDate(),
                        project.recruitmentEndDate(), project.registeredDate(), project.capacityClosed(),
                        status(project, today)))
                .toList();
    }

    private String status(ProjectProjection project, LocalDate today) {
        if (project.capacityClosed()) return ProjectRecruitStatus.COMPLETED.getCode();
        if (project.recruitmentStartDate() == null || project.recruitmentEndDate() == null)
            return ProjectRecruitStatus.WAITING.getCode();
        if (today.isBefore(project.recruitmentStartDate())) return ProjectRecruitStatus.WAITING.getCode();
        if (today.isAfter(project.recruitmentEndDate())) return ProjectRecruitStatus.COMPLETED.getCode();
        return ProjectRecruitStatus.RECRUITING.getCode();
    }

    public record ProjectProjection(String projectGuid, String title, String category, String username,
            String imageFileGuid, LocalDate recruitmentStartDate, LocalDate recruitmentEndDate,
            java.time.LocalDateTime registeredDate, boolean capacityClosed) {
    }
}
