# Sistema de Gerenciamento de Usuários — Fullstack

Aplicação fullstack para gerenciamento de usuários com endereços integrados via API ViaCEP.
Desenvolvida com Java Spring Boot no backend e Angular no frontend.

##  Links

- **Frontend (Vercel):** https://consumo-api-viacep-f5mmpowpf-gustavodev641s-projects.vercel.app
- **Backend (Railway):** https://via-cep-production.up.railway.app
- **Repositório:** https://github.com/Gustavodev641/Consumo-API-ViaCEP

---

## 🛠 Tecnologias

### Backend
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Oracle (produção) / H2 (desenvolvimento)
- Maven

### Frontend
- Angular 17+
- Angular Material
- TypeScript
- RxJS

---

##  Funcionalidades

- CRUD completo de usuários
- Cadastro de múltiplos endereços por usuário
- Busca automática de endereço por CEP via API ViaCEP
- Validação de formulários (e-mail, campos obrigatórios)
- Feedback visual com spinners e toasts
- Tratamento global de erros no backend

---

##  Como Executar Localmente

### Pré-requisitos
- Java 21+
- Node.js 18+ e Angular CLI
- Maven

### 1. Clonar o repositório
```bash
git clone https://github.com/Gustavodev641/Consumo-API-ViaCEP.git
cd Consumo-API-ViaCEP
```

### 2. Backend
```bash
cd CEP
mvn clean install -DskipTests
mvn spring-boot:run
```
Acesse: `http://localhost:8081`

### 3. Frontend
```bash
cd frontend
npm install
ng serve
```
Acesse: `http://localhost:4200`

---

##  Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/usuarios` | Lista todos os usuários |
| GET | `/usuarios/{id}` | Busca usuário por ID |
| POST | `/usuarios` | Cria usuário com endereço |
| PUT | `/usuarios/{id}` | Atualiza usuário |
| DELETE | `/usuarios/{id}` | Remove usuário e endereços |
| GET | `/cep/{cep}` | Valida e busca dados do CEP |

---

## Configuração do Banco de Dados

### Desenvolvimento (H2)
O projeto já vem configurado para H2 em memória.
Acesse o console em: `http://localhost:8081/h2-console`

### Produção (Oracle)
Configure as variáveis no `application-prod.properties`:
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XE
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.profiles.active=prod
```

---

Desenvolvido por **Gustavo Pereira**