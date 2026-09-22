package teamdevhub.devhub.community.http.board.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.http.board.admin.model.DeleteAdminBoardRequestDto;
import teamdevhub.devhub.community.http.board.admin.model.SearchAdminBoardRequestDto;
import teamdevhub.devhub.community.http.request.PageRequestDto;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;
import teamdevhub.devhub.community.http.response.DataListApiResponseDto;
import teamdevhub.devhub.community.http.board.facade.BoardFacade;
import teamdevhub.devhub.community.http.board.port.in.Facade.model.AdminBoardResponseDto;
import teamdevhub.devhub.shared.core.common.page.PageCommand;

@Tag(name = "Admin - Board", description = "관리자 게시글 관리 API")
@RestController
@RequestMapping("/admin/boards")
@RequiredArgsConstructor
public class AdminBoardController {

	private final BoardFacade boardFacade;

	@Operation(summary = "게시글 목록 조회 (관리자)", description = "관리자가 검색 조건으로 전체 게시글 목록을 페이징 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping
	public ResponseEntity<DataListApiResponseDto<AdminBoardResponseDto>> listAdminBoard(@ModelAttribute SearchAdminBoardRequestDto searchAdminBoardRequestDto, PageRequestDto pageRequestDto) {
		return ResponseEntity.ok(boardFacade.listAdminBoard(searchAdminBoardRequestDto.toCommand(), PageCommand.of((pageRequestDto.getPage()), pageRequestDto.getSize())));
	}

	@Operation(summary = "게시글 삭제 (관리자)", description = "관리자가 게시글 GUID 목록을 받아 개별 또는 일괄 삭제합니다.")
	@ApiResponse(responseCode = "200", description = "삭제 성공")
	@PostMapping("/delete")
	public ResponseEntity<DataApiResponseDto<Void>> deleteAdminBoard(@Valid @RequestBody DeleteAdminBoardRequestDto deleteAdminBoardRequestDto) {
		return ResponseEntity.ok(boardFacade.deleteAdminBoard(deleteAdminBoardRequestDto.getBoardGuids()));
	}

}
