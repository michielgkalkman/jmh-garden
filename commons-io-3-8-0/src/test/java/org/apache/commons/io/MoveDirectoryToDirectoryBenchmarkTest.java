package org.apache.commons.io;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.link.BinaryLinkClient;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.spf4j.stackmonitor.Spf4jJmhProfiler;
import stackmonitor.JmhNewFlightRecorderProfiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class MoveDirectoryToDirectoryBenchmarkTest {


    @Test
    public void testCopyDirectories() throws Exception {

//        final String destinationFolder = System.getProperty("basedir",
//                org.spf4j.base.Runtime.USER_DIR) + "/target";
//        final String profile = System.getProperty("basedir",
//                org.spf4j.base.Runtime.USER_DIR) + "/src/main/jfc/profile.jfc";
        Options opt = new OptionsBuilder()
                // Specify which benchmarks to run.
                // You can be more specific if you'd like to run only one benchmark per test.
                .include(this.getClass().getName() + ".*")
//                .addProfiler(JmhNewFlightRecorderProfiler.class)
//                .jvmArgs("-Xmx256m", "-Xms256m", // "-XX:+UnlockCommercialFeatures",
//                        "-Djmh.stack.profiles=" + destinationFolder,
//                        "-Djmh.executor=FJP",
//                        "-Djmh.fr.options=defaultrecording=true,settings=" + profile)
                .jvmArgs("-XX:+UnlockExperimentalVMOptions", "-XX:+UseEpsilonGC", "-Xms512m", "-Xmx512m", "-XX:+AlwaysPreTouch"
                    ,"-XX:FlightRecorderOptions=samplethreads=false")
//                        ,"-XX:+UnlockDiagnosticVMOptions","-XX:+DebugNonSafepoints")
//                .addProfiler(Spf4jJmhProfiler.class)
//                .addProfiler(ClassloaderProfiler.class)
//                .result(destinationFolder + "/" + "benchmarkResults.csv")
//                .resultFormat(ResultFormatType.CSV)
                // Set the following options as needed
                .mode (Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.seconds(10))
                .warmupIterations(5)
                .measurementTime(TimeValue.milliseconds(15))
                .measurementIterations(100)
                .threads(1)
                .forks(3)
                .shouldFailOnError(true)
                .shouldDoGC(false)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class MoveDirectoryToDirectory {
        File src;
        File destParentDir;
        File destDir;

        @Setup(Level.Invocation)
        public void setUp() throws IOException {
            src = Files.createTempDirectory( "moveDirectoryToDirectory-src" ).toFile();
            destParentDir = Files.createTempDirectory( "moveDirectoryToDirectory-destDir").toFile();
            destDir = new File( destParentDir, "destDir");
        }

        @TearDown(Level.Invocation)
        public void tearDown() {
            try {
                FileUtils.deleteDirectory(src);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileUtils.deleteDirectory(destParentDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Benchmark
    public void moveDirectoryToDirectory(MoveDirectoryToDirectory moveDirectoryToDirectory) throws IOException {
        moveDirectoryToDirectory(moveDirectoryToDirectory.src, moveDirectoryToDirectory.destDir, true);
    }

    @Benchmark
    public void moveDirectoryToDirectoryDedup(MoveDirectoryToDirectory moveDirectoryToDirectory) throws IOException {
        moveDirectoryToDirectoryDedup(moveDirectoryToDirectory.src, moveDirectoryToDirectory.destDir, true);
    }

    public static void moveDirectoryToDirectory(final File src, final File destDir, final boolean createDestDir)
            throws IOException {
//        validateMoveParameters(src, destDir);
        if (!destDir.exists() && createDestDir) {
//            if (!destDir.mkdirs()) {
//                throw new IOException("Could not create destination directories '" + destDir + "'");
//            }
        }
        if (!destDir.exists()) {
//            throw new FileNotFoundException("Destination directory '" + destDir +
//                    "' does not exist [createDestDir=" + createDestDir + "]");
        }
        if (!destDir.isDirectory()) {
//            throw new IOException("Destination '" + destDir + "' is not a directory");
        }
//        FileUtils.moveDirectory(src, new File(destDir, src.getName()));
    }

    public static void moveDirectoryToDirectoryDedup(final File src, final File destDir, final boolean createDestDir)
            throws IOException {
//        validateMoveParameters(src, destDir);
        if (!destDir.exists()) {
//            if( createDestDir) {
//                if (!destDir.mkdirs()) {
//                    throw new IOException("Could not create destination directories '" + destDir + "'");
//                }
//            } else {
//                throw new FileNotFoundException("Destination directory '" + destDir +
//                        "' does not exist [createDestDir=" + createDestDir + "]");
//            }
        } else if (!destDir.isDirectory()) {
//            throw new IOException("Destination '" + destDir + "' is not a directory");
        }
//        FileUtils.moveDirectory(src, new File(destDir, src.getName()));
    }


    /**
     * Validates the given arguments.
     * <ul>
     * <li>Throws {@link NullPointerException} if {@code src} is null</li>
     * <li>Throws {@link NullPointerException} if {@code dest} is null</li>
     * <li>Throws {@link FileNotFoundException} if {@code src} does not exist</li>
     * </ul>
     *
     * @param source                       the file or directory to be moved
     * @param destination                      the destination file or directory
     * @throws FileNotFoundException    if {@code src} file does not exist
     */
    private static void validateMoveParameters(final File source, final File destination) throws FileNotFoundException {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        if (!source.exists()) {
            throw new FileNotFoundException("Source '" + source + "' does not exist");
        }
    }
}
