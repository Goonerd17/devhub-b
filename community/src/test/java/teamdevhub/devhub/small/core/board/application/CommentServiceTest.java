package teamdevhub.devhub.community.core.board.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import teamdevhub.devhub.community.core.board.application.CommentService;
import teamdevhub.devhub.community.core.board.domain.Comment;
import teamdevhub.devhub.community.core.board.port.out.CommentRepository;
import teamdevhub.devhub.member.api.profile.MemberPublicProfile;
import teamdevhub.devhub.member.api.profile.MemberPublicProfileQuery;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private IdentifierProvider identifierProvider;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberPublicProfileQuery memberPublicProfileQuery;

    @Test
    void commentListFillsDisplayNameFromMemberPublicProfileQuery() {
        Comment comment = Comment.builder()
                .commentGuid("COMMENT_1")
                .boardGuid("BOARD_1")
                .userGuid("MEMBER_1")
                .content("댓글")
                .build();
        CommentService commentService = new CommentService(
                identifierProvider,
                commentRepository,
                memberPublicProfileQuery
        );
        when(commentRepository.findByBoardGuid("BOARD_1")).thenReturn(List.of(comment));
        when(memberPublicProfileQuery.findPublicProfilesByMemberGuids(List.of("MEMBER_1")))
                .thenReturn(List.of(new MemberPublicProfile("MEMBER_1", "댓글작성자")));

        List<Comment> result = commentService.commentList("BOARD_1");

        assertThat(result).containsExactly(comment);
        assertThat(result.get(0).getUserName()).isEqualTo("댓글작성자");
    }
}
