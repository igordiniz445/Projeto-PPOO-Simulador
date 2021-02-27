
reset
echo "[ .. ] Compilando código fonte..."
echo "[ .. ] Acessando diretório /dev..."
cd dev/
echo "[ OK ] Diretório acessado!"
javac -encoding ISO-8859-1 -d ../build **/*.java
#javac -encoding ISO-8859-1 -d ../build Main.java
cd ../
echo "[ OK ] PROJETO COMPILADO!"
