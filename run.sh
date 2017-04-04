chmod a+x ./src/*.java

javac ./src/*.java
java -cp ./src Main ./log_input/log.txt ./log_output/hosts.txt ./log_output/resources.txt ./log_output/hours.txt ./log_output/blocked.txt