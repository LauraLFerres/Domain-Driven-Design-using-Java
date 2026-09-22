import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

void main() {
    var historico = new ArrayList<String>();

    while (true) {
        IO.println("""
                Calculadora v2.0, digite .exit para sair, digite -h para ver o histórico ou enter para continuar.
                """);
        var comando = IO.readln();

        if (comando.equals(".exit")) {
            System.exit(0);
        } else if (comando.equals("-h")) {
            exibirHistorico(historico);
        } else if (comando.isEmpty()) {
            executarOperacao(historico);
        }
    }
}

void executarOperacao(List<String> historico) {
    IO.println("Digite o primeiro número:");
    var num1 = Float.parseFloat(IO.readln());
    IO.println("Digite o segundo número:");
    var num2 = Float.parseFloat(IO.readln());

    IO.println(Operacao.menu());
    var simbolo = IO.readln();
    var operacao = Operacao.porSimbolo(simbolo);

    if (operacao == null) {
        IO.println("Operação inválida");
        return;
    }

    try {
        var resultado = formatarResultado(operacao.aplicar(num1, num2));
        IO.println(resultado);
        historico.add(num1 + " " + simbolo + " " + num2 + " = " + resultado);
    } catch (ArithmeticException erro) {
        IO.println(erro.getMessage());
    }
}

void exibirHistorico(List<String> historico) {
    IO.println("Histórico: ");
    for (var linha : historico)
        IO.println(linha);
}

String formatarResultado(double resultado) {
    if (resultado == Math.rint(resultado) && !Double.isInfinite(resultado))
        return String.valueOf((long) resultado);
    return String.valueOf(resultado);
}

enum Operacao {
    SOMA("+", "Soma", (a, b) -> a + b),
    SUBTRACAO("-", "Subtração", (a, b) -> a - b),
    MULTIPLICACAO("*", "Multiplicação", (a, b) -> a * b),
    DIVISAO("/", "Divisão", (a, b) -> {
        if (b == 0) throw new ArithmeticException("Divisão por zero é inválida");
        return a / b;
    }),
    POTENCIACAO("pow", "Potenciação", Math::pow),
    RESTO("%", "Resto da divisão", (a, b) -> {
        if (b == 0) throw new ArithmeticException("Divisão por zero é inválida");
        return a % b;
    }),
    MAXIMO("max", "Máximo entre os dois valores", Math::max),
    MINIMO("min", "Mínimo entre os dois valores", Math::min);

    private final String simbolo;
    private final String descricao;
    private final DoubleBinaryOperator calculo;

    Operacao(String simbolo, String descricao, DoubleBinaryOperator calculo) {
        this.simbolo = simbolo;
        this.descricao = descricao;
        this.calculo = calculo;
    }

    double aplicar(double a, double b) {
        return calculo.applyAsDouble(a, b);
    }

    static Operacao porSimbolo(String simbolo) {
        for (var operacao : values())
            if (operacao.simbolo.equals(simbolo))
                return operacao;
        return null;
    }

    static String menu() {
        var texto = new StringBuilder("Digite a operação desejada:\n");
        for (var operacao : values())
            texto.append("  ").append(operacao.simbolo).append(" | ").append(operacao.descricao).append("\n");
        return texto.toString();
    }
}
