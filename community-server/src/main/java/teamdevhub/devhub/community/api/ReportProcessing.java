package teamdevhub.devhub.community.api;

/** 관리자 신고 처리 완료를 위한 Community 공개 명령 계약. */
public interface ReportProcessing {
    void process(String reportGuid);
}
