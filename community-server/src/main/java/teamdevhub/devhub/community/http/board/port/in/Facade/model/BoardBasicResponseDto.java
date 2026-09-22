package teamdevhub.devhub.community.http.board.port.in.Facade.model;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.community.core.board.domain.Board;

@Getter
@SuperBuilder
@NoArgsConstructor
public class BoardBasicResponseDto {
	
	private String boardGuid;
	private String userGuid;
	private String userName;
	private String userFileGuid;
	private String categoryCd;
	private String title;
	private String content;
	private String viewCount;
	
    private String registrantGuid;
    private LocalDateTime registeredDate;
    private String modifierGuid;
    private LocalDateTime modifiedDate;
    
    public static BoardBasicResponseDto fromDomain(Board board) {
    	return BoardBasicResponseDto.builder()
    			.boardGuid(board.getBoardGuid())
    			.userGuid(board.getUserGuid())
    			.userName(board.getUserName())
    			.userFileGuid(board.getUserFileGuid())
    			.categoryCd(board.getCategoryCd())
    			.title(board.getTitle())
    			.content(board.getContent())
    			.viewCount(board.getViewCount())
    			.registrantGuid(board.getAuditInfo().registrantGuid())
                .registeredDate(board.getAuditInfo().registeredDate())
                .modifierGuid(board.getAuditInfo().modifierGuid())
                .modifiedDate(board.getAuditInfo().modifiedDate())
                .build();
    		
    }
}

