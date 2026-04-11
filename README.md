# API da Guilda de Aventureiros

Projeto desenvolvido em **Java com Spring Boot** para implementar o sistema de Registro Oficial da Guilda de Aventureiros.

A aplicação permite registrar, consultar, atualizar e gerenciar aventureiros e seus companheiros seguindo regras definidas pelo conselho da guilda.

---

## Como executar o projeto

Pré-requisitos:

- Java 17 ou superior
- Maven

Na pasta do projeto execute:  
mvn clean install  
mvn spring-boot:run

A aplicação iniciará em:
http://localhost:8080

### IMPORTANTE
No banco de dados pode ser necessário forçar a senha correta para funcionamento. No terminal utilize:

docker exec -it postgres-tp2 psql -U postgres

ALTER USER postgres WITH PASSWORD 'postgres';

Isso força para a senha correta necessária ao acesso da API

---

## Tecnologias utilizadas

- Java 17+
- Spring Boot 3
- Maven
- API REST
- Estruturas em memória (ArrayList simulando banco de dados)

---

## Estrutura do projeto

src/main/java/br/com/guilda

controller → endpoints da API  
service → regras de negócio  
repository → simulação de banco de dados  
model → entidades do domínio  
dto → objetos de requisição e resposta  
exception → tratamento de erros da API  
util → inicialização de dados

---

## Endpoints da API

### Listar aventureiros
GET /api/guilda

Parâmetros opcionais:

- classe
- ativo
- nivelMinimo
- page
- size

Exemplo:

/api/guilda?classe=MAGO&page=0&size=5

---

### Buscar aventureiro por id

GET /api/guilda/{id}

---

### Registrar aventureiro

POST /api/guilda

Body:

```json
{
  "nome": "Thalion",
  "classe": "ARQUEIRO",
  "nivel": 7
}
```

---

### Atualizar aventureiro
PUT /api/guilda/{id}

Body:
```json
{
  "nome": "Arthos",
  "classe": "GUERREIRO",
  "nivel": 10
}
```
---

### Encerrar vínculo com a guilda
PATCH /api/guilda/{id}/encerrar
---

### Recrutar novamente
PATCH /api/guilda/{id}/recrutar
---

### Definir ou substituir companheiro
PUT /api/guilda/{id}/companheiro

Body:
```json
{
  "nome": "Fenrir",
  "especie": "LOBO",
  "lealdade": 90
}
```
---

### Remover companheiro
DELETE /api/guilda/{id}/companheiro

---

## Regras de negócio implementadas

- ID gerado automaticamente pelo sistema
- Nome do aventureiro obrigatório
- Classe deve pertencer ao conjunto permitido
- Nível mínimo igual a 1
- Aventureiros novos iniciam como ativos
- Companheiros não existem isoladamente
- Lealdade do companheiro deve estar entre 0 e 100
- Paginação padrão: page=0 e size=10
- Ordenação crescente por ID
- Filtros por classe, ativo e nível mínimo

---
## Armazenamento

Para atender ao requisito do trabalho, não foi utilizado banco de dados.

O sistema utiliza:

```java
ArrayList<Aventureiro>
```
simulando um repositório em memória.

A aplicação inicia automaticamente com 100 aventureiros simulados.

---

## Tratamento de erros

A API retorna erros no formato:

```json
{
"mensagem": "Solicitação inválida",
"detalhes": [
"classe inválida",
"nivel deve ser maior ou igual a 1"
]
}
```
