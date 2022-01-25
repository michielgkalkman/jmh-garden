@echo off

call mvn clean install -P fork

sleep 5
echo "Benchmark on fork ..."
java -jar "target\benchmark-fork.jar" -f 1 -wi 1 -wf 0 -i 30  -r 1 -w 1 -o target\fork.txt
