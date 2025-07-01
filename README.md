
# ms-user

Este serviço faz parte de um sistema baseado em microsserviços. O `ms-user` é responsável pelo cadastro e autenticação de usuários, além de publicar eventos no RabbitMQ para que outros serviços possam reagir, como o envio de e-mails de boas-vindas pelo `ms-email`.

## Funcionalidades

- Cadastro de usuários.
- Autenticação com JWT.
- Publicação de eventos no RabbitMQ ao cadastrar um novo usuário.
- Integração com o serviço `ms-email` para envio de e-mails.
- Documentação da API disponível via Swagger.

## Tecnologias utilizadas

- Java 17
- Spring Boot 3.4.5
- Spring Web, JPA, Security e Validation
- JWT (com `jjwt`)
- RabbitMQ (via CloudAMQP)
- MySQL
- Lombok
- Swagger/OpenAPI

## Documentação da API

Após iniciar a aplicação, a documentação estará disponível em:

```
http://localhost:8081/swagger-ui/index.html#/
```

## Pré-requisitos

- Java 17 instalado
- Maven instalado
- MySQL em execução
- CloudAMQP (RabbitMQ na nuvem)

O serviço `ms-user` se comunica com o RabbitMQ através de uma instância remota no [CloudAMQP](https://www.cloudamqp.com/).  
Certifique-se de criar uma instância gratuita ou utilizar uma já existente para configurar os dados de conexão.

## Como rodar

Este projeto é parte de um sistema de dois microsserviços (`ms-user` e `ms-email`) que precisam ser executados simultaneamente.

Por padrão, o `ms-user` utiliza a porta `8081`.

1. Clone o repositório:

```bash
git clone https://github.com/bbering/ms-user.git
cd ms-user
```

2. Crie um arquivo `env.properties` na raiz do projeto com as seguintes variáveis configuradas:

```properties
DB_NAME=nome_do_banco
DB_USERNAME=usuario
DB_PASSWORD=senha

RABBIT_URL=amqps://usuario:senha@host/vhost

SECRET_JWT=segredo_jwt
```

> O arquivo `application.properties` já está configurado para importar automaticamente esse arquivo.

3. Execute o projeto com Maven:

```bash
mvn spring-boot:run
```

## Endpoints principais

- `POST /api/auth/signup`: cadastro de um novo usuário
- `POST /api/auth/signin`: autenticação e geração do token JWT

## Observações

- A aplicação deve estar rodando juntamente com o `ms-email`, que estará escutando os eventos publicados pelo RabbitMQ.
- Certifique-se de que o `ms-email` está rodando em outra porta (por exemplo, `8080`) e conectado à mesma instância do RabbitMQ (CloudAMQP).

## Testes

Para executar os testes automatizados:

```bash
mvn test
```

## Licença

Este projeto é livre para uso e modificação.