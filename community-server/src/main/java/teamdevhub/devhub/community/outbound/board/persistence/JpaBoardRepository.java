package teamdevhub.devhub.community.outbound.board.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.community.outbound.board.adapter.entity.BoardEntity;

public interface JpaBoardRepository extends JpaRepository<BoardEntity, String> {
	
	@Query(value = "select b " +
			"from BoardEntity b " +
			"where (:title IS NULL OR b.title LIKE %:title%) " +
			"AND (:categoryCd IS NULL OR b.categoryCd=:categoryCd) " +
			"AND (:userGuid IS NULL OR b.userGuid=:userGuid)")
	Page<BoardEntity> findByConditions(@Param("title") String title,  @Param("categoryCd") String categoryCd, @Param("userGuid") String userGuid, Pageable pageable);

	Optional<BoardEntity> findByBoardGuid(String boardGuid);
	
	@Modifying
	@Query(value = "update BoardEntity b set b.viewCount = b.viewCount + 1 " +
			"where b.boardGuid = :boardGuid ")
	int updateViewCount(@Param("boardGuid") String boardGuid);
	
	@Query("""
		    select b, (select count(r) from ReportEntity r where r.boardGuid = b.boardGuid) as reportNum
		    from BoardEntity b
		    where (:title IS NULL OR b.title LIKE %:title%)
		    and (:categoryCd IS NULL OR b.categoryCd=:categoryCd)
		    and (:registeredStartDate is null or b.registeredDate >= :registeredStartDate)
			and (:registeredEndDate is null or b.registeredDate <= :registeredEndDate)
		    and (:isReported is null or (
		            (:isReported = true and exists (select 1 from ReportEntity r where r.boardGuid = b.boardGuid)) or
		            (:isReported = false and not exists (select 1 from ReportEntity r where r.boardGuid = b.boardGuid))
		        ))
		    and (:filterByUserStatus = false or b.userGuid in :memberGuids)
		    """)
	Page<Object[]> findBySearchCondition(@Param("title") String title, @Param("categoryCd") String categoryCd,
			@Param("filterByUserStatus") boolean filterByUserStatus, @Param("memberGuids") List<String> memberGuids,
			@Param("isReported") Boolean isReported, @Param("registeredStartDate") LocalDateTime registeredStartDate, @Param("registeredEndDate") LocalDateTime registeredEndDate, Pageable pageable);
	
	void deleteAllByBoardGuidIn(List<String> boardGuids);

}
