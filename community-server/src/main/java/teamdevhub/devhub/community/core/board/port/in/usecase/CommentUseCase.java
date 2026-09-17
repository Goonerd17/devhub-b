package teamdevhub.devhub.community.core.board.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.community.core.board.domain.Comment;
import teamdevhub.devhub.community.core.board.port.in.command.CreateCommentCommand;
import teamdevhub.devhub.community.core.board.port.in.command.UpdateCommentCommand;

public interface CommentUseCase {

	List<Comment> commentList(String boardGuid);

	void createComment(CreateCommentCommand createCommentCommand);

	void deleteComment(String boardGuid, String commentGuid, String userGuid);

	void updateComment(UpdateCommentCommand updateCommentCommand);
}