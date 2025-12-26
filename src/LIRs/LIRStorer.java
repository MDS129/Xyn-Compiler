package LIRs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class LIRStorer {

    private final Path buildDir;
    private final Path outputFile;


    public LIRStorer(Path sourceFile) {
        Path sourceFile1 = sourceFile.toAbsolutePath();

        Path parent = sourceFile1.getParent();
        if (parent == null) {
            parent = Path.of(".");
        }

        this.buildDir = parent.resolve("build");
        this.outputFile = buildDir.resolve(getBaseName(sourceFile) + ".xir");
    }

    public void store(String lir) {
        try {
            ensureBuildDir();

            Files.writeString(
                    outputFile,
                    lir,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to write LIR file: " + outputFile, e
            );
        }
    }


    private void ensureBuildDir() throws IOException {
        Files.createDirectories(buildDir);
    }


    private static String getBaseName(Path file) {
        String name = file.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return (dot == -1) ? name : name.substring(0, dot);
    }

    public Path getOutputFile() {
        return outputFile;
    }
}