package com.legec.tkom.core;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class EmailReaderTest {

    private static final String TEST_FILE = "First line\n\n\nSecond\n abc\n\nefg   \n   \n end  ";

    @Test
    public void contentReturnedByReaderShouldBeEqual() throws IOException{
        Path filePath = Paths.get("testFile.txt");
        List<String> fileContent = new ArrayList<>();
        fileContent.add(TEST_FILE);
        Files.write(filePath, fileContent);
        EmailReader emailReader = new EmailReader();
        emailReader.setInputFile(filePath);
        for(int i = 0; i < TEST_FILE.length(); i++){
            char testChar = TEST_FILE.charAt(i);
            char readerChar = emailReader.getNextCharacter();
            assertTrue("Chars at position equals", testChar == readerChar);
        }
        assertFalse("Reader is empty", emailReader.hasNext());
        Files.delete(filePath);
    }
}
