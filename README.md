## Code Challenge Pismo

**Execução no Docker**

	* mvn clean install
	* docker build -t app .
	* docker run -d -p 8080:8080 app

**Execução sem Docker**

	* Ir na raiz do projeto e inserir os comandos abaixo;
		* mvn clean install
		* java -jar ./target/transactions-1.0.jar
		

[Swagger Documentation](http://localhost:8080/swagger-ui/index.html)
[H2-Console](http://localhost:8080/h2-console)
  * user: sa
  * pass: sa
  
