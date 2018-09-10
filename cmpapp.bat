cls
cd   classes
rd   /q/s   com
cd  ..
cd  src
  javac   -d    ..\classes    -classpath     ..\lib\*;.    com\thinking\machines\dmframework\pojo\*.java
  javac   -d    ..\classes    -classpath     ..\lib\*;.    com\thinking\machines\dmframework\utilities\*.java
  javac   -d    ..\classes    -classpath     ..\lib\*;.    com\thinking\machines\dmframework\annotations\*.java
  javac   -d    ..\classes    -classpath     ..\lib\*;.    com\thinking\machines\dmframework\validators\*.java
  javac   -d    ..\classes    -classpath     ..\lib\*;.    com\thinking\machines\dmframework\dml\*.java
 javac   -d    ..\classes    -classpath     ..\lib\*;.    com\thinking\machines\dmframework\query\*.java
 javac   -d    ..\classes    -classpath     ..\lib\*;.    com\thinking\machines\dmframework\*.java
javac   -d    ..\classes    -classpath     ..\lib\*;.    com\thinking\machines\dmframework\tools\pojo\*.java
cd ..
