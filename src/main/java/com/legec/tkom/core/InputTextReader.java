package com.legec.tkom.core;

import com.legec.tkom.core.model.Position;

import java.io.IOException;
import java.nio.file.Path;

interface InputTextReader {
    boolean hasNext();
    char getNextCharacter();
    char seeNextCharacter();
    Position getPosition();
    void setInputFile(Path inputFilePath) throws IOException;
}
