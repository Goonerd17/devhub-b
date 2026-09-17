package teamdevhub.devhub.media.http.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamdevhub.devhub.media.http.model.UploadFileRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.media.http.facade.FileFacade;
import teamdevhub.devhub.media.http.facade.model.FileResponseDto;
import teamdevhub.devhub.media.http.facade.model.UploadFileResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.Map;

@Tag(name = "File", description = "파일 업로드/조회/다운로드 API")
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileFacade fileFacade;

    @Operation(summary = "파일 업로드", description = "파일을 업로드하고 파일 GUID를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "400", description = "파일 형식 오류")
    })
    @PostMapping
    public ResponseEntity<DataApiResponseDto<UploadFileResponseDto>> upload(@RequestParam Map<String, MultipartFile> uploadFiles) {
        UploadFileRequestDto uploadFileRequestDto = UploadFileRequestDto.from(uploadFiles);
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.CREATE_SUCCESS,
                        fileFacade.upload(uploadFileRequestDto.toCommandMap())
                )
        );
    }

    @Operation(summary = "파일 메타 조회", description = "파일 GUID로 파일 메타데이터(이름, 타입 등)를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{fileGuid}/meta")
    public ResponseEntity<DataApiResponseDto<FileResponseDto>> selectFile(
            @Parameter(description = "파일 GUID", required = true) @PathVariable("fileGuid") String fileGuid) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        fileFacade.selectFileObject(fileGuid)
                )
        );
    }

    @Operation(summary = "파일 인라인 조회", description = "파일을 브라우저에서 인라인으로 표시합니다.")
    @ApiResponse(responseCode = "200", description = "파일 스트림 반환")
    @GetMapping("/{fileGuid}")
    public ResponseEntity<byte[]> view(
            @Parameter(description = "파일 GUID", required = true) @PathVariable("fileGuid") String fileGuid) {
        return FileResponseFactory.inline(fileFacade.find(fileGuid));
    }

    @Operation(summary = "파일 다운로드", description = "파일을 첨부 파일로 다운로드합니다.")
    @ApiResponse(responseCode = "200", description = "다운로드 성공")
    @GetMapping("/{fileGuid}/download")
    public ResponseEntity<byte[]> download(
            @Parameter(description = "파일 GUID", required = true) @PathVariable String fileGuid) {
        return FileResponseFactory.attachment(fileFacade.find(fileGuid));
    }

    @Operation(summary = "파일 삭제", description = "파일 GUID로 파일을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @DeleteMapping("/{fileGuid}")
    public ResponseEntity<DataApiResponseDto<Void>> delete(
            @Parameter(description = "파일 GUID", required = true) @PathVariable String fileGuid) {
        fileFacade.delete(fileGuid);
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.DELETE_SUCCESS
                )
        );
    }
}
