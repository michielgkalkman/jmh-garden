package org.taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MoveDirectoryToDirectoryBenchmarkTest {
    //
    // Test tiny improvement to FileUtils::opyDirectory in
    // commons-io:commons-io:2.9.0-SNAPSHOT.
    //
    // I just removed duplicate call to File::getCanonicalPath.
    //
    // Would it show when using jmh for testing?
    //
    // I ran this code on my Dell Latitude E5550 laptop using Windows 10 
    // 64-bit with 16GB memory and an Intel(R) Core(TM) i7-5600 CPU @ 2.60GHz
    // using a LITEONIT LCS-256L9S-11 2.5 7mm 256GB disk drive
    // using JDK:
    // openjdk version "11.0.8" 2020-07-14 LTS
    // OpenJDK Runtime Environment Corretto-11.0.8.10.1 (build 11.0.8+10-LTS)
    // OpenJDK 64-Bit Server VM Corretto-11.0.8.10.1 (build 11.0.8+10-LTS, mixed mode)
    //
    // Results from two test runs:
    //
    //    Benchmark                                                                                  Mode  Cnt        Score       Error  Units
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.arunBenchmark       avgt  100    11083,590 ±   768,848  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.arunBenchmark1by1   avgt  100    51988,380 ± 11676,741  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.arunBenchmark2by2   avgt  100   204613,482 ± 37215,888  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.arunBenchmark3by3   avgt  100  1174152,673 ± 22541,114  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.dedupBenchmark1by1  avgt  100    31088,342 ±  1264,120  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.dedupBenchmark2by2  avgt  100   146071,467 ±  3787,178  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.dedupBenchmark3by3  avgt  100  1154023,563 ± 15089,337  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.deduppedBenchmark   avgt  100    14246,154 ±  2367,264  us/op
    //
    //    Benchmark                                                                                  Mode  Cnt        Score       Error  Units
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.arunBenchmark       avgt  100    11273,827 ±  2165,753  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.arunBenchmark1by1   avgt  100    32702,611 ±  1813,470  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.arunBenchmark2by2   avgt  100   152587,381 ±  4642,588  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.arunBenchmark3by3   avgt  100  1195478,564 ± 24303,450  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.dedupBenchmark1by1  avgt  100    30884,652 ±  1310,080  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.dedupBenchmark2by2  avgt  100   147020,471 ±  4613,101  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.dedupBenchmark3by3  avgt  100  1185322,846 ± 24429,620  us/op
    //    taHjaj.wo.pmdplus.dup.jmh.org.apache.commons.io.FileUtilsBenchmarkTest.deduppedBenchmark   avgt  100    11565,818 ±  1937,442  us/op
    //
    // On the whole, the deduppedBenchmarks seem to perform better, but I am definitely not an expert on this.
    //
    // In partcularly I cannot explain why the deduppedBenchmark benchmark seems to be slower than the arunBenchmark.
    //
    // Interesting sources:
    // http://tutorials.jenkov.com/java-performance/jmh.html
    // https://github.com/corretto/corretto-jmc/releases
    // https://docs.oracle.com/cd/E15289_01/JRCLR/optionxx.htm#JRCLR275 (Parameters for -XX:StartFlightRecording)
    // https://stackoverflow.com/questions/36807008/is-it-possible-to-run-a-jmh-benchmark-under-an-external-profiler#answer-37857708
    // https://github.com/zolyfarkas/spf4j
    // https://stackoverflow.com/questions/38926255/maven-annotation-processing-processor-not-found
    

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
                .jvmArgs("-XX:+UnlockExperimentalVMOptions", "-XX:+UseEpsilonGC")
//                .result(destinationFolder + "/" + "benchmarkResults.csv")
//                .resultFormat(ResultFormatType.CSV)
                // Set the following options as needed
                .mode (Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.seconds(1))
                .warmupIterations(5)
                .measurementTime(TimeValue.milliseconds(15))
                .measurementIterations(100)
                .threads(1)
                .forks(1)
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
            src = Files.createTempDirectory( "moveDirectoryToDirectroy-src" ).toFile();
            destParentDir = Files.createTempDirectory( "moveDirectoryToDirectroy-destDir").toFile();
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
    public void moveDirectoryToDirectroy(MoveDirectoryToDirectory moveDirectoryToDirectory) throws IOException {
        moveDirectoryToDirectory(moveDirectoryToDirectory.src, moveDirectoryToDirectory.destDir, true);
    }

    @Benchmark
    public void moveDirectoryToDirectroyDedup(MoveDirectoryToDirectory moveDirectoryToDirectory) throws IOException {
        moveDirectoryToDirectoryDedup(moveDirectoryToDirectory.src, moveDirectoryToDirectory.destDir, true);
    }

    public static void moveDirectoryToDirectory(final File src, final File destDir, final boolean createDestDir)
            throws IOException {
        validateMoveParameters(src, destDir);
        if (!destDir.exists() && createDestDir) {
            if (!destDir.mkdirs()) {
                throw new IOException("Could not create destination directories '" + destDir + "'");
            }
        }
        if (!destDir.exists()) {
            throw new FileNotFoundException("Destination directory '" + destDir +
                    "' does not exist [createDestDir=" + createDestDir + "]");
        }
        if (!destDir.isDirectory()) {
            throw new IOException("Destination '" + destDir + "' is not a directory");
        }
        FileUtils.moveDirectory(src, new File(destDir, src.getName()));
    }

    public static void moveDirectoryToDirectoryDedup(final File src, final File destDir, final boolean createDestDir)
            throws IOException {
        validateMoveParameters(src, destDir);
        if (!destDir.exists()) {
            if( createDestDir) {
                if (!destDir.mkdirs()) {
                    throw new IOException("Could not create destination directories '" + destDir + "'");
                }
            } else {
                throw new FileNotFoundException("Destination directory '" + destDir +
                        "' does not exist [createDestDir=" + createDestDir + "]");
            }
        } else if (!destDir.isDirectory()) {
            throw new IOException("Destination '" + destDir + "' is not a directory");
        }
        FileUtils.moveDirectory(src, new File(destDir, src.getName()));
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
