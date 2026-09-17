package teamdevhub.devhub.media.core.port.out;

public interface FileStorage {

    String save(String fileGuid, byte[] content);
    byte[] read(String fileGuid);
    void delete(String fileGuid);
}
