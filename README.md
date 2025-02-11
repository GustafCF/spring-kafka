# Back-end de serviço de pedidos de uma cafeteria
  Neste projeto existem dois módulos que utilizam Kafka para produzir e consumir mensagens, o produtor envia pedidos ao tópico, e o consumidor recebe estes pedidos e os persiste no banco de dados.

# Tecnologias     
 - **Linguagem:** JAVA, HTML, CSS, JS
 - **Frameworks:** Spring Boot
 - **Ferramentas:** Kafka(Para mensageria e processamento de eventos), Git (Para versionamento do código), Postman(Para testes de requisições), Docker(Para deploy da aplicação), Maven(Ferramenta de automação de build)
 - **Banco de Dados:** MySQL

   **INFO**
     - Para rodar o projeto é necessário uma instância do Kafka que está presente no docker-compose.yml na raiz do coffe_producer_kafka, e também será necessário o mysql que possuir um docker-compose-yml na raiz do projeto coffee_consumer_kafka.
     - O proejto está documentado com swagger na url: http://localhost:8080/swagger-ui/index.html.
     - Ambos os projetos possuem um front-end nas rotas http://localhost:8080 para o consumer e http://localhost:8081 para o producer.
