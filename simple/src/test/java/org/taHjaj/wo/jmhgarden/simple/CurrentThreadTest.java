package org.taHjaj.wo.jmhgarden.simple;

import org.junit.Ignore;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Ignore
public class CurrentThreadTest {

    
    @Test
    public void testCurrentThread() throws Exception {

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

    @Benchmark
    public void runCurrentThread(Blackhole blackhole) throws IOException {
        blackhole.consume(Thread.currentThread());
    }

    @Benchmark
    public void runGetStackTrace(Blackhole blackhole) throws IOException {
        blackhole.consume(Thread.currentThread().getStackTrace());
    }

}
