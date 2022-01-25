package org.apache.commons.io;

import org.openjdk.jmh.annotations.*;

import org.apache.commons.io.FilenameUtils;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@Fork(warmups = 2, value = 1)
public class FilenameUtilsBenchmark {


    @Param({"/", "\\", "/abc", "\\abc"})
    String filename;

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void getFullPath(Blackhole blackhole) {
        blackhole.consume(FilenameUtils.getFullPath(filename));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void getFullPathNoEndSeparator(Blackhole blackhole) {
        blackhole.consume(FilenameUtils.getFullPathNoEndSeparator(filename));
    }
}
