rem set JAVA_HOME=
rem set JRE_HOME=
rem set PATH=

java -jar jmeter-center-0.0.1-SNAPSHOT-jar-with-dependencies.jar data2file  .\jmeter http://127.0.0.1 true
java -jar jmeter-center-0.0.1-SNAPSHOT-jar-with-dependencies.jar data2file  .\swagger2jmx http://127.0.0.1 true
pause