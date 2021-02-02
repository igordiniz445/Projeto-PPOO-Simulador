
reset
echo "[ .. ] Compilando código fonte..."
echo "[ .. ] Acessando diretório /dev..."
cd dev/
echo "[ OK ] Diretório acessado!"
javac -d ../build **/*.java
javac -d ../build Main.java
echo "[ OK ] PROJETO COMPILADO!"