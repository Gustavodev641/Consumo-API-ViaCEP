# Projeto Gerenciamento de Usuários (Fullstack)

Este projeto é um sistema completo de gerenciamento de usuários com integração de endereços via API ViaCep. Desenvolvido para demonstrar habilidades em Java Spring Boot no backend e Angular no frontend.

## Tecnologias Utilizadas

### Frontend
* **Angular 17+**
* **Angular Material** (UI/UX)
* **TypeScript**
* **RxJS**

### Backend
* **Java 23**
* **Spring Boot**
* **Spring Data JPA**
* **Oracle PL/SQL / H2 Database**
* **Maven**

##  Funcionalidades
- [x] CRUD completo de usuários.
- [x] Cadastro dinâmico de múltiplos endereços.
- [x] Busca automática de endereço por **CEP (ViaCep API)**.
- [x] Validações de formulário (CPF, E-mail, campos obrigatórios).
- [x] Tratamento de erros do servidor (ex: CPF duplicado).

##  Como Executar o Projeto

### Pré-requisitos
- Java 17 ou superior
- Node.js & Angular CLI
- Maven

### 1. Clonar o repositório
```bash
git clone https://github.com/SEU_USUARIO/NOME_DO_REPOSITORIO.git)](https://github.com/Gustavodev641/Consumo-API-ViaCEP.git
2. Configurar o back-end
Navegue até a pasta do backend.

Execute o comando:

Bash
mvn clean install
mvn spring-boot:run
3. Configurar o Frontend 
Navegue até a pasta do frontend.

Instale as partes:

Bash
npm install
Inicie o servidor:

Bash
ng serve
Acesse http://localhost:4200no seu navegador.

Desenvolvido por Gustavo Pereira
