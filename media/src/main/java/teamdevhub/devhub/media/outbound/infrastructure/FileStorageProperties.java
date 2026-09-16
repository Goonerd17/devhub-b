package teamdevhub.devhub.media.outbound.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {

    private Path rootPath;

    protected FileStorageProperties() {}

    public Path getRootPath() {
        return rootPath;
    }

    public void setRootPath(Path rootPath) {
        this.rootPath = rootPath;
    }

    public Path resolve(String fileGuid) {
        String prefix = fileGuid.substring(0, 2);
        return rootPath
                .resolve(prefix)
                .resolve(fileGuid);
    }
}