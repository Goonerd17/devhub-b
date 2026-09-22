package teamdevhub.devhub.media.outbound.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.media.core.port.out.FileStorage;
import teamdevhub.devhub.shared.outbound.common.exception.ExternalServiceException;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    private final FileStorageProperties fileStorageProperties;

    @Override
    public String save(String fileGuid, byte[] content) {
        try {
            Path destination = safeResolve(fileGuid);
            Files.createDirectories(destination.getParent());
            Files.write(destination, content);
            return destination.toString();
        } catch (IOException e) {
            throw ExternalServiceException.of(ErrorCode.FILE_INVALID);
        }
    }

    @Override
    public byte[] read(String fileGuid) {
        try {
            Path path = safeResolve(fileGuid);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw ExternalServiceException.of(ErrorCode.FILE_INVALID);
        }
    }

    @Override
    public void delete(String fileGuid) {
        try {
            Path destination = safeResolve(fileGuid);
            Files.deleteIfExists(destination);
        } catch (IOException e) {
            throw ExternalServiceException.of(ErrorCode.FILE_INVALID);
        }
    }

    private Path safeResolve(String fileGuid) {
        Path root = fileStorageProperties.getRootPath().toAbsolutePath().normalize();
        Path resolved = fileStorageProperties.resolve(fileGuid).toAbsolutePath().normalize();
        if (!resolved.startsWith(root)) {
            throw ExternalServiceException.of(ErrorCode.FILE_INVALID);
        }
        return resolved;
    }
}
