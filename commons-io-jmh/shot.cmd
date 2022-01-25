@echo off

call mvn clean install -P repo
call mvn install -P fork

sleep 5
echo "SingleShot Profile benchmark on repo ..."
java -jar "target\benchmark-repo.jar" -bm SingleShotTime  -f 1 -wi 3 -wf 0 -i 30  -r 1 -w 1 -o target\repo.txt

sleep 5
echo "SingleShot Profile benchmark on fork ..."
java -jar "target\benchmark-fork.jar" -bm SingleShotTime  -f 1 -wi 3 -wf 0 -i 30  -r 1 -w 1 -o target\fork.txt
