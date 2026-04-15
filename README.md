# FitManager

Sistema de gerenciamento para academias desenvolvido em Java como projeto da disciplina de Linguagem de Programação Orientada a Objetos (LPOO) — UFMS.

O sistema permite o cadastro e controle de alunos, planos, matrículas e pagamentos por meio de uma interface interativa no terminal, organizado em arquitetura de três camadas: Interface do Usuário, Aplicação e Domínio.

---

## Integrantes

- Pedro Rodrigues Rocha
- Lucas Dias Squinca
- Luis Henrique Gonzaga Botelho

---

## Versão do Java

Java 26

---

## Como compilar e executar

### Pré-requisito

Certifique-se de ter o JDK 26 (ou superior) instalado. Para verificar:

```bash
java -version
```

### 1. Clone o repositório

```bash
git clone https://github.com/lucas-squinca/grupoE-fitmanager.git
```

### 2. Compile

A partir da raiz do projeto:

```bash
javac src/Main.java src/domain/*.java src/application/*.java src/ui/*.java
```

### 3. Execute

```bash
java -cp src Main
```

---

## Estrutura do projeto

```
grupoE-fitmanager/
├── src/
│   ├── application/        # Camada de aplicação (FitManager e serviços)
│   ├── domain/             # Camada de domínio (entidades e enums)
│   ├── ui/                 # Camada de interface (menus e UserInterface)
│   └── Main.java           # Ponto de entrada do sistema
├── report.md               # Relatório da primeira etapa
├── diagram.png             # Diagrama de classes final
└── README.md
```
