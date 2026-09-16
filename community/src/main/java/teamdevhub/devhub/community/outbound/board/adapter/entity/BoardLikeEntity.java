package teamdevhub.devhub.community.outbound.board.adapter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_like")
public class BoardLikeEntity{

    @Id
    @Column(length = 32)
    private String boardLikeGuid;
    
    @Column(name = "user_guid", nullable = false)
    private String userGuid;
    
    @Column(name = "board_guid", nullable = false)
    private String boardGuid;
   
}