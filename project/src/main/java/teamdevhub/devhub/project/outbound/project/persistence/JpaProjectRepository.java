package teamdevhub.devhub.project.outbound.project.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectEntity;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, String> {

	/*
	 * @Query(""" select p from ProjectEntity p left join ProjectLikeEntity pl on
	 * pl.projectGuid = p.projectGuid where exists ( select 1 from
	 * ProjectRequirementEntity pr where pr.projectGuid = p.projectGuid and
	 * (:positionCodeList is null or pr.positionCd in :positionCodeList) and
	 * (:positionLevelCodeList is null or pr.levelCd in :positionLevelCodeList) )
	 * and (:skillCodeList is null or exists ( select 1 from ProjectSkillEntity ps
	 * where ps.projectGuid = p.projectGuid and ps.skillCd in :skillCodeList )) and
	 * (:username is null or ( p.title like concat('%', :username, '%') )) and
	 * (:regionCodeList is null or p.progressRegionCd in :regionCodeList) and
	 * (:projectRecruitTypeList is null or p.recruitmentTypeCd in
	 * :projectRecruitTypeList) and (:projectRecruitStatusList is null or ( ('3201'
	 * in :projectRecruitStatusList and CURRENT_DATE between p.recruitmentStartDate
	 * and p.recruitmentEndDate) or ('3202' in :projectRecruitStatusList and
	 * CURRENT_DATE > p.recruitmentEndDate) or ('3203' in :projectRecruitStatusList
	 * and CURRENT_DATE < p.recruitmentStartDate) )) and (:progressPeriodList is
	 * null or ( ('001' in :progressPeriodList and function('timestampdiff', DAY,
	 * p.progressStartDate, p.progressEndDate) between 1 and 31) or ('002' in
	 * :progressPeriodList and function('timestampdiff', DAY, p.progressStartDate,
	 * p.progressEndDate) between 90 and 100) or ('003' in :progressPeriodList and
	 * function('timestampdiff', DAY, p.progressStartDate, p.progressEndDate)
	 * between 170 and 185) )) and (:projectProgressTypeList is null or
	 * p.progressTypeCd in :projectProgressTypeList) and (:recruitmentStartDate is
	 * null or p.recruitmentStartDate >= :recruitmentStartDate) and
	 * (:recruitmentEndDate is null or p.recruitmentEndDate <= :recruitmentEndDate)
	 * and (:progressStartDate is null or p.progressStartDate >= :progressStartDate)
	 * group by p order by case when :order = '001' then p.registeredDate end desc,
	 * case when :order = '002' then (p.recruitmentEndDate - CURRENT_TIMESTAMP) end
	 * asc, case when :order = '003' then count(pl.projectLikeGuid) end desc """)
	 */
	@Query("""
			select p
			from ProjectEntity p
			where exists (
			    select 1
			    from ProjectRequirementEntity pr
			    where pr.projectGuid = p.projectGuid
			    and (:positionCodeList is null or pr.positionCd in :positionCodeList)
			    and (:positionLevelCodeList is null or pr.levelCd in :positionLevelCodeList)
			)
			and (:skillCodeList is null or exists (
			    select 1
			    from ProjectSkillEntity ps
			    where ps.projectGuid = p.projectGuid
			    and ps.skillCd in :skillCodeList
			))
			and (:keyword is null or p.title like concat('%', :keyword, '%'))
			and (:regionCodeList is null or p.progressRegionCd in :regionCodeList)
			and (:projectRecruitTypeList is null or p.recruitmentTypeCd in :projectRecruitTypeList)

			and (:projectRecruitStatusList is null or (
			    ('3201' in :projectRecruitStatusList and CURRENT_DATE between p.recruitmentStartDate and p.recruitmentEndDate)
			    or ('3202' in :projectRecruitStatusList and CURRENT_DATE > p.recruitmentEndDate)
			    or ('3203' in :projectRecruitStatusList and CURRENT_DATE < p.recruitmentStartDate)
			))

			and (:progressPeriodList is null or (
			    ('001' in :progressPeriodList and function('timestampdiff', DAY, p.progressStartDate, p.progressEndDate) between 1 and 31)
			    or ('002' in :progressPeriodList and function('timestampdiff', DAY, p.progressStartDate, p.progressEndDate) between 90 and 100)
			    or ('003' in :progressPeriodList and function('timestampdiff', DAY, p.progressStartDate, p.progressEndDate) between 170 and 185)
			))

			and (:projectProgressTypeList is null or p.progressTypeCd in :projectProgressTypeList)
			and (:recruitmentStartDate is null or p.recruitmentStartDate >= :recruitmentStartDate)
			and (:recruitmentEndDate is null or p.recruitmentEndDate <= :recruitmentEndDate)
			and (:progressStartDate is null or p.progressStartDate >= :progressStartDate)
			and (:progressEndDate is null or p.progressEndDate >= :progressEndDate)

			order by
			    case when :order = '001' then p.registeredDate end desc,
			    case when :order = '002' then p.recruitmentEndDate end asc,
			    case when :order = '003' then (
			        select count(pl2)
			        from ProjectLikeEntity pl2
			        where pl2.projectGuid = p.projectGuid
			    ) end desc
			""")
	Page<ProjectEntity> findBySearchCondition(@Param("keyword") String keyword, @Param("order") String order, @Param("skillCodeList") List<String> skillCodeList,
			@Param("regionCodeList") List<String> regionCodeList, @Param("positionCodeList") List<String> positionCodeList, @Param("positionLevelCodeList") List<String> positionLevelCodeList,
			@Param("projectRecruitTypeList") List<String> projectRecruitTypeList, @Param("projectRecruitStatusList") List<String> projectRecruitStatusList,
			@Param("projectProgressTypeList") List<String> projectProgressTypeList, @Param("recruitmentStartDate") LocalDate recruitmentStartDate, @Param("recruitmentEndDate") LocalDate recruitmentEndDate,
			@Param("progressStartDate") LocalDate progressStartDate, @Param("progressEndDate") LocalDate progressEndDate, @Param("progressPeriodList") List<String> progressPeriodList, Pageable pageable);

	@Modifying
	@Query("""
			update ProjectEntity p
			set p.userGuid = COALESCE(:userGuid, p.userGuid),
				p.username = COALESCE(:username, p.username),
				p.attachmentFileGuid = COALESCE(:attachmentFileGuid, p.attachmentFileGuid),
				p.imageFileGuid = COALESCE(:imageFileGuid, p.imageFileGuid),
				p.title = COALESCE(:title, p.title),
				p.content = COALESCE(:content, p.content),
				p.recruitmentStartDate = COALESCE(:recruitmentStartDate, p.recruitmentStartDate),
				p.recruitmentEndDate = COALESCE(:recruitmentEndDate, p.recruitmentEndDate),
				p.progressStartDate = COALESCE(:progressStartDate, p.progressStartDate),
				p.progressEndDate = COALESCE(:progressEndDate, p.progressEndDate),
				p.recruitmentTypeCd = COALESCE(:recruitmentTypeCd, p.recruitmentTypeCd),
				p.progressRegionCd = COALESCE(:progressRegionCd, p.progressRegionCd),
				p.progressTypeCd = COALESCE(:progressTypeCd, p.progressTypeCd),
				p.category = COALESCE(:category, p.category)
			where p.projectGuid = :projectGuid
			""")
	void update(@Param("projectGuid") String projectGuid, @Param("userGuid") String userGuid, @Param("username") String username, @Param("attachmentFileGuid") String attachmentFileGuid, @Param("imageFileGuid") String imageFileGuid,
			@Param("title") String title, @Param("content") String content, @Param("recruitmentStartDate") LocalDate recruitmentStartDate, @Param("recruitmentEndDate") LocalDate recruitmentEndDate,
			@Param("progressStartDate") LocalDate progressStartDate, @Param("progressEndDate") LocalDate progressEndDate, @Param("recruitmentTypeCd") String recruitmentTypeCd, @Param("progressRegionCd") String progressRegionCd,
			@Param("progressTypeCd") String progressTypeCd, @Param("category") String category);

	@Query("""
			select p
			from ProjectEntity p
			where p.userGuid = :userGuid
			""")
	Page<ProjectEntity> findByUserGuid(@Param("userGuid") String userGuid, Pageable pageable);

	@Modifying
	@Query("""
			update ProjectEntity p
			set p.capacityClosed = true
			where p.projectGuid = :projectGuid
			""")
	void closeProject(@Param("projectGuid")String projectGuid);

	@Query("""
			select p
			from ProjectEntity p
			where (
			 	exists (
		            select 1
		            from ProjectApplicationEntity pa
		            join ProjectRequirementEntity pr
		                on pr.projectRequirementGuid = pa.requirementGuid
		            where pa.applicantGuid = :userGuid
		            and pa.statusCd = '3302'
		            and pr.projectGuid = p.projectGuid
		        )
	        )
			and CURRENT_DATE > p.progressStartDate
			""")
	Page<ProjectEntity> findEndProjectsByApplicantGuid(@Param("userGuid")String userGuid, Pageable pageable);

	@Query("""
		    select p
		    from ProjectEntity p
		    join ProjectRequirementEntity pr
		        on pr.projectGuid = p.projectGuid
		    where pr.projectRequirementGuid = :requirementGuid
		""")
	ProjectEntity getProjectByRequirementGuid(@Param("requirementGuid") String requirementGuid);

	@Modifying
	@Query("""
			update ProjectEntity p
			set p.title = COALESCE(:title, p.title),
				p.recruitmentStartDate = COALESCE(:recruitmentStartDate, p.recruitmentStartDate),
				p.recruitmentEndDate = COALESCE(:recruitmentEndDate, p.recruitmentEndDate),
				p.progressStartDate = COALESCE(:progressStartDate, p.progressStartDate),
				p.progressEndDate = COALESCE(:progressEndDate, p.progressEndDate),
				p.recruitmentTypeCd = COALESCE(:recruitmentTypeCd, p.recruitmentTypeCd),
				p.progressRegionCd = COALESCE(:progressRegionCd, p.progressRegionCd),
				p.progressTypeCd = COALESCE(:progressTypeCd, p.progressTypeCd)
			where p.projectGuid = :projectGuid
			""")
	void updateAdminProject(@Param("projectGuid") String projectGuid, @Param("title") String title, @Param("recruitmentTypeCd") String recruitmentTypeCd, @Param("progressTypeCd") String progressTypeCd,
			@Param("progressRegionCd") String progressRegionCd, @Param("recruitmentStartDate") LocalDate recruitmentStartDate, @Param("recruitmentEndDate") LocalDate recruitmentEndDate,
			@Param("progressStartDate") LocalDate progressStartDate, @Param("progressEndDate") LocalDate progressEndDate);

	@Query("""
			select p
			from ProjectEntity p
			where 1=1
			and (:keyword is null or p.title like concat('%', :keyword, '%'))
			and (:progressRegionCd is null or p.progressRegionCd = :progressRegionCd)
			and (:recruitmentTypeCd is null or p.recruitmentTypeCd = :recruitmentTypeCd)

			and (:recruitStatusCd is null or (
			    ('3201' = :recruitStatusCd and CURRENT_DATE between p.recruitmentStartDate and p.recruitmentEndDate)
			    or ('3202' = :recruitStatusCd and CURRENT_DATE > p.recruitmentEndDate)
			    or ('3203' = :recruitStatusCd and CURRENT_DATE < p.recruitmentStartDate)
			))
			and (:progressTypeCd is null or p.progressTypeCd = :progressTypeCd)
			and (:recruitmentStartDate is null or p.recruitmentStartDate >= :recruitmentStartDate)
			and (:recruitmentEndDate is null or p.recruitmentEndDate <= :recruitmentEndDate)
			and (:progressStartDate is null or p.progressStartDate >= :progressStartDate)
			and (:progressEndDate is null or p.progressEndDate >= :progressEndDate)

			order by p.registeredDate desc
			""")
	Page<ProjectEntity> findByAdminSearchCondition(@Param("keyword")String keyword, @Param("recruitmentTypeCd")String recruitmentTypeCd, @Param("recruitStatusCd")String recruitStatusCd,
			@Param("progressTypeCd")String progressTypeCd, @Param("progressRegionCd")String progressRegionCd, @Param("recruitmentStartDate")LocalDate recruitmentStartDate,
			@Param("recruitmentEndDate")LocalDate recruitmentEndDate, @Param("progressStartDate")LocalDate progressStartDate, @Param("progressEndDate")LocalDate progressEndDate, Pageable pageable);


}
