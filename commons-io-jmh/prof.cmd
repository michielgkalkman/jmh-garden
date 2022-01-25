@echo off

call mvn clean install -P repo
call mvn install -P fork

sleep 5
echo "Profile benchmark on repo ..."
rmdir /s /q prof-repo
mkdir prof-repo
cd prof-repo
java -jar "..\target\benchmark-repo.jar" -f 1 -wi 1 -wf 0 -i 30  -r 1 -w 1 -o ..\target\repo.txt -prof jfr

cd ..
sleep 5
echo "Profile benchmark on fork ..."

rmdir /s /q prof-fork
mkdir prof-fork
cd prof-fork
java -jar "..\target\benchmark-fork.jar" -f 1 -wi 1 -wf 0 -i 30  -r 1 -w 1 -o ..\target\fork.txt -prof jfr

cd ..
