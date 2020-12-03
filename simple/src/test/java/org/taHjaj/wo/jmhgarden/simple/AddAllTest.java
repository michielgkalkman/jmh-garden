package org.taHjaj.wo.jmhgarden.simple;

import org.junit.Ignore;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Ignore
public class AddAllTest {

    
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

    @State(Scope.Benchmark)
    public static class EmptySet {
        Set<Object> set = new HashSet<>();
    }

    @State(Scope.Benchmark)
    public static class FullSet {
        Set<Object> set = new HashSet<>();

        @Setup(Level.Invocation)
        public void setUp() throws IOException {
            for(int i=0; i<10; i++) {
                set.add(new Object());
            }
        }
    }

    @Benchmark
    public void check(EmptySet set, Blackhole blackhole) throws IOException {
        Set<Object> allObjects = new LinkedHashSet<>();
        allObjects.addAll( set.set);

        blackhole.consume(allObjects);
    }

    @Benchmark
    public void check2(EmptySet set, Blackhole blackhole) throws IOException {
        Set<Object> allObjects = new LinkedHashSet<>(set.set);

        blackhole.consume(allObjects);
    }

    @Benchmark
    public void checkFull2(FullSet set, Blackhole blackhole) throws IOException {
        Set<Object> allObjects = new LinkedHashSet<>(set.set);

        blackhole.consume(allObjects);
    }

    @Benchmark
    public void checkFull(FullSet set, Blackhole blackhole) throws IOException {
        Set<Object> allObjects = new LinkedHashSet<>();
        allObjects.addAll( set.set);

        blackhole.consume(allObjects);
    }

    @Benchmark
    public void checkFull3(FullSet set, Blackhole blackhole) throws IOException {
        Set<Object> allObjects = new LinkedHashSet<>(100);
        allObjects.addAll( set.set);

        blackhole.consume(allObjects);
    }
}
