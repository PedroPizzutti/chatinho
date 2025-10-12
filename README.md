# Chatinho

**Chatinho** é um projeto backend modular feito com SpringBoot para comunicação em tempo real, implementando um sistema de chat via WebSocket, que utiliza de serviço de mensageria com RabbitMQ para persistência de mensagens e possui um API Rest para as funcionalidades de criação de salas, busca de mensagens antigas, autenticação e convite.

## Arquitetura

O projeto é dividido em três camadas principais:

1. **WebSocket**
   Responsável pela comunicação em tempo real entre clientes. Ele gerencia a troca instantânea de mensagens nas salas de chat.

2. **Worker (RabbitMQ)**
   Um worker que consome mensagens de uma fila do RabbitMQ e as persiste no banco de dados, garantindo que as mensagens enviadas via WebSocket sejam armazenadas de forma confiável.

3. **API REST**
   Endpoints para:

   * Criar e gerenciar salas de chat
   * Obter mensagens antigas
   * Convidar usuários para salas
   * Autenticação e login de usuários

## Tecnologias Utilizadas

* **Java 21**
* **Spring Boot**
* **RabbitMQ (AMQP)**
* **WebSocket**
* **Banco de dados relacional** (MySQL)
* **Docker**

## Funcionalidades

* Envio e recebimento de mensagens em tempo real via WebSocket
* Persistência de mensagens via worker RabbitMQ
* Criação, atualização e listagem de salas de chat
* Convidar usuários para salas
* Login e autenticação de usuários
* Consulta de mensagens antigas por sala

## Estrutura do Projeto

```
src/main/java/br/com/pizzutti/chatinho
│
├── api           # Endpoints REST e serviços relacionados
├── worker        # Worker que consome filas do RabbitMQ
├── ws            # Configuração e gerenciamento de WebSocket
```
