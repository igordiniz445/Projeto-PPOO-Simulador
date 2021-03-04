reset
echo 'Gerando documentação...'
cd dev/
sudo javadoc -d ../docs Core
sudo javadoc -d ../docs Controllers
sudo javadoc -d ../docs Utils
sudo javadoc -d ../docs View
cd ../
echo 'Documentação gerada!'