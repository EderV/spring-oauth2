package com.evm.oauth2.domain.ports.out;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileSystemPort {

    File newfile(String path);
    byte[] readAllBytes(Path path) throws IOException;

}
