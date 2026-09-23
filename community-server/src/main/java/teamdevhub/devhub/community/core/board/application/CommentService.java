package teamdevhub.devhub.community.core.board.application;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.core.board.domain.Comment;
import teamdevhub.devhub.community.core.board.port.in.command.CreateCommentCommand;
import teamdevhub.devhub.community.core.board.port.in.command.UpdateCommentCommand;
import teamdevhub.devhub.community.core.board.port.in.usecase.CommentUseCase;
import teamdevhub.devhub.community.core.board.port.out.CommentRepository;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;
import teamdevhub.devhub.community.core.port.out.MemberProfilePort;
import teamdevhub.devhub.shared.member.MemberPublicProfile;
import teamdevhub.devhub.shared.enums.ErrorCode;
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

	private final IdentifierProvider identifierProvider;
	private final CommentRepository commentRepository;
	private final MemberProfilePort memberPublicProfileQuery;

	@Override
	public List<Comment> commentList(String boardGuid) {
        List<Comment> commentList = commentRepository.findByBoardGuid(boardGuid);
        Map<String, String> commentUserNames = memberPublicProfileQuery
                .findPublicProfilesByMemberGuids(commentList.stream().map(Comment::getUserGuid).toList())
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        MemberPublicProfile::memberGuid,
                        MemberPublicProfile::displayName
                ));
        commentList.forEach(comment ->
        	comment.fillUserName(commentUserNames.getOrDefault(comment.getUserGuid(), ""))
        		);

        return commentList;
	}

	@Override
	public void createComment(CreateCommentCommand createCommentCommand) {
		String commentGuid = identifierProvider.generateIdentifier();
		Comment comment = Comment.createComment(createCommentCommand, commentGuid);
		commentRepository.save(comment);
	}

	@Override
	public void deleteComment(String boardGuid, String commentGuid, String userGuid) {
		Comment comment = commentRepository.findByCommentGuid(commentGuid);
		validateCommentBoard(comment, boardGuid);
		validateOwner(comment.getUserGuid(), userGuid);
		commentRepository.deleteByBoardGuidAndCommentGuid(boardGuid, commentGuid);
	}

	@Override
	public void updateComment(UpdateCommentCommand updateCommentCommand) {
		Comment comment = commentRepository.findByCommentGuid(updateCommentCommand.commentGuid());

		validateCommentBoard(comment, updateCommentCommand.boardGuid());
		validateOwner(comment.getUserGuid(), updateCommentCommand.userGuid());

		comment.updateContent(updateCommentCommand.content());
		commentRepository.updateComment(comment);
	}

	private void validateCommentBoard(Comment comment, String boardGuid) {
		if (!comment.getBoardGuid().equals(boardGuid)) {
	        throw BusinessRuleException.of(ErrorCode.READ_FAIL);
	    }
	}

	private void validateOwner(String ownerGuid, String requestUserGuid) {
		if (!ownerGuid.equals(requestUserGuid)) {
			throw BusinessRuleException.of(ErrorCode.AUTH_INVALID);
		}
	}
}
