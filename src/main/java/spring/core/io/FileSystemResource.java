package spring.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileSystemResource implements Resource {
    private final File file;

    public FileSystemResource(String path) {
        this.file = new File(path);
    }

    @Override
    public InputStream getInputStream() throws Exception {
        return new FileInputStream(this.file);
    }
}
