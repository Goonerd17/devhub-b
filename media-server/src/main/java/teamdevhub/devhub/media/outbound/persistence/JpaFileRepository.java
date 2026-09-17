package teamdevhub.devhub.media.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.media.outbound.adapter.entity.FileEntity;

import java.util.Optional;

public interface JpaFileRepository extends JpaRepository<FileEntity, String> {

    Optional<FileEntity> findByFileGuid(String fileGuid);
    void deleteByFileGuid(String fileGuid);
}
