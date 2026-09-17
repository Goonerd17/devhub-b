package teamdevhub.devhub.community.outbound.board.adapter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.outbound.common.persistence.jpa.audit.BaseEntity;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "board")
public class BoardEntity extends BaseEntity {

    @Id
    @Column(length = 32)
    private String boardGuid;
    
    @Column(name = "user_guid", nullable = false)
    private String userGuid;
    
    @Column(name = "category_cd", nullable = false)
    private String categoryCd;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "content", nullable = false, length = 3000)
    private String content;
    
    @Column(name = "view_count")
    private int viewCount;
}