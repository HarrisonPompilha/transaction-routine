
## Code Challenge Pismo

**Execução no Docker**

	mvn clean install
	docker build -t app .
	docker run -d -p 8080:8080 app

**Execução sem Docker**

	mvn clean install
	java -jar ./target/transactions-1.0.jar
		
**SWAGGER**

[Swagger Documentation](http://localhost:8080/swagger-ui/index.html)

**Banco de dados**

[H2-Console](http://localhost:8080/h2-console)

	jdbcUrl: jdbc:h2:mem:transaction
	user: sa
	pass: sa
  
 
 
 **Exercicio**
 
 ![image](https://user-images.githubusercontent.com/21183665/166008591-3b4d6a4f-1f5f-4777-bd49-4b5a16c2fb7a.png)

![image](https://user-images.githubusercontent.com/21183665/166008623-9c82bf44-e84d-427f-b9ec-c3aab82c6601.png)

