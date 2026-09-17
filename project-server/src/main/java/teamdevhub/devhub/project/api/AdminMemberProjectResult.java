package teamdevhub.devhub.project.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** 관리자 사용자 화면에서 사용하는 Project/Application 읽기 projection. */
public record AdminMemberProjectResult(
        String projectGuid, String userGuid, String username, String category, String title, String content,
        String attachmentFileGuid, String imageFileGuid, String recruitmentTypeCd,
        LocalDate recruitmentStartDate, LocalDate recruitmentEndDate, String progressTypeCd,
        String progressRegionCd, String progressPeriod, LocalDate progressStartDate, LocalDate progressEndDate,
        String recruitStatus, String currentRecruitNumber, String totalRecruitNumber, String applicantNumber,
        String approvalNumber, String approvalState, String progressState, String applicationGuid,
        LocalDateTime registeredDate, LocalDateTime modifiedDate, List<Application> applications) {

    public record Application(String applicationGuid, String applicantGuid, String userName, String email,
                              String statusCd, double mannerDegree, Double score) { }
}
