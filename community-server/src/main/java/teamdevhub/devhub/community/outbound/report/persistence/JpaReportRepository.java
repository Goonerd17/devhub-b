package teamdevhub.devhub.community.outbound.report.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.community.outbound.report.adapter.entity.ReportEntity;

public interface JpaReportRepository extends JpaRepository<ReportEntity, String> {

    Page<ReportEntity> findByReportedUser(String reportedUser, Pageable pageable);
    Page<ReportEntity> findByReporterUser(String reporterUser, Pageable pageable);

    @Modifying
    @Query("update ReportEntity r set r.isProcessed = true where r.reportGuid = :reportGuid")
    int markProcessed(@Param("reportGuid") String reportGuid);

    @Query("""
            select case when count(r) > 0 then true else false end
            from ReportEntity r
            where r.reporterUser = :reporterUser
              and (
                    (:commentGuid is not null and r.commentGuid = :commentGuid)
                 or (:commentGuid is null and r.boardGuid = :boardGuid and r.commentGuid is null)
              )
            """)
    boolean existsDuplicate(
            @Param("reporterUser") String reporterUser,
            @Param("boardGuid") String boardGuid,
            @Param("commentGuid") String commentGuid
    );
}
