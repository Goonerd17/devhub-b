package teamdevhub.devhub.web.api.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.web.api.board.model.CreateCommentRequestDto;
import teamdevhub.devhub.web.api.board.model.UpdateCommentRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.resolver.LoginUser;
import teamdevhub.devhub.web.api.board.facade.CommentFacade;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;

@Tag(name = "Board - Comment", description = "게시글 댓글 API")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class CommentController {

	private final CommentFacade commentFacade;

	@Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "댓글 작성 성공"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
	})
	@PostMapping("/{boardGuid}/comments")
	public ResponseEntity<DataApiResponseDto<Void>> createComment(@Valid @RequestBody CreateCommentRequestDto createcommentRequestDto, @LoginUser AuthenticatedUser authenticatedUser,
			@Parameter(description = "게시글 GUID", required = true) @PathVariable("boardGuid") String boardGuid) {
		return ResponseEntity.ok(commentFacade.createComment(createcommentRequestDto.toCommand(authenticatedUser.userGuid(), boardGuid)));
	}
	
	@Operation(summary = "댓글 수정", description = "댓글 내용을 수정합니다.")
	@ApiResponse(responseCode = "200", description = "댓글 수정 성공")
	@PutMapping("/{boardGuid}/comments/{commentGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> updateComment(@Valid @RequestBody UpdateCommentRequestDto updatecommentRequestDto,
			@LoginUser AuthenticatedUser authenticatedUser,
			@Parameter(description = "게시글 GUID", required = true) @PathVariable("boardGuid") String boardGuid,
			@Parameter(description = "댓글 GUID", required = true) @PathVariable("commentGuid") String commentGuid) {
		return ResponseEntity.ok(commentFacade.updateComment(updatecommentRequestDto.toCommand(boardGuid, commentGuid, authenticatedUser.userGuid())));
	}
	
	@Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
	@ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
	@DeleteMapping("/{boardGuid}/comments/{commentGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> deleteComment(
			@LoginUser AuthenticatedUser authenticatedUser,
			@Parameter(description = "게시글 GUID", required = true) @PathVariable("boardGuid") String boardGuid,
			@Parameter(description = "댓글 GUID", required = true) @PathVariable("commentGuid") String commentGuid) {
		return ResponseEntity.ok(commentFacade.deleteComment(boardGuid, commentGuid, authenticatedUser.userGuid()));
	}
}
