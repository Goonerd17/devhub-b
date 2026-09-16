package teamdevhub.devhub.fake.pure.application.port.out.file;

import teamdevhub.devhub.platform.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.media.core.port.out.FileStorage;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import java.util.HashMap;
import java.util.Map;

public class FakeFileStorage implements FileStorage {

    private final Map<String, byte[]> storage = new HashMap<>();

    @Override
    public String save(String fileGuid, byte[] content) {
        storage.put(fileGuid, content);
        return "/fake/path/" + fileGuid;
    }

    @Override
    public byte[] read(String fileGuid) {
        byte[] content = storage.get(fileGuid);
        if (content == null) {
            throw AdapterDataException.of(ErrorCode.FILE_READ_FAIL);
        }
        return content;
    }

    @Override
    public void delete(String fileGuid) {
        storage.remove(fileGuid);
    }

    public boolean exists(String fileGuid) {
        return storage.containsKey(fileGuid);
    }
}
