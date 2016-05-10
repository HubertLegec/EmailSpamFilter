package com.legec.tkom.core;

import com.legec.tkom.core.model.Position;

import java.io.IOException;
import java.nio.file.Path;

public interface InputTextReader {
    boolean hasNext();
    char getNextCharacter();
    Position getPosition();
    void setInputFile(Path inputFilePath) throws IOException;
}
