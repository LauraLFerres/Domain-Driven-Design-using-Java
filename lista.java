void main(){
    var historicoDeOperacoes = new ArrayList<String>();

    while(true) {
        IO.println("""
                Calculadora v1.0, digite .exit para sair, digite -h para ver o histórico ou enter para continuar.
                """);
        var commando = IO.readln();
        if(commando.equals(".exit"))
            System.exit(0);
        else if (commando.equals("-h")) {
            IO.println("Histórico: ");
            for (var operacao : historicoDeOperacoes)
                IO.println(operacao);
        }
        else if(commando.isEmpty()) {
            IO.println("Digite o primeiro número:");
            var n1 = IO.readln();
            IO.println("Digite o segundo número:");
            var n2 = IO.readln();
            IO.println("""
                    Digite a operação desejada:
                    + | Soma
                    - | Subtração
                    * | Multiplicação
                    / | Divisão
                    pow | Elevação
                    """);
            var operacao = IO.readln();

            var num1 = Float.parseFloat(n1);
            var num2 = Float.parseFloat(n2);

            switch (operacao) {
                case "+" -> {
                    IO.println(num1 + num2);
                    historicoDeOperacoes.add(num1 + " + " + num2 + " = " + (num1 + num2));
                }
                case "-" -> IO.println(num1 - num2);
                case "*" -> IO.println(num1 * num2);
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
                case "pow" -> IO.println(Math.pow(num1, num2));
                case null, default -> IO.println("Operação inválida");
            }
        }
    }
}
