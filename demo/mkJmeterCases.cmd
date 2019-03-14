rem set JAVA_HOME=
rem set JRE_HOME=
rem set PATH=
%-Drewrite.tests=true%
%-Dprj.root=.%
java -jar jmeter-center-0.0.1-SNAPSHOT-jar-with-dependencies.jar ut2jmx .\ut .\jmeter
pause