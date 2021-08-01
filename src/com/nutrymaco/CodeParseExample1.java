package com.nutrymaco;

import com.nutrymaco.lang.execution.Frame;
import com.nutrymaco.lang.parsing.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CodeParseExample1 {
    public static void main(String[] args) throws IOException {
        var code = String.join("",
                Files.readAllLines(Path.of("/Users/smykovefim/Documents/MyProjects/Java/aulang/resources/" +
                        "testcode")))
                .replaceAll("[\s\t\r]", "");
        System.out.println(code);
        var parser = new Parser(code, new Frame());
        parser.execute();
    }
}
