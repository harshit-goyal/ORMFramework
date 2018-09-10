del dist\TMORMFrameworkTools.jar
cd classes
rd  /q/s  icons
md  icons
copy  ..\icons\*.*  icons
jar -cvf TMORMFrameworkTools.jar com icons
move TMORMFrameworkTools.jar ..\dist
cd..