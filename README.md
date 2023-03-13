# Projeto AUDSAT - SEGUROS

### Documentação

AUDSAT - SEGUROS é uma REST API que fornece serviços para o cálculo de risco
para novos clientes.

### Instruções

Para testes subir a aplicação utilizando o profile dev. Este profile irá pré popular
as tabelas.

Acesso ao H2 Console
http://localhost:8080/h2-console/
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: password

### Open API Documentation
http://localhost:8080/audsat-api/swagger-ui/index.html

### Docker
É possível criar uma imagem para o Docker

para isto basta executar:

./gradlew bootBuildImage

Após a imagem ter sido criada com sucesso, executar:

docker run -e SPRING_PROFILES_ACTIVE=dev -p 8080:8080 audsat-api:latest

Simples teste:
curl http://localhost:8080/insurance/budget/100

### Dicionário de Dados
Acessar a wiki deste repositório
