package teamdevhub.devhub.community.outbound.board.adapter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.outbound.common.persistence.jpa.audit.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class CommentEntity extends BaseEntity {

    @Id
    @Column(length = 32)
    private String commentGuid;
    
    @Column(name = "board_guid", nullable = false)
    private String boardGuid;
    
    @Column(name = "user_guid", nullable = false)
    private String userGuid;
    
    @Column(name = "content", nullable = false)
    private String content;
}