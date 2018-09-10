cls
cd   classes
rd   /q/s   com
cd  ..
cd  src
javac -classpath ..\..\classes;..\..\lib\*;. -d  ..\classes com\thinking\machines\dmframework\tools\*.java
cd ..

