package teamdevhub.devhub.web.api.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.web.api.board.model.CreateBoardRequestDto;
import teamdevhub.devhub.web.api.board.model.DeleteBoardRequestDto;
import teamdevhub.devhub.web.api.board.model.SearchBoardRequestDto;
import teamdevhub.devhub.web.api.board.model.UpdateBoardRequestDto;
import teamdevhub.devhub.web.api.web.model.request.PageRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.web.api.web.resolver.LoginUser;
import teamdevhub.devhub.web.api.board.facade.BoardFacade;
import teamdevhub.devhub.web.core.board.port.in.Facade.model.BoardDetailResponseDto;
import teamdevhub.devhub.web.core.board.port.in.Facade.model.BoardSummaryResponseDto;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;

import java.util.Optional;

@Tag(name = "Board", description = "커뮤니티 게시글 API")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

	private final BoardFacade boardFacade;

	@Operation(summary = "게시글 목록 조회", description = "검색 조건으로 게시글 목록을 페이징 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping
	public ResponseEntity<DataListApiResponseDto<BoardSummaryResponseDto>> listBoard(@ModelAttribute SearchBoardRequestDto searchBoardRequestDto, PageRequestDto pageRequestDto,
			@LoginUser(required = false) AuthenticatedUser authenticatedUser) {
		String userGuid = authenticatedUser != null ? authenticatedUser.userGuid() : null;
		return ResponseEntity.ok(boardFacade.listBoard(searchBoardRequestDto.toCommand(), PageCommand.of((pageRequestDto.getPage()), pageRequestDto.getSize()), userGuid));
	}
	
	@Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "작성 성공"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
	})
	@PostMapping
	public ResponseEntity<DataApiResponseDto<Void>> createBoard(@Valid @RequestBody CreateBoardRequestDto createBoardRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
		return ResponseEntity.ok(boardFacade.createBoard(createBoardRequestDto.toCommand(authenticatedUser.userGuid())));
	}

	@Operation(summary = "게시글 상세 조회", description = "게시글 상세 내용을 조회합니다. 쿠키 기반으로 중복 조회수를 방지합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping("/{boardGuid}")
	public ResponseEntity<DataApiResponseDto<BoardDetailResponseDto>> detailBoard(
			@Parameter(description = "게시글 GUID", required = true) @PathVariable("boardGuid") String boardGuid,
			HttpServletRequest request, HttpServletResponse response,  @LoginUser(required = false) AuthenticatedUser authenticatedUser) {
		boolean cookieResult = isCookie(boardGuid, request, response);
		String userGuid = Optional.ofNullable(authenticatedUser)
				.map(AuthenticatedUser::userGuid)
				.orElse(null);
		return ResponseEntity.ok(boardFacade.detailBoard(boardGuid, cookieResult, userGuid));
	}
	
	private boolean isCookie(String boardGuid, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("boardView")) {
                	if(cookie.getValue().contains("[" + boardGuid + "]")) return false;
                	
                	cookie.setValue(cookie.getValue() + "_[" + boardGuid + "]");
                	cookie.setPath("/");
                	cookie.setMaxAge(60 * 60 * 24);
                	response.addCookie(cookie);
                	return true;
                }
            }
        }
        
        Cookie newCookie = new Cookie("boardView","[" + boardGuid + "]");
        newCookie.setPath("/");
        newCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(newCookie);
        return true;
	}
	
	@Operation(summary = "게시글 수정", description = "게시글 내용을 수정합니다. 작성자만 수정 가능합니다.")
	@ApiResponse(responseCode = "200", description = "수정 성공")
	@PutMapping("/{boardGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> updateBoard(@Valid @RequestBody UpdateBoardRequestDto updateBoardRequestDto, @LoginUser AuthenticatedUser authenticatedUser,
			@Parameter(description = "게시글 GUID", required = true) @PathVariable("boardGuid") String boardGuid) {
		return ResponseEntity.ok(boardFacade.updateBoard(updateBoardRequestDto.toCommand(authenticatedUser.userGuid(), boardGuid)));
	}
		
	@Operation(summary = "게시글 좋아요", description = "게시글에 좋아요를 추가합니다.")
	@ApiResponse(responseCode = "200", description = "좋아요 성공")
	@PostMapping("/{boardGuid}/likes")
	public ResponseEntity<DataApiResponseDto<Void>> likeBoard(
			@Parameter(description = "게시글 GUID", required = true) @PathVariable("boardGuid") String boardGuid,
			@LoginUser AuthenticatedUser authenticatedUser) {
		return ResponseEntity.ok(boardFacade.likeBoard(boardGuid, authenticatedUser.userGuid()));
	}
	
	@Operation(summary = "게시글 삭제", description = "게시글 GUID 목록을 받아 일괄 삭제합니다.")
	@ApiResponse(responseCode = "200", description = "삭제 성공")
	@PostMapping("/delete")
	public ResponseEntity<DataApiResponseDto<Void>> deleteBoard(@Valid @RequestBody DeleteBoardRequestDto deleteBoardRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
		return ResponseEntity.ok(boardFacade.deleteBoard(deleteBoardRequestDto.getBoardGuid(), authenticatedUser.userGuid()));
	}
}
