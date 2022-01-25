@echo off

call mvn clean install -P repo
call mvn install -P fork

sleep 5
echo "Benchmark on repo ..."
java -jar "target\benchmark-repo.jar" -f 1 -wi 1 -wf 0 -i 60 -r 1 -w 1 -o target\repo.txt

sleep 5
echo "Benchmark on fork ..."
java -jar "target\benchmark-fork.jar" -f 1 -wi 1 -wf 0 -i 60 -r 1 -w 1 -o target\fork.txt
