## Relatórios
### [Relatório - Etapa 1](#fitmanager--relatório-da-primeira-etapa)
### [Relatório - Etapa 2](#fitmanager--relatório-da-segunda-etapa)
### [Relatório - Etapa 3](#fitmanager--relatório-da-terceira-etapa)

----
# FitManager — Relatório da Primeira Etapa

## 1. Introdução

O FitManager é um sistema de gerenciamento para academias desenvolvido em Java como projeto da disciplina de Linguagem de Programação Orientada a Objetos. Nesta primeira etapa, foi construída a base funcional do sistema: modelagem do domínio, implementação das operações essenciais e organização do código em camadas.

O sistema é capaz de gerenciar alunos, planos, matrículas e pagamentos por meio de uma interface de menus interativos no terminal. A arquitetura foi estruturada em três camadas bem definidas: Interface do Usuário, Aplicação e Domínio, com separação clara de responsabilidades entre elas, de forma a facilitar a evolução do projeto nas etapas seguintes.

---

## 2. Integrantes e Contribuições

| Integrante | Foco principal |
|---|---|
| Lucas Dias Squinca | Camada de Domínio (`Student`, `Plan`, `Enrollment`, `Payment`, enums) |
| Pedro Rodrigues Rocha | Camada de Aplicação (`FitManager`, `StudentService`, `PlanService`, `EnrollmentService`, `OperationResult`) |
| Luis Henrique Gonzaga Botelho | Camada de Interface (`UserInterface`, `MainMenu`, `StudentMenu`, `PlanMenu`, `EnrollmentMenu`, `ReportsMenu`) e apoio à camada de Aplicação |

A divisão por camadas não representou uma separação de conhecimento, mas uma estratégia prática para viabilizar o desenvolvimento paralelo. As decisões de projeto foram discutidas e tomadas em conjunto pelo grupo ao longo de todo o processo, cada integrante precisava entender as responsabilidades das outras camadas para que as interfaces entre elas fizessem sentido.

**Dinâmica de desenvolvimento:** A rotina de estudos e trabalho dos integrantes impediu reuniões frequentes de desenvolvimento conjunto. O grupo optou por uma abordagem distribuída: cada integrante avançava em sua camada localmente, e as dúvidas e decisões eram alinhadas por mensagem ou em encontros pontuais. Essa dinâmica exigiu que os contratos entre camadas, as assinaturas dos métodos do `FitManager`, os retornos de `OperationResult`, as classes de domínio, fossem definidos e acordados antes de cada um iniciar sua implementação, para que as partes se encaixassem ao ser integradas.

A integração final foi realizada em uma reunião síncrona do grupo, onde as três camadas foram conectadas, os testes manuais foram executados e os ajustes necessários foram feitos em conjunto antes de subir o código para o repositório.

---

## 3. Diagrama de Classes Final

O diagrama abaixo reflete o sistema conforme implementado. A estrutura geral segue o diagrama original proposto no enunciado, com as adaptações descritas na seção de Decisões de Projeto.

```plantuml
@startuml FitManager

package "ui" {
  class UserInterface {
    -scanner: Scanner
    +showMenu(title: String, options: String[]): int
    +getInput(prompt: String): String
    +showMessage(msg: String): void
    +showError(msg: String): void
  }

  class MainMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +start(): void
  }

  class StudentMenu {
    -ui: UserInterface
    -fitManager: FitManager
    -dateFormatter: DateTimeFormatter
    +run(): void
  }

  class PlanMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +run(): void
  }

  class EnrollmentMenu {
    -ui: UserInterface
    -fitManager: FitManager
    -dateFormatter: DateTimeFormatter
    +run(): void
  }

  class ReportsMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +run(): void
  }
}

package "application" {
  class FitManager {
    -studentService: StudentService
    -planService: PlanService
    -enrollmentService: EnrollmentService
    +registerStudent(...): OperationResult
    +findStudentByCpf(cpf: String): Student
    +removeStudent(cpf: String): OperationResult
    +listStudents(): ArrayList<Student>
    +registerPlan(...): OperationResult
    +findPlanByName(name: String): Plan
    +updatePlanPrice(...): OperationResult
    +listPlans(): ArrayList<Plan>
    +enrollStudent(...): OperationResult
    +registerPayment(...): OperationResult
    +cancelEnrollment(code: int): OperationResult
    +findActiveEnrollment(cpf: String): Enrollment
    +listEnrollments(): ArrayList<Enrollment>
  }

  class StudentService {
    -students: ArrayList<Student>
    +registerStudent(...): OperationResult
    +findByCPF(cpf: String): Student
    +removeStudent(cpf: String): OperationResult
    +listStudents(): ArrayList<Student>
    +cpfExists(cpf: String): boolean
  }

  class PlanService {
    -plans: ArrayList<Plan>
    +registerPlan(...): OperationResult
    +findByName(name: String): Plan
    +updatePrice(...): OperationResult
    +listPlans(): ArrayList<Plan>
    +nameExists(name: String): boolean
  }

  class EnrollmentService {
    -enrollments: ArrayList<Enrollment>
    -{static} nextCode: int
    +enroll(...): OperationResult
    +registerPayment(...): OperationResult
    +cancel(code: int): OperationResult
    +findActiveByStudent(cpf: String): Enrollment
    +findByCode(code: int): Enrollment
    +listEnrollments(): ArrayList<Enrollment>
    +hasActiveEnrollment(cpf: String): boolean
  }

  class OperationResult {
    -success: boolean
    -message: String
    -data: Object
    +OperationResult(success: boolean, message: String)
    +OperationResult(success: boolean, message: String, data: Object)
    +isSuccess(): boolean
    +getMessage(): String
    +getData(): Object
  }
}

package "domain" {
  class Student {
    -name: String
    -cpf: String
    -contact: String
    -birthDate: LocalDate
    -active: boolean
    +{static} validateCpf(cpf: String): boolean
    +calculateAge(): int
    +activate(): void
    +deactivate(): void
    +isActive(): boolean
  }

  class Plan {
    -name: String
    -description: String
    -type: PlanType
    -minDurationMonths: int
    -pricePerMonth: double
    +calculateTotalPrice(months: int): double
    +updatePrice(newPrice: double): void
  }

  class Enrollment {
    -code: int
    -student: Student
    -plan: Plan
    -startDate: LocalDate
    -endDate: LocalDate
    -durationMonths: int
    -totalPrice: double
    -status: EnrollmentStatus
    -payments: ArrayList<Payment>
    +registerPayment(payment: Payment): void
    +calculateTotalPaid(): double
    +calculateBalance(): double
    +cancel(): void
  }

  class Payment {
    -date: LocalDate
    -amount: double
    -type: PaymentType
    -description: String
  }

  enum PlanType {
    MONTHLY
    QUARTERLY
    SEMI_ANNUAL
    ANNUAL
  }

  enum PaymentType {
    PIX
    CREDIT_CARD
    DEBIT_CARD
    CASH
  }

  enum EnrollmentStatus {
    ACTIVE
    CANCELLED
  }
}

MainMenu --> StudentMenu
MainMenu --> PlanMenu
MainMenu --> EnrollmentMenu
MainMenu --> ReportsMenu
MainMenu --> UserInterface
MainMenu --> FitManager

StudentMenu --> UserInterface
StudentMenu --> FitManager
PlanMenu --> UserInterface
PlanMenu --> FitManager
EnrollmentMenu --> UserInterface
EnrollmentMenu --> FitManager
ReportsMenu --> UserInterface
ReportsMenu --> FitManager

FitManager *-- StudentService
FitManager *-- PlanService
FitManager *-- EnrollmentService

StudentService --> Student
PlanService --> Plan
EnrollmentService --> Enrollment
EnrollmentService --> Payment

Enrollment --> Student
Enrollment --> Plan
Enrollment --> EnrollmentStatus
Enrollment "1" *-- "0..*" Payment

Plan --> PlanType
Payment --> PaymentType

@enduml
```

---

## 4. Decisões de Projeto

### 4.1 Inativação em vez de remoção física do aluno

**Decisão:** Ao "remover" um aluno, o sistema o marca como inativo via `deactivate()` em vez de eliminá-lo fisicamente da coleção do `StudentService`.

**Alternativas consideradas:** Remoção física do objeto `Student` da `ArrayList`.

**Justificativa:** Remover fisicamente o objeto `Student` deixaria as instâncias de `Enrollment` com uma referência a um objeto que não existe mais na lista do serviço, o que poderia causar inconsistências nos relatórios e consultas históricas. A inativação preserva o histórico íntegro e mantém o vínculo entre matrícula e aluno válido em memória. O atributo `active` já estava previsto no diagrama original, o que confirma que esse comportamento foi antecipado no projeto.

**Impacto:** As listagens exibem o status `ATIVO` ou `INATIVO` para cada aluno. Um aluno inativo ainda aparece no histórico de matrículas, mas não pode ser matriculado novamente enquanto inativo.

---

### 4.2 Armazenamento do CPF sem formatação

**Decisão:** O CPF é armazenado como string de 11 dígitos numéricos, sem pontos ou hífen (ex.: `12345678900`).

**Alternativas consideradas:** Armazenar com formatação (`123.456.789-00`) e normalizar antes de cada busca.

**Justificativa:** Guardar sem formatação simplifica comparações via `equals()` e elimina a necessidade de normalização em toda busca. O método `validateCpf()` já verifica que a entrada tem exatamente 11 caracteres numéricos antes de qualquer operação, garantindo que o dado armazenado seja sempre consistente.

**Impacto:** O usuário deve digitar o CPF sem formatação. A exibição nas listagens também é sem formatação, uma decisão que pode ser revisada nas etapas seguintes sem impactar a lógica de negócio.

---

### 4.3 Validação de CPF apenas por formato básico

**Decisão:** O método `validateCpf()` em `Student` verifica apenas comprimento (11 caracteres) e se todos os caracteres são numéricos. Não implementa o algoritmo de verificação dos dígitos verificadores.

**Alternativas consideradas:** Implementar o algoritmo completo de validação dos dois dígitos verificadores.

**Justificativa:** O algoritmo completo aumentaria a robustez do sistema, mas também sua complexidade de implementação e de testes. Para esta etapa, o grupo priorizou a corretude estrutural da arquitetura. A validação de formato é suficiente para garantir que o dado armazenado tenha a forma esperada e que CPFs claramente inválidos sejam rejeitados. O algoritmo completo pode ser adicionado ao método existente sem qualquer alteração na arquitetura.

**Impacto:** CPFs com formato válido mas dígitos verificadores incorretos são aceitos pelo sistema. Isso é uma limitação conhecida e documentada.

---

### 4.4 `totalPrice` calculado e armazenado no construtor de `Enrollment`

**Decisão:** O valor total da matrícula é calculado via `plan.calculateTotalPrice(durationMonths)` no momento da criação do objeto `Enrollment` e armazenado no atributo `totalPrice`. Não é recalculado em nenhum outro momento.

**Alternativas consideradas:** Calcular `totalPrice` dinamicamente a partir do plano sempre que necessário.

**Justificativa:** O cálculo dinâmico tornaria o contrato suscetível a alterações futuras no preço do plano, o que violaria a consistência histórica do sistema, uma academia não pode alterar retroativamente o valor de um contrato já firmado. Ao armazenar no momento da criação, o `Enrollment` torna-se autossuficiente: mesmo que o preço do plano seja atualizado posteriormente, a matrícula existente permanece inalterada.

**Impacto:** `Enrollment.calculateBalance()` opera sobre `totalPrice` estático e a lista de pagamentos dinâmica, garantindo cálculo correto do saldo em qualquer momento.

---

### 4.5 Pagamento inicial como qualquer valor positivo

**Decisão:** A regra de pagamento mínimo inicial na matrícula é satisfeita por qualquer valor maior que zero.

**Alternativas consideradas:** Exigir o pagamento de pelo menos uma mensalidade (equivalente a `pricePerMonth`), ou um percentual fixo do valor total.

**Justificativa:** A especificação exige que a matrícula seja efetivada com um pagamento inicial, mas não define um valor mínimo específico. O grupo interpretou que qualquer valor positivo satisfaz a regra, pois academias reais têm políticas variadas de entrada, algumas aceitam pagamento parcial no ato. Essa decisão simplifica o fluxo de matrícula sem violar nenhuma restrição explícita do enunciado.

**Impacto:** A validação `amount <= 0` é realizada no `FitManager` antes de delegar ao `EnrollmentService`, mantendo a verificação na camada de orquestração, fora do domínio.

---

### 4.6 Instanciação dos menus sob demanda no `MainMenu`

**Decisão:** Os objetos de menu (`StudentMenu`, `PlanMenu`, `EnrollmentMenu`, `ReportsMenu`) são instanciados dentro do `switch` do `MainMenu` a cada vez que o usuário seleciona a opção correspondente.

**Alternativas consideradas:** Instanciar todos os menus no início do programa e mantê-los como atributos do `MainMenu`.

**Justificativa:** A instanciação sob demanda é mais econômica em memória e mais simples de implementar nesta etapa, já que os menus não carregam estado entre chamadas, eles apenas coletam dados e delegam ao `FitManager`. Como `UserInterface` e `FitManager` são passados como parâmetro ao construtor de cada menu, a criação repetida não causa inconsistências. A alternativa de pré-instanciar todos os menus seria adequada caso houvesse estado a preservar entre visitas, o que não ocorre aqui.

**Impacto:** Nenhum impacto funcional observado. A decisão pode ser revisada na etapa seguinte sem alterações em outras camadas.

---

### 4.7 `endDate` calculada no construtor de `Enrollment`

**Decisão:** A data de término da matrícula é calculada como `startDate.plusMonths(durationMonths)` diretamente no construtor de `Enrollment`.

**Alternativas consideradas:** Calcular em um método separado ou delegar ao `EnrollmentService`.

**Justificativa:** A `endDate` depende exclusivamente de dois atributos do próprio objeto (`startDate` e `durationMonths`). Pela regra prática descrita no enunciado se a operação usa apenas dados de um objeto, ela pertence a esse objeto, o cálculo no construtor é a alocação mais coesa. Além disso, o método `plusMonths()` da API `LocalDate` foi projetado exatamente para esse fim e trata corretamente meses de tamanhos variados.

**Impacto:** `endDate` é imutável após a criação da matrícula, o que é o comportamento esperado para um contrato com período definido.

---

### 4.8 Seleção de `PlanType` e `PaymentType` por menu numerado

**Decisão:** Os valores dos enums são apresentados ao usuário como opções numeradas via `showMenu()`, e a escolha é mapeada para o enum correspondente no menu.

**Alternativas consideradas:** Aceitar a entrada como texto e converter para o enum via `Enum.valueOf()`.

**Justificativa:** A abordagem por menu numerado é mais robusta: o usuário só pode escolher entre as opções válidas, eliminando erros de digitação e a necessidade de normalização de texto. A abordagem por texto livre exigiria tratamento de exceções adicionais e seria menos amigável ao usuário. O mesmo padrão foi aplicado tanto para `PlanType` (no `PlanMenu`) quanto para `PaymentType` (no `EnrollmentMenu`), garantindo consistência em todo o sistema.

---

### 4.9 Atomicidade no fluxo de matrícula

**Decisão:** O `EnrollmentService.enroll()` cria o `Enrollment` e o `Payment` inicial na mesma operação. Se a validação de duração falhar, nenhum dos dois objetos é adicionado à coleção.

**Alternativas consideradas:** Criar a matrícula primeiro e registrar o pagamento em uma chamada separada.

**Justificativa:** A criação separada criaria um estado intermediário inválido, uma matrícula sem pagamento, que violaria a regra de negócio de que a matrícula só é efetivada após o registro do pagamento inicial. Ao centralizar ambas as criações no `enroll()`, o serviço garante que ou os dois objetos são criados juntos, ou nenhum deles é adicionado, preservando a consistência da coleção.

---

### 4.10 Verificação de matrícula ativa no `FitManager`, não no `EnrollmentService.enroll()`

**Decisão:** A verificação de que o aluno já possui matrícula ativa (`hasActiveEnrollment()`) é feita no `FitManager.enrollStudent()` antes de delegar ao serviço.

**Alternativas consideradas:** Incluir essa verificação dentro do próprio `EnrollmentService.enroll()`.

**Justificativa:** A verificação envolve cruzar informações entre `Student` e `Enrollment`, dois domínios distintos. Conforme a regra prática do enunciado, operações que precisam coordenar mais de um objeto pertencem ao `FitManager`. Manter o `EnrollmentService` responsável apenas pelas operações internas de matrícula aumenta sua coesão e evita que o serviço precise conhecer contexto externo. O `FitManager` consolida todas as pré-condições antes de delegar.

---

## 5. Regras de Negócio Implementadas

| Regra | Implementação |
|---|---|
| CPF deve ser único no sistema | `StudentService.cpfExists()` → retorna `OperationResult` com `success = false` se duplicado |
| Todos os campos do aluno são obrigatórios | `StudentService.registerStudent()` verifica nulos e strings vazias |
| CPF deve ter 11 dígitos numéricos | `Student.validateCpf()` chamado como método estático em `StudentService` |
| Nome do plano deve ser único | `PlanService.nameExists()` |
| Duração mínima do plano deve ser maior que zero | `PlanService.registerPlan()` |
| Preço por mês deve ser positivo | `PlanService.registerPlan()` e `PlanService.updatePrice()` |
| Alteração de preço não afeta matrículas existentes | `totalPrice` armazenado no construtor de `Enrollment`; `updatePrice()` altera apenas o plano |
| Aluno não pode ter mais de uma matrícula ativa | `FitManager.enrollStudent()` via `EnrollmentService.hasActiveEnrollment()` |
| Duração contratada ≥ duração mínima do plano | `EnrollmentService.enroll()` |
| Matrícula exige pagamento inicial positivo | `FitManager.enrollStudent()` valida `amount > 0` antes de delegar |
| Pagamento não pode ser registrado em matrícula cancelada | `EnrollmentService.registerPayment()` verifica `EnrollmentStatus` |
| Valor de pagamento deve ser positivo | `EnrollmentService.registerPayment()` |
| Aluno com matrícula ativa não pode ser removido | `FitManager.removeStudent()` via `EnrollmentService.hasActiveEnrollment()` |
| Matrícula cancelada não pode ser cancelada novamente | `EnrollmentService.cancel()` verifica o status atual |
| Transição de status é irreversível (`ACTIVE` → `CANCELLED`) | `Enrollment.cancel()` só executa se status for `ACTIVE` |
| `calculateBalance()` positivo = saldo pendente; negativo = crédito | `Enrollment.calculateBalance()` retorna `totalPrice - calculateTotalPaid()` |

**Regras não implementadas ou implementadas parcialmente:**

- **Validação completa do CPF (dígito verificador):** Implementada apenas validação de formato (comprimento e caracteres numéricos). A decisão e justificativa estão na seção 4.3.
- **Edição de aluno:** A opção de edição de cadastro (opção 3 do `StudentMenu`) não foi implementada nesta etapa por limitação de tempo. A estrutura de getters e setters em `Student` está preparada para recebê-la.
- **Resumo financeiro no cancelamento:** O cancelamento altera o status da matrícula corretamente, mas a exibição do resumo financeiro (valor total, total pago, saldo) no momento do cancelamento não foi implementada no menu.
- **Relatórios específicos por filtro:** O `ReportsMenu` apresenta relatórios gerais de alunos, planos e matrículas, mas não inclui as listagens filtradas específicas exigidas ("alunos com matrícula ativa" e "matrículas com saldo pendente").

---

## 6. Dificuldades e Aprendizados

**Organização do grupo e dinâmica de trabalho**

A principal dificuldade organizacional foi a impossibilidade de realizar reuniões frequentes para desenvolvimento conjunto. A rotina de estudos e trabalho de cada integrante limitou os encontros presenciais, de modo que o grupo adotou uma dinâmica de desenvolvimento paralelo e distribuído: cada um avançava em sua camada localmente, tirando dúvidas e alinhando decisões por mensagem ou em encontros pontuais. Essa abordagem funcionou bem para manter o progresso individual, mas exigiu disciplina para garantir que os contratos entre camadas, assinaturas de métodos, tipos de retorno, estrutura das classes de domínio, fossem acordados antes de cada um iniciar sua parte.

A integração das três camadas foi realizada em uma reunião final síncrona, onde o código de cada integrante foi conectado, os testes manuais foram conduzidos em conjunto e os ajustes de compatibilidade foram feitos. Essa reunião também foi o momento em que o repositório no GitHub foi configurado e o código foi subido. A decisão de deixar a organização do repositório para o final foi consciente: o grupo priorizou concluir o desenvolvimento local antes de lidar com a configuração do controle de versão remoto, para evitar que dificuldades com o GitHub atrasassem o desenvolvimento do sistema em si.

Como aprendizado organizacional, o grupo reconhece que estabelecer o repositório no início do projeto, mesmo que com commits simples, teria facilitado o rastreamento do progresso individual e evitado o trabalho concentrado de integração no final. Nas etapas seguintes, a intenção é adotar o fluxo de branches desde o início do desenvolvimento.

**Dificuldades técnicas**

A principal dificuldade técnica foi compreender e aplicar consistentemente a separação em camadas na prática. Entender que os menus não devem conter nenhuma lógica de negócio, e que o `FitManager` não deve armazenar dados, exigiu revisões durante a implementação, pois a tendência natural era resolver tudo no lugar mais próximo ao problema.

A modelagem do fluxo de matrícula foi o ponto mais complexo do projeto: envolver três serviços, quatro classes de domínio e garantir a atomicidade da criação de `Enrollment` e `Payment` exigiu planejamento cuidadoso antes de escrever o código. A dependência entre camadas também gerou momentos de bloqueio, a camada de interface precisava que os métodos do `FitManager` estivessem definidos para poder ser implementada, o que reforçou a importância de acordar as interfaces entre camadas antes de cada um começar a programar.

Como aprendizado principal, o grupo identificou que decisões de projeto tomadas cedo, como onde alocar uma validação ou como nomear um método, têm impacto direto na clareza e na manutenibilidade do código nas etapas seguintes. Documentar essas decisões ao longo do desenvolvimento, e não apenas no final, teria facilitado a escrita deste relatório.

# FitManager — Relatório da Segunda Etapa

## 1. Introdução

Nesta segunda etapa, o sistema FitManager foi refatorado para aplicar conceitos fundamentais de Orientação a Objetos: herança, polimorfismo e interfaces Java. As principais mudanças estruturais foram a conversão da classe `Plan` e da classe `Payment` em superclasses abstratas, com a criação de quatro subclasses especializadas para cada uma, e a transformação de `UserInterface` de classe concreta em uma interface Java, permitindo duas implementações intercambiáveis, `TerminalUI` e `JOptionPaneUI`.

O sistema mantém toda a base funcional construída na Etapa 1 e incorpora as novas hierarquias de forma integrada aos fluxos existentes. A funcionalidade de edição de aluno, que havia ficado pendente na etapa anterior, também foi implementada nesta entrega.

---

## 2. Integrantes e Contribuições

| Integrante | Foco principal |
|---|---|
| Pedro Rodrigues Rocha | Camada de Interface: conversão de `UserInterface` em interface Java, implementação de `TerminalUI` e `JOptionPaneUI`, e atualização do `Main.java` |
| Lucas Dias Squinca | Hierarquia de Planos: abstração de `Plan`, criação das quatro subclasses com regras de desconto e cancelamento, e atualização de `PlanService` e `PlanMenu` |
| Luis Henrique Gonzaga Botelho | Hierarquia de Pagamentos: abstração de `Payment`, criação das quatro subclasses com atributos e regras específicas, e atualização de `EnrollmentService` e `EnrollmentMenu` |

O desenvolvimento desta etapa foi mais progressivo e melhor organizado do que na etapa anterior. O grupo manteve a divisão por responsabilidades, mas com mais pontos de alinhamento ao longo do processo, o que reduziu o retrabalho na integração final e permitiu que os ajustes entre camadas fossem feitos de forma mais incremental.

---

## 3. Diagrama de Classes Atualizado

O diagrama abaixo reflete o sistema conforme implementado. A estrutura geral segue o diagrama original proposto no enunciado, com as adaptações descritas na seção de Decisões de Projeto.

```plantuml
@startuml FitManager - Etapa 2

package "ui" {
  interface UserInterface {
    +showMenu(title: String, options: String[]): int
    +getInput(prompt: String): String
    +showMessage(msg: String): void
    +showError(msg: String): void
  }

  class TerminalUI implements UserInterface {
    -scanner: Scanner
  }

  class JOptionPaneUI implements UserInterface {
  }

  class MainMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +start(): void
  }

  class StudentMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +run(): void
  }

  class PlanMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +run(): void
  }

  class EnrollmentMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +run(): void
  }

  class ReportsMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +run(): void
  }
}

package "application" {
  class FitManager {
    -studentService: StudentService
    -planService: PlanService
    -enrollmentService: EnrollmentService
    +registerStudent(...): OperationResult
    +findStudentByCpf(cpf: String): Student
    +updateStudent(...): OperationResult
    +removeStudent(cpf: String): OperationResult
    +listStudents(): ArrayList<Student>
    +registerPlan(...): OperationResult
    +findPlanByName(name: String): Plan
    +updatePlanPrice(...): OperationResult
    +listPlans(): ArrayList<Plan>
    +enrollStudent(...): OperationResult
    +registerPayment(...): OperationResult
    +cancelEnrollment(code: int): OperationResult
    +findActiveEnrollment(cpf: String): Enrollment
    +listEnrollments(): ArrayList<Enrollment>
  }

  class StudentService {
    -students: ArrayList<Student>
    +registerStudent(...): OperationResult
    +updateStudent(...): OperationResult
    +findByCPF(cpf: String): Student
    +removeStudent(cpf: String): OperationResult
    +listStudents(): ArrayList<Student>
    +cpfExists(cpf: String): boolean
  }

  class PlanService {
    -plans: ArrayList<Plan>
    +registerPlan(...): OperationResult
    +findByName(name: String): Plan
    +updatePrice(...): OperationResult
    +listPlans(): ArrayList<Plan>
    +nameExists(name: String): boolean
  }

  class EnrollmentService {
    -enrollments: ArrayList<Enrollment>
    -{static} nextCode: int
    +enroll(...): OperationResult
    +registerPayment(...): OperationResult
    +cancel(code: int): OperationResult
    +findActiveByStudent(cpf: String): Enrollment
    +findByCode(code: int): Enrollment
    +listEnrollments(): ArrayList<Enrollment>
    +hasActiveEnrollment(cpf: String): boolean
  }

  class OperationResult {
    -success: boolean
    -message: String
    -data: Object
    +isSuccess(): boolean
    +getMessage(): String
    +getData(): Object
  }
}

package "domain" {
  class Student {
    -name: String
    -cpf: String
    -contact: String
    -birthDate: LocalDate
    -active: boolean
    +{static} validateCpf(cpf: String): boolean
    +calculateAge(): int
    +activate(): void
    +deactivate(): void
  }

  abstract class Plan {
    -name: String
    -description: String
    -type: PlanType
    -minDurationMonths: int
    -pricePerMonth: double
    +{abstract} calculateTotalPrice(months: int): double
    +{abstract} getCancellationFee(enrollment: Enrollment): double
    +updatePrice(newPrice: double): void
  }

  class MonthlyPlan extends Plan {
    +calculateTotalPrice(months: int): double
    +getCancellationFee(enrollment: Enrollment): double
  }

  class QuarterlyPlan extends Plan {
    +calculateTotalPrice(months: int): double
    +getCancellationFee(enrollment: Enrollment): double
  }

  class SemiAnnualPlan extends Plan {
    +calculateTotalPrice(months: int): double
    +getCancellationFee(enrollment: Enrollment): double
  }

  class AnnualPlan extends Plan {
    +calculateTotalPrice(months: int): double
    +getCancellationFee(enrollment: Enrollment): double
  }

  class Enrollment {
    -code: int
    -student: Student
    -plan: Plan
    -startDate: LocalDate
    -endDate: LocalDate
    -durationMonths: int
    -totalPrice: double
    -status: EnrollmentStatus
    -payments: ArrayList<Payment>
    +registerPayment(payment: Payment): void
    +calculateTotalPaid(): double
    +calculateBalance(): double
    +cancel(): void
  }

  abstract class Payment {
    -date: LocalDate
    -amount: double
    -type: PaymentType
    -description: String
    +{abstract} getProcessingFee(): double
    +{abstract} getPaymentSummary(): String
  }

  class PixPayment extends Payment {
    -pixKey: String
    +getProcessingFee(): double
    +getPaymentSummary(): String
  }

  class CreditCardPayment extends Payment {
    -installments: int
    -cardLastDigits: String
    +getProcessingFee(): double
    +getPaymentSummary(): String
  }

  class DebitCardPayment extends Payment {
    -cardLastDigits: String
    +getProcessingFee(): double
    +getPaymentSummary(): String
  }

  class CashPayment extends Payment {
    -amountReceived: double
    +getProcessingFee(): double
    +getPaymentSummary(): String
  }

  enum PlanType {
    MONTHLY
    QUARTERLY
    SEMI_ANNUAL
    ANNUAL
  }

  enum PaymentType {
    PIX
    CREDIT_CARD
    DEBIT_CARD
    CASH
  }

  enum EnrollmentStatus {
    ACTIVE
    CANCELLED
  }
}

MainMenu --> UserInterface
MainMenu --> FitManager
StudentMenu --> UserInterface
StudentMenu --> FitManager
PlanMenu --> UserInterface
PlanMenu --> FitManager
EnrollmentMenu --> UserInterface
EnrollmentMenu --> FitManager
ReportsMenu --> UserInterface
ReportsMenu --> FitManager

FitManager *-- StudentService
FitManager *-- PlanService
FitManager *-- EnrollmentService

EnrollmentService --> Enrollment
Enrollment --> Plan
Enrollment "1" *-- "0..*" Payment
Enrollment --> EnrollmentStatus
Plan --> PlanType
Payment --> PaymentType

@enduml
```

---

## 4. Decisões de Projeto

### 4.1 Interface Java em vez de classe abstrata para `UserInterface`

**Decisão:** `UserInterface` foi convertida em uma interface Java pura, implementada por `TerminalUI` e `JOptionPaneUI`.

**Alternativas consideradas:** Converter em classe abstrata com implementações padrão para os métodos comuns.

**Justificativa:** `TerminalUI` e `JOptionPaneUI` não compartilham nenhum estado interno (atributos) nem lógica de implementação, cada uma resolve os quatro métodos do contrato de forma completamente independente. Uma classe abstrata seria adequada se houvesse comportamento comum a reutilizar, o que não é o caso aqui. A interface estabelece um contrato rígido sem acoplar as implementações, e os menus continuam referenciando apenas o tipo `UserInterface`, sem precisar conhecer qual implementação está em uso, o que é exatamente o Princípio Aberto/Fechado: adicionar uma nova implementação de UI no futuro não exige nenhuma alteração nos menus.

**Impacto:** Todos os menus foram mantidos sem alteração em sua lógica interna. A escolha da implementação de `UserInterface` ocorre em um único ponto, na inicialização do sistema em `Main`, o que torna a troca trivial.

---

### 4.2 Manutenção dos enums `PlanType` e `PaymentType`

**Decisão:** Os enums `PlanType` e `PaymentType` foram mantidos no sistema mesmo após a criação das subclasses concretas.

**Alternativas consideradas:** Eliminar os enums, deixando que a própria hierarquia de classes represente o tipo do plano ou pagamento.

**Justificativa:** Os enums cumprem um papel diferente das subclasses: eles atuam como seletores seguros nos Factory Methods de `PlanService` e `EnrollmentService`, permitindo que a camada de interface comunique a escolha do usuário sem precisar conhecer as subclasses concretas. Sem os enums, os menus precisariam importar e instanciar `AnnualPlan`, `PixPayment` e afins diretamente, violando a separação de camadas. Com os enums, o menu passa `PlanType.ANNUAL` ao `FitManager`, que delega ao serviço, que instancia a subclasse correta. Isso mantém a interface desacoplada do domínio e previne erros de digitação ou escolhas inválidas.

**Impacto:** Os Factory Methods em `PlanService` e `EnrollmentService` usam `switch` sobre o enum para instanciar a subclasse correta. Essa estrutura condicional é o ponto que será eliminado em etapas futuras, caso o padrão Factory Method seja formalizado, o que foi documentado como ponto de extensão planejado.

---

### 4.3 Validação de `CashPayment` no `EnrollmentService`, antes da instanciação

**Decisão:** A validação de que o valor recebido em dinheiro (`amountReceived`) não pode ser inferior ao valor cobrado (`amount`) é feita no `EnrollmentService`, antes de o objeto `CashPayment` ser criado.

**Alternativas consideradas:** Realizar a validação no construtor de `CashPayment`, lançando uma exceção caso o valor seja inválido.

**Justificativa:** O padrão estabelecido na Etapa 1 é que os serviços retornam `OperationResult` com `success = false` para operações inválidas, sem lançar exceções para o usuário final. Manter a validação no serviço preserva esse contrato: o menu recebe um `OperationResult` descritivo e exibe a mensagem de erro via `ui.showError()`. Lançar uma exceção no construtor quebraria o fluxo esperado pelos menus e introduziria um tratamento de erro inconsistente com o restante do sistema.

**Impacto:** `CashPayment` é instanciado apenas quando os dados já foram validados, garantindo que nenhum objeto em estado inválido seja adicionado ao histórico de pagamentos.

---

### 4.4 `getPaymentSummary()` como contrato de domínio, separado de `toString()`

**Decisão:** O método abstrato `getPaymentSummary()` foi definido na superclasse `Payment` e implementado por cada subclasse para formatar a exibição dos dados financeiros ao usuário. O `toString()` foi preservado para uso técnico.

**Alternativas consideradas:** Sobrescrever `toString()` em cada subclasse para exibir as informações ao usuário.

**Justificativa:** `toString()` tem uma semântica definida em Java: é usado para representação textual de objetos em contextos técnicos, como depuração, logs e concatenação implícita. Utilizá-lo para formatação de saída ao usuário mistura responsabilidades. O método `getPaymentSummary()` expressa uma regra de negócio explícita — "como este pagamento deve ser apresentado no contexto da academia" — e é invocado intencionalmente, nunca de forma implícita. Essa separação torna o código mais legível e evita comportamentos inesperados caso `toString()` seja chamado em outros contextos.

**Impacto:** O `EnrollmentMenu` itera sobre a lista de pagamentos chamando `p.getPaymentSummary()` para cada um. Cada subclasse retorna sua string formatada com os atributos específicos — `PixPayment` inclui a chave PIX, `CreditCardPayment` inclui parcelas e últimos dígitos, `CashPayment` inclui o troco — sem que o menu precise saber com qual subclasse está lidando.

---

### 4.5 Encapsulamento das regras de desconto e cancelamento nas subclasses de `Plan`

**Decisão:** As regras de cálculo de preço total e de taxa de cancelamento foram encapsuladas nos métodos `calculateTotalPrice()` e `getCancellationFee()` de cada subclasse, sem lógica condicional baseada em tipo na superclasse ou nos serviços.

**Alternativas consideradas:** Manter um único método em `Plan` com estrutura `if/else` ou `switch` sobre `PlanType`.

**Justificativa:** A estrutura condicional por tipo é exatamente o problema que o polimorfismo resolve. Cada subclasse conhece suas próprias regras e as aplica sem precisar consultar o tipo do objeto. Isso elimina a necessidade de alterar a superclasse ou os serviços quando uma nova regra de negócio for introduzida em um tipo específico de plano — basta modificar a subclasse correspondente. O `EnrollmentService` chama `enrollment.getPlan().getCancellationFee(enrollment)` sem precisar saber se o plano é anual, trimestral ou mensal.

**Impacto:** O fluxo de cancelamento ficou significativamente simplificado: o serviço delega o cálculo da taxa ao plano, que responde de acordo com suas próprias regras, e o resultado é incorporado ao resumo financeiro exibido ao usuário.

---

### 4.6 Correção do vazamento de saída nos menus (`System.out` → `ui.showMessage()`)

**Decisão:** Todos os pontos onde os menus usavam `System.out.printf` ou `System.out.println` diretamente foram refatorados para usar `ui.showMessage()`.

**Contexto:** Na Etapa 1, havia chamadas diretas a `System.out` em alguns métodos dos menus, identificadas como violação da separação de camadas. Durante a integração da implementação `JOptionPaneUI` nesta etapa, o problema se tornou evidente: as listagens continuavam sendo impressas no terminal mesmo quando o modo gráfico estava ativo.

**Solução adotada:** Os métodos de listagem foram refatorados para montar o conteúdo em memória usando `StringBuilder` e `String.format()`, e então entregar o texto completo a `ui.showMessage()`. Isso garante que a exibição seja tratada pela implementação de `UserInterface` ativa, independentemente de qual for.

**Impacto:** O sistema agora funciona de forma completamente consistente em ambas as implementações de interface, sem nenhum vazamento de saída para o console quando `JOptionPaneUI` está em uso.

---

## 5. Como o Polimorfismo Simplificou o Código

O impacto do polimorfismo ficou evidente em dois pontos centrais do sistema:

**Cancelamento de matrícula (`EnrollmentService`):** Em vez de usar estruturas condicionais para verificar o tipo do plano e calcular a taxa de cancelamento, o serviço executa diretamente `enrollment.getPlan().getCancellationFee(enrollment)`. A decisão de aplicar multa de 20% (plano anual, cancelamento antes da metade do período) ou retornar zero (demais planos) fica completamente encapsulada nas subclasses. O serviço não precisa conhecer as regras de cada tipo.

**Exibição do histórico de pagamentos (`EnrollmentMenu`):** O menu não precisa verificar se o pagamento é PIX (exibe chave), cartão de crédito (exibe parcelas e últimos dígitos) ou dinheiro (exibe troco). O laço de repetição invoca `p.getPaymentSummary()` para cada pagamento, e cada subclasse de `Payment` devolve sua string formatada de acordo com seus atributos específicos.

---

## 6. Regras de Negócio Implementadas

### Hierarquia de Planos

| Subclasse | Desconto | Taxa de Cancelamento |
|---|---|---|
| `MonthlyPlan` | Nenhum | 0% |
| `QuarterlyPlan` | 5% sobre o valor total bruto, se duração ≥ mínimo do plano | 0% |
| `SemiAnnualPlan` | 10% sobre o valor total bruto, se duração ≥ mínimo do plano | 0% |
| `AnnualPlan` | 15% sobre o valor total bruto, se duração ≥ mínimo do plano | 20% do valor total, se cancelamento ocorrer antes da metade do período contratado |

O cálculo da taxa de cancelamento do `AnnualPlan` utiliza `ChronoUnit.MONTHS` da API `LocalDate` para determinar o número de meses decorridos desde o início da matrícula.

### Hierarquia de Pagamentos

| Subclasse | Atributos específicos | Taxa de processamento | Comportamento adicional |
|---|---|---|---|
| `PixPayment` | `pixKey` | 0% | Exibe a chave PIX no resumo |
| `CreditCardPayment` | `installments`, `cardLastDigits` | Simulada (positiva) | Exibe parcelas e últimos dígitos no resumo |
| `DebitCardPayment` | `cardLastDigits` | Simulada (positiva) | Exibe últimos dígitos no resumo |
| `CashPayment` | `amountReceived` | 0% | Valida que valor recebido ≥ valor cobrado; exibe troco no resumo |

### Funcionalidade recuperada da Etapa 1

- **Edição de aluno:** Implementada nesta etapa. O `StudentMenu` permite atualizar nome e contato de um aluno já cadastrado, com a operação delegada ao `StudentService` via `FitManager`.

---

## 7. Dificuldades e Aprendizados

**Integração das duas implementações de `UserInterface`**

A principal dificuldade técnica desta etapa foi a integração da `JOptionPaneUI`. O problema do vazamento de saída para o console — descrito na seção 4.6 — só ficou evidente durante os testes com a interface gráfica ativa, pois no modo terminal o comportamento parecia correto. Isso reforçou na prática a importância de testar o sistema com todas as implementações possíveis de uma interface, e não apenas com a mais familiar.

A refatoração para usar `StringBuilder` e `ui.showMessage()` nas listagens foi trabalhosa por envolver vários métodos distribuídos nos menus, mas o resultado final ficou mais limpo e coeso do que antes.

**Modelagem das hierarquias**

Definir onde alocar a lógica de desconto — se na superclasse, nas subclasses ou no serviço — exigiu discussão. A tentação inicial era manter um `switch` no `PlanService`, o que teria preservado a estrutura da Etapa 1 com menor esforço. A decisão de mover a lógica para as subclasses foi mais trabalhosa de implementar, mas tornou o cancelamento e o cálculo de preço muito mais simples nos serviços.

**Organização e evolução em relação à Etapa 1**

O desenvolvimento desta etapa foi mais progressivo e melhor organizado. O grupo manteve a divisão de responsabilidades por camada, mas com mais pontos de alinhamento ao longo do processo — o que reduziu o retrabalho na integração e permitiu que incompatibilidades entre camadas fossem identificadas e corrigidas mais cedo. A experiência da Etapa 1 mostrou que alinhar os contratos entre camadas antes de implementar é mais eficiente do que descobrir incompatibilidades na integração final.

# FitManager — Relatório da Terceira Etapa

## 1. Introdução

Nesta terceira etapa, o sistema FitManager foi aprimorado com quatro frentes de evolução: tipagem genérica via Generics, uma hierarquia de exceções de domínio, persistência de dados em arquivos binários e geração de relatório financeiro mensal. As mudanças reforçam a robustez da arquitetura e garantem que os dados sobrevivam ao encerramento do sistema.

O sistema mantém toda a base funcional construída nas etapas anteriores. As correções de comportamento — desconto de planos, taxa de cartão de débito, multa de cancelamento e exibição do histórico de pagamentos — foram integradas sem alterações na estrutura de camadas.

---

## 2. Integrantes e Contribuições

| Integrante | Foco principal |
|---|---|
| Lucas Dias Squinca | Hierarquia de Exceções: Criação do pacote de exceções customizadas e a consequente blindagem rica das classes de domínio `(Student, Plan, Payment, Enrollment)` contra dados inválidos. |
| Pedro Rodrigues Rocha | Generics e Manutenção: Parametrização genérica da classe `OperationResult<T>` e implementação das correções de regras de negócio e refatorações herdadas da Etapa 2. |
| Luis Henrique Gonzaga Botelho | Persistência e Relatórios: Criação da abstração genérica `Repository<T>`, implementação da persistência automatizada em arquivos binários (.dat) e desenvolvimento do novo Relatório Financeiro Mensal. |

A divisão por responsabilidades foi mantida. As decisões de projeto foram alinhadas previamente em grupo, especialmente a interface entre `Repository<T>` e os serviços e a estrutura do pacote de exceções, para evitar retrabalho na integração.

---

## 3. Diagrama de Classes Atualizado

O diagrama abaixo reflete o sistema conforme implementado nesta etapa, destacando as adições e modificações em relação à Etapa 2.

```plantuml
@startuml

package "ui" {
  interface UserInterface {
    +showMenu(title: String, options: String[]): int
    +getInput(prompt: String): String
    +showMessage(msg: String): void
    +showError(msg: String): void
  }
  
  class TerminalUI implements UserInterface {
    -scanner: Scanner
  }
  
  class JOptionPaneUI implements UserInterface {
  }
  
  class MainMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +start(): void
  }
  
  class StudentMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +run(): void
  }
  
  class PlanMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +run(): void
  }
  
  class EnrollmentMenu {
    -ui: UserInterface
    -fitManager: FitManager
    -processPaymentDetails(): PaymentContext
    +run(): void
  }
  
  class ReportsMenu {
    -ui: UserInterface
    -fitManager: FitManager
    +run(): void
    +generateFinancialReport(month: int, year: int): void
  }
}

package "application" {
  abstract class Repository<T> {
    #elements: ArrayList<T>
    -type: Class<T>
    +add(item: T): void
    +listAll(): ArrayList<T>
    +save(filename: String): void
    +load(filename: String): void
  }
  
  class FitManager {
    -studentService: StudentService
    -planService: PlanService
    -enrollmentService: EnrollmentService
    +registerStudent(...): OperationResult<Student>
    +findStudentByCpf(cpf: String): Student
    +updateStudent(...): OperationResult<Student>
    +removeStudent(cpf: String): OperationResult<Student>
    +listStudents(): ArrayList<Student>
    +registerPlan(...): OperationResult<Plan>
    +findPlanByName(name: String): Plan
    +updatePlanPrice(...): OperationResult<Plan>
    +listPlans(): ArrayList<Plan>
    +enrollStudent(...): OperationResult<Enrollment>
    +registerPayment(...): OperationResult<Enrollment>
    +cancelEnrollment(code: int): OperationResult<Enrollment>
    +findActiveEnrollment(cpf: String): Enrollment
    +listEnrollments(): ArrayList<Enrollment>
    +loadAllData(): void
    +saveAllData(): void
  }
  
  class StudentService extends Repository {
    +registerStudent(...): OperationResult<Student>
    +updateStudent(...): OperationResult<Student>
    +findByCPF(cpf: String): Student
    +removeStudent(cpf: String): OperationResult<Student>
    +listStudents(): ArrayList<Student>
    +cpfExists(cpf: String): boolean
  }
  
  class PlanService extends Repository {
    +registerPlan(...): OperationResult<Plan>
    +findByName(name: String): Plan
    +updatePrice(...): OperationResult<Plan>
    +listPlans(): ArrayList<Plan>
    +nameExists(name: String): boolean
  }
  
  class EnrollmentService extends Repository {
    -{static} nextCode: int
    +enroll(...): OperationResult<Enrollment>
    +registerPayment(...): OperationResult<Enrollment>
    +cancel(code: int): OperationResult<Enrollment>
    +findActiveByStudent(cpf: String): Enrollment
    +findByCode(code: int): Enrollment
    +listEnrollments(): ArrayList<Enrollment>
    +hasActiveEnrollment(cpf: String): boolean
  }
  
  class OperationResult<T> {
    -success: boolean
    -message: String
    -data: T
    +isSuccess(): boolean
    +getMessage(): String
    +getData(): T
  }
  
  class PaymentContext {
    +type: PaymentType
    +amount: double
    +pixKey: String
    +cardLastDigits: String
    +installments: int
    +amountReceived: double
  }
}

package "domain" {
  interface Serializable
  
  class Student implements Serializable {
    -name: String
    -cpf: String
    -contact: String
    -birthDate: LocalDate
    -active: boolean
    +{static} validateCpf(cpf: String): boolean
    +calculateAge(): int
    +activate(): void
    +deactivate(): void
  }
  
  abstract class Plan implements Serializable {
    -name: String
    -description: String
    -type: PlanType
    -minDurationMonths: int
    -pricePerMonth: double
    +{abstract} calculateTotalPrice(months: int): double
    +{abstract} getCancellationFee(enrollment: Enrollment): double
    +updatePrice(newPrice: double): void
  }
  
  class MonthlyPlan extends Plan { }
  class QuarterlyPlan extends Plan { }
  class SemiAnnualPlan extends Plan { }
  class AnnualPlan extends Plan { }
  
  class Enrollment implements Serializable {
    -code: int
    -student: Student
    -plan: Plan
    -startDate: LocalDate
    -endDate: LocalDate
    -durationMonths: int
    -totalPrice: double
    -status: EnrollmentStatus
    -payments: ArrayList<Payment>
    +registerPayment(payment: Payment): void
    +calculateTotalPaid(): double
    +calculateBalance(): double
    +getPayments(): ArrayList<Payment>
    +cancel(): void
  }
  
  abstract class Payment implements Serializable {
    -date: LocalDate
    -amount: double
    -type: PaymentType
    -description: String
    +{abstract} getProcessingFee(): double
    +{abstract} getPaymentSummary(): String
  }
  
  class PixPayment extends Payment { }
  class CreditCardPayment extends Payment { }
  class DebitCardPayment extends Payment { }
  class CashPayment extends Payment { }
  
enum PlanType {
  MONTHLY
  QUARTERLY
  SEMI_ANNUAL
  ANNUAL
}

enum PaymentType {
  PIX
  CREDIT_CARD
  DEBIT_CARD
  CASH
}

enum EnrollmentStatus {
  ACTIVE
  CANCELLED
}
}

package "exceptions" {
  class FitManagerException extends RuntimeException {
    +FitManagerException(message: String)
    +FitManagerException(message: String, cause: Throwable)
  }
  
  class ValidationException extends FitManagerException {
    +ValidationException(message: String)
  }
  
  class PersistenceException extends FitManagerException {
    +PersistenceException(message: String, cause: Throwable)
  }
}

' --- RELACIONAMENTOS ---

MainMenu --> UserInterface
MainMenu --> FitManager
StudentMenu --> UserInterface
StudentMenu --> FitManager
PlanMenu --> UserInterface
PlanMenu --> FitManager
EnrollmentMenu --> UserInterface
EnrollmentMenu --> FitManager
ReportsMenu --> UserInterface
ReportsMenu --> FitManager

EnrollmentMenu ..> PaymentContext : "Usa (DTO)"

FitManager *-- StudentService
FitManager *-- PlanService
FitManager *-- EnrollmentService

StudentService --> Student
PlanService --> Plan
EnrollmentService --> Enrollment
Enrollment --> Plan
Enrollment "1" *-- "0..*" Payment
Enrollment --> EnrollmentStatus
Plan --> PlanType
Payment --> PaymentType

Repository ..> PersistenceException
Student ..> ValidationException
Plan ..> ValidationException
Payment ..> ValidationException
Enrollment ..> ValidationException

@enduml
```

---

## 4. Decisões de Projeto

### 4.1 Repositório genérico como classe abstrata, não interface

**Decisão:** `Repository<T>` foi implementado como classe abstrata com `ArrayList<T>` como estrutura interna e os métodos `save()` / `load()` já implementados.

**Alternativas consideradas:** Criar uma interface `Repository<T>` e deixar cada serviço implementar sua própria lógica de armazenamento e persistência.

**Justificativa:** Os três serviços compartilham exatamente o mesmo comportamento de armazenamento em lista e de serialização binária. Delegar essa implementação para cada subclasse causaria triplicação de código sem nenhum ganho de flexibilidade, pois o mecanismo de persistência é uniforme em todo o sistema. A classe abstrata é o padrão correto quando há comportamento concreto reutilizável: as subclasses herdam a lógica comum e sobrescrevem apenas o que é específico de cada domínio.

**Impacto:** `StudentService`, `PlanService` e `EnrollmentService` passaram a estender `Repository<T>`, eliminando os atributos de lista duplicados e centralizando `save()` e `load()` em um único ponto de manutenção.

---

### 4.2 Tratamento de type erasure via `Class<T>` no construtor

**Decisão:** O construtor de `Repository<T>` recebe o parâmetro `Class<T> type`, que é armazenado como atributo e utilizado em `type.cast()` durante a leitura dos arquivos.

**Alternativas consideradas:** Usar `@SuppressWarnings("unchecked")` e realizar casts sem verificação de tipo.

**Justificativa:** Em Java, o tipo genérico `T` é apagado em tempo de execução (type erasure), o que impede a verificação de tipo ao desserializar objetos de arquivos binários. O uso de `@SuppressWarnings("unchecked")` suprime o aviso do compilador sem resolver o problema: um arquivo corrompido ou com dado de tipo errado causaria um `ClassCastException` em tempo de execução. Receber `Class<T>` no construtor permite que o repositório realize o cast via `type.cast()`, que lança `ClassCastException` de forma controlada e pode ser tratada dentro do `PersistenceException`. A abordagem é a mesma utilizada por frameworks como o Hibernate para instanciação segura de entidades.

**Impacto:** A assinatura dos construtores dos serviços passou a incluir a classe do tipo gerenciado — `new StudentService(Student.class)` — o que é instanciado em `FitManager`.

---

### 4.3 `OperationResult<T>` parametrizado

**Decisão:** `OperationResult` foi convertido em `OperationResult<T>`, parametrizando o atributo `data` com o tipo real do objeto processado.

**Alternativas consideradas:** Manter `data` como `Object` e realizar casts no ponto de uso.

**Justificativa:** O cast de `Object` nos menus é frágil: o compilador não detecta incompatibilidades de tipo, e um cast errado só manifesta erro em tempo de execução. Com `OperationResult<Student>`, o compilador garante que o dado extraído via `getData()` seja um `Student`. Isso também documenta a intenção do método de forma mais precisa: `registerStudent()` retorna `OperationResult<Student>`, não um `OperationResult` genérico cujo conteúdo precisa ser inspecionado ou documentado separadamente.

**Impacto:** Todas as assinaturas de método nos serviços e no `FitManager` foram atualizadas com o tipo parametrizado correto. Os menus que antes faziam cast explícito passaram a usar `getData()` diretamente com segurança de tipo.

---

### 4.4 Hierarquia de exceções com dois ramos distintos

**Decisão:** O pacote `exception` foi estruturado com `FitManagerException` como base, `ValidationException` para violações de regra de negócio e `PersistenceException` para falhas de infraestrutura (I/O de arquivos).

**Alternativas consideradas:** Usar `IllegalArgumentException` e `IOException` diretamente, sem hierarquia customizada.

**Justificativa:** Exceções da JDK não carregam o contexto do domínio: um `IllegalArgumentException` lançado por `Student` não diz nada sobre o que é inválido nem em que contexto. A hierarquia customizada permite que a camada de serviço capture `ValidationException` e `PersistenceException` por nome, sem depender de mensagens de texto para distinguir falhas de tipos diferentes. A separação entre os dois ramos reflete a separação arquitetural: `ValidationException` vem do domínio e indica dado inválido; `PersistenceException` vem da infraestrutura e indica falha de I/O. A camada de interface trata os dois de formas diferentes — `ValidationException` resulta em mensagem de erro ao usuário; `PersistenceException` pode resultar em log ou encerramento controlado.

**Impacto:** `FitManagerException` estende `RuntimeException`, o que permite que o domínio a lance sem obrigar todos os chamadores a declará-la com `throws`. Os serviços capturam as exceções nos blocos `try-catch` e as convertem em `OperationResult` com `success = false`.

---

### 4.5 Blindagem do domínio com validação nos construtores

**Decisão:** As classes `Plan`, `Student`, `Payment` e `Enrollment` passaram a lançar `ValidationException` diretamente em seus construtores e setters quando recebem dados inválidos.

**Alternativas consideradas:** Manter toda a validação na camada de serviço, antes da instanciação.

**Justificativa:** O modelo anêmico — classe de domínio sem lógica, com validação espalhada nos serviços — cria uma dependência: a consistência de um objeto depende do serviço que o instanciou ter feito as verificações corretas. Se um segundo serviço ou ponto de criação for adicionado no futuro, a validação precisa ser duplicada. Com a validação no construtor, o objeto é sempre válido ao existir: é impossível criar um `Plan` com preço negativo, independentemente de qual serviço o instanciou. Essa abordagem segue o princípio de invariantes de domínio e torna o código mais fácil de raciocinar.

**Impacto:** Os serviços ainda realizam validações de unicidade e regras que envolvem múltiplos objetos (ex.: CPF duplicado), pois essas não podem ser feitas pelo domínio isolado. A validação do domínio agrega, não substitui, a validação do serviço.

---

### 4.6 Serialização binária como mecanismo de persistência

**Decisão:** A persistência foi implementada com `ObjectOutputStream` e `ObjectInputStream`, serializando cada coleção completa em um arquivo `.dat`.

**Alternativas consideradas:** Persistência em texto simples (CSV ou JSON manual) ou uso de banco de dados embarcado (H2).

**Justificativa:** A serialização binária é a forma mais direta de persistir grafos de objetos Java sem dependências externas: não exige parsing, mapeamento entre campos de texto e atributos, nem biblioteca adicional. Para um sistema acadêmico sem requisito de interoperabilidade externa com outros sistemas, a serialização atende completamente. O CSV exigiria serialização manual de cada campo de cada subclasse de `Payment` e `Plan`, o que seria mais propenso a erros e mais difícil de manter à medida que novas subclasses fossem adicionadas.

**Impacto:** Os arquivos `.dat` são opacos (não legíveis em texto), o que é aceitável para o contexto do sistema. Uma migração futura para JSON ou banco de dados seria feita apenas nos métodos `save()` e `load()` de `Repository<T>`, sem alteração nos serviços ou no domínio.

---

### 4.7 Orquestração da carga e salvamento no `FitManager`

**Decisão:** Os métodos `loadAllData()` e `saveAllData()` foram implementados no `FitManager`, que controla a ordem de execução: alunos e planos são carregados antes das matrículas.

**Alternativas consideradas:** Deixar cada serviço carregar seus próprios dados de forma independente, ou centralizar no `MainMenu`.

**Justificativa:** A ordem de carregamento importa: `Enrollment` mantém referências a objetos `Student` e `Plan`. Se as matrículas forem desserializadas antes dos alunos e planos, as referências internas ainda são resolvidas pelo mecanismo de serialização Java (que preserva o grafo de objetos), mas a coleção de serviços estaria vazia, impossibilitando buscas por CPF ou nome. Centralizar no `FitManager` segue o mesmo princípio das etapas anteriores: operações que coordenam múltiplos serviços pertencem ao orquestrador. Colocar no `MainMenu` violaria a separação de camadas — a interface não deve conhecer dependências entre serviços.

**Impacto:** O `MainMenu` chama apenas `fitManager.loadAllData()` na inicialização e `fitManager.saveAllData()` na saída, sem conhecimento da ordem ou dos arquivos envolvidos.

---

### 4.8 `PaymentContext` como objeto de transferência no `EnrollmentMenu`

**Decisão:** A duplicação de código entre os fluxos de matrícula e registro de pagamento no `EnrollmentMenu` foi eliminada com a extração do método `processPaymentDetails()`, que retorna um objeto `PaymentContext` com todos os dados coletados do usuário.

**Alternativas consideradas:** Passar os dados coletados como múltiplos parâmetros ou como um `Map<String, Object>`.

**Justificativa:** A passagem de múltiplos parâmetros torna a assinatura do método frágil: qualquer novo dado exige alteração na assinatura e em todos os pontos de chamada. O `Map<String, Object>` perde a tipagem e exige casts nos pontos de uso. O `PaymentContext` é um objeto de transferência simples (DTO) que agrupa os dados com tipos corretos, tornando a intenção explícita e o código do menu mais legível. O padrão é amplamente utilizado em arquiteturas Java para transferência de dados entre camadas sem acoplamento excessivo.

**Impacto:** O `EnrollmentMenu` ficou significativamente mais conciso. A lógica de coleta de dados financeiros existe em um único método, facilitando manutenção e futuras extensões (como a adição de um novo método de pagamento).

---

### 4.9 Relatório financeiro com agrupamento por `instanceof`

**Decisão:** O `generateFinancialReport()` no `ReportsMenu` filtra pagamentos por mês e ano, e usa `instanceof` para categorizar e somar a receita por tipo de pagamento.

**Alternativas consideradas:** Usar o enum `PaymentType` já presente nos objetos `Payment` para o agrupamento.

**Justificativa:** O enum `PaymentType` seria a alternativa mais simples, mas não acessa atributos específicos das subclasses (como taxa de processamento real calculada por `getProcessingFee()`). O `instanceof` permite tanto a categorização quanto o downcast seguro para acessar comportamentos polimórficos de cada subclasse. Embora o uso extensivo de `instanceof` seja geralmente um sinal de design a ser revisto, no contexto de um relatório de saída — que é por natureza uma operação de projeção sobre um conjunto heterogêneo de objetos — ele é a abordagem mais direta e inteligível.

**Impacto:** O relatório exibe o total arrecadado separado por forma de pagamento (PIX, crédito, débito e dinheiro) e o total geral do mês, com as taxas de processamento somadas ao valor líquido recebido.

---

## 5. Correções Implementadas

As correções abaixo foram identificadas na revisão do sistema das etapas anteriores e integradas nesta entrega.

### 5.1 Desconto dos planos com duração mínima

O desconto das subclasses `QuarterlyPlan`, `SemiAnnualPlan` e `AnnualPlan` agora é aplicado apenas quando a duração contratada pelo aluno atinge ou supera a duração mínima do plano (`durationMonths >= minDurationMonths`). Nas etapas anteriores, o desconto era aplicado independentemente da duração, o que violava a regra de negócio especificada.

### 5.2 Taxa de processamento do cartão de débito

A taxa de processamento de `DebitCardPayment` foi corrigida para o valor especificado. O método `calculateBalance()` de `Enrollment` passou a somar as taxas de processamento de todos os pagamentos ao valor total da matrícula, refletindo o custo real incorrido pelo aluno.

### 5.3 Multa de cancelamento aplicada ao saldo

O método `cancel()` em `EnrollmentService` passou a invocar `enrollment.getPlan().getCancellationFee(enrollment)` e somar o resultado ao `totalPrice` da matrícula no momento do cancelamento. Antes desta correção, a multa era calculada mas não incorporada ao saldo devedor.

### 5.4 Exibição do histórico de pagamentos

A tela de consulta de matrícula ativa no `EnrollmentMenu` passou a exibir a lista completa de pagamentos do aluno, utilizando `getPaymentSummary()` de forma efetiva para cada entrada. O método `getPayments()` foi adicionado a `Enrollment` para expor o histórico à camada de interface sem quebrar o encapsulamento.

---

## 6. Regras de Negócio Implementadas

### Persistência

| Regra | Implementação |
|---|---|
| Dados persistem entre execuções | `Repository<T>.save()` / `load()` com serialização binária em `.dat` |
| Ordem de carga respeita dependências | `FitManager.loadAllData()` carrega alunos e planos antes de matrículas |
| Salvamento automático na saída | `MainMenu` chama `saveAllData()` na opção 5 (Sair) |
| Carga automática na inicialização | `MainMenu.start()` chama `loadAllData()` antes de exibir o menu principal |

### Hierarquia de Planos (corrigida)

| Subclasse | Desconto | Condição para desconto | Taxa de cancelamento |
|---|---|---|---|
| `MonthlyPlan` | Nenhum | — | 0% |
| `QuarterlyPlan` | 5% sobre valor bruto total | `durationMonths >= minDurationMonths` | 0% |
| `SemiAnnualPlan` | 10% sobre valor bruto total | `durationMonths >= minDurationMonths` | 0% |
| `AnnualPlan` | 15% sobre valor bruto total | `durationMonths >= minDurationMonths` | 20% do total, se cancelamento ocorrer antes da metade do período |

### Relatório Financeiro

| Campo | Origem |
|---|---|
| Total por forma de pagamento | Soma de `payment.getAmount()` por categoria via `instanceof` |
| Taxas de processamento | Soma de `payment.getProcessingFee()` por categoria |
| Total geral do mês | Soma de todos os pagamentos do mês/ano filtrados |

---

## 7. Dificuldades e Aprendizados

**Generics e type erasure**

A maior dificuldade técnica desta etapa foi o tratamento do type erasure no repositório genérico. A tentativa inicial de fazer o cast diretamente com `(T)` gerava aviso de unchecked cast — correto por parte do compilador, pois o tipo `T` não existe em tempo de execução. Compreender que a solução exigia passar `Class<T>` explicitamente no construtor, e que frameworks Java utilizam esse mesmo padrão, foi um aprendizado concreto sobre os limites e contornos do sistema de tipos da linguagem.

**Serialização e consistência do grafo de objetos**

A serialização binária do Java preserva automaticamente referências compartilhadas: se dois objetos `Enrollment` referenciam o mesmo objeto `Plan`, o arquivo serializado armazena o `Plan` uma vez e as duas referências apontam para o mesmo objeto ao desserializar. Esse comportamento é correto para o sistema, mas exigiu atenção ao testar a carga: modificar um plano após carregar os dados afeta todas as matrículas vinculadas a ele, o que é o comportamento esperado e consistente com as etapas anteriores.

**Integração das correções sem regressão**

Aplicar as quatro correções da seção 5 sem introduzir regressões nos fluxos existentes exigiu reteste manual completo dos cenários de matrícula, pagamento e cancelamento. A experiência reforçou a importância de testes automatizados, mesmo que básicos — a ausência de testes unitários tornou cada correção um risco de quebra em outra parte do sistema.

**Organização e evolução em relação às etapas anteriores**

O grupo manteve a dinâmica de alinhar contratos entre camadas antes de implementar, o que reduziu o retrabalho na integração. A separação entre as correções de comportamento e as novas funcionalidades foi tratada de forma explícita desde o início do desenvolvimento, evitando que mudanças de correção se misturassem com mudanças arquiteturais e tornassem a revisão do código mais difícil.