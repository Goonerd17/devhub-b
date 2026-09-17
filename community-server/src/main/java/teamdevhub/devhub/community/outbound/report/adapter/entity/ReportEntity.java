package teamdevhub.devhub.community.outbound.report.adapter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.outbound.common.persistence.jpa.converter.BooleanToYNConverter;
import teamdevhub.devhub.shared.outbound.common.persistence.jpa.audit.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "report")
public class ReportEntity extends BaseEntity {

    @Id
    @Column(length = 32)
    private String reportGuid;
    
    @Column(name = "board_guid", nullable = false)
    private String boardGuid;
    
    @Column(name = "comment_guid")
    private String commentGuid;
    
    @Column(name = "reported_user", nullable = false)
    private String reportedUser;
    
    @Column(name = "reporter_user", nullable = false)
    private String reporterUser;
    
    @Column(name = "category_cd", nullable = false)
    private String categoryCd;
    
    @Column(name = "reason", nullable = false)
    private String reason;
    
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "process_yn", nullable = false)
    private boolean isProcessed;
}