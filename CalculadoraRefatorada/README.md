# Calculadora — Refatoração

Este diretório contém a versão refatorada da Calculadora (`src/Calculadora.java`).
As versões originais, usadas como "antes" na comparação, continuam na raiz do
repositório: `lista.java` (versão mais completa, com histórico e loop) e
`Main.java`/`Calculadora.java` (versões anteriores, sem loop/histórico).

## 1. Problemas identificados na versão original

1. **Toda operação nova exige alterar o mesmo `switch` no fluxo principal.**
   Adicionar `%`, `max`, `min` etc. significa editar `main()`, misturando
   leitura de entrada, validação e cálculo no mesmo bloco.

2. **Responsabilidades misturadas em um único método.** `main()` faz leitura
   de I/O, parsing de número, escolha de operação, cálculo, formatação de
   saída e gravação de histórico — tudo junto. Isso dificulta testar ou
   reaproveitar qualquer parte isoladamente.

3. **Comportamento inconsistente entre operações.** Em `lista.java`, apenas o
   `case "+"` grava no histórico (`historicoDeOperacoes.add(...)`); as
   demais operações (`-`, `*`, `/`, `pow`) são executadas mas nunca aparecem
   no histórico. Esse tipo de inconsistência é sintoma direto de lógica
   duplicada por `case`: é fácil esquecer de repetir um comportamento em
   todos os ramos.

4. **Validação de regra de negócio (divisão por zero) misturada com
   apresentação.** A checagem de `n2 == "0"` e a decisão de formatação
   (arredondar quando a divisão é exata) ficam dentro do próprio `case "/"`
   do fluxo principal, em vez de pertencerem à operação de divisão.

## 2. Nova organização

- **`enum Operacao`** — passa a ser o único responsável por *conhecer* as
  operações disponíveis: símbolo (`+`, `pow`, `%`...), descrição para o menu
  e a fórmula de cálculo (`DoubleBinaryOperator`). Cada constante do enum é
  autocontida — inclusive a validação de divisão por zero mora dentro da
  própria operação (`DIVISAO`, `RESTO`), não no fluxo principal.
- **`Operacao.porSimbolo(String)`** — substitui o `switch`. Percorre os
  valores do enum e localiza a operação pelo símbolo digitado. Se nada for
  encontrado, retorna `null`, e o fluxo principal trata isso como operação
  inválida — um único ponto de decisão, não um bloco por operação.
- **`Operacao.menu()`** — gera a lista de operações exibida ao usuário a
  partir do próprio enum, então o menu nunca fica desatualizado em relação
  às operações realmente suportadas.
- **Método `main()`** — passa a ser só orquestração: lê comando, decide entre
  sair / mostrar histórico / executar operação, delegando os detalhes para
  `executarOperacao`, `exibirHistorico` e para o `enum Operacao`.
- **`executarOperacao`** — cuida da leitura dos números, busca a operação,
  executa (`try/catch` para `ArithmeticException`, usada para sinalizar
  divisão/resto por zero) e grava no histórico de forma padronizada — para
  **qualquer** operação, corrigindo a inconsistência apontada no item 3.
- **`historico`** — continua sendo uma simples `List<String>`, sem exagero:
  o problema não exigia uma classe própria só para isso.

## 3. Antes e depois — exemplo com a divisão

**Antes** (`lista.java`), a lógica de divisão está dentro do `switch`, no
fluxo principal, misturada com a decisão de formatação:

```java
case "/" -> {
    if ("0".equals(n2))
        IO.println("Divisão por zero é inválida");
    else {
        if (num1 % num2 == 0)
            IO.println(Math.round(num1 / num2));
        else
            IO.println(num1 / num2);
    }
}
```

**Depois**, a regra de negócio da divisão (incluindo a validação) fica
dentro da própria operação; o fluxo principal não sabe nem precisa saber
que "divisão por zero" é um caso especial de "divisão":

```java
DIVISAO("/", "Divisão", (a, b) -> {
    if (b == 0) throw new ArithmeticException("Divisão por zero é inválida");
    return a / b;
}),
```

O fluxo principal trata **qualquer** operação da mesma forma
(`operacao.aplicar(num1, num2)` dentro de um único `try/catch`), então a
divisão deixou de ser um caso especial no `main`.

## 4. Teste de extensibilidade

Duas novas operações foram adicionadas além da exigida (`%`): `max` e
`min`. Cada uma foi adicionada só com uma linha no enum, reaproveitando
métodos prontos do `Math`:

```java
MAXIMO("max", "Máximo entre os dois valores", Math::max),
MINIMO("min", "Mínimo entre os dois valores", Math::min);
```

Nenhuma outra parte do programa (menu, leitura de entrada, histórico,
tratamento de erro) precisou ser tocada — o menu já lista a nova operação
automaticamente porque é gerado a partir do enum.

## 5. Resposta à pergunta central

> Se amanhã fosse necessário adicionar dez novas operações à calculadora,
> como sua solução se comportaria?

Cada nova operação seria **uma linha nova no `enum Operacao`** (símbolo,
descrição e fórmula), sem tocar em `main`, `executarOperacao`,
`exibirHistorico` ou no menu — que é gerado dinamicamente a partir dos
valores do enum. A mudança fica **localizada** (um só lugar para olhar) e
**previsível** (mesmo padrão para todas as operações, incluindo
formatação de erro e gravação no histórico). Isso resolve diretamente o
problema da versão original, em que cada operação nova era mais um `case`
espalhando lógica pelo fluxo principal — e o risco de esquecer de repetir
algo (como aconteceu com o histórico) crescia junto com o número de casos.
