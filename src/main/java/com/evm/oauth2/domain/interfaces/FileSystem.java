package com.evm.oauth2.domain.interfaces;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileSystem {

    File newfile(String path);
    byte[] readAllBytes(Path path) throws IOException;

}
