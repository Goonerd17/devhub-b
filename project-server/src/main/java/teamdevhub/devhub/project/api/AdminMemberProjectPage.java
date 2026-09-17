package teamdevhub.devhub.project.api;
import java.util.List;
public record AdminMemberProjectPage(List<AdminMemberProjectResult> content,int page,int size,long totalElements,int totalPages,boolean first,boolean last) { }
