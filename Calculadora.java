void main () {

    IO.println("Olá, digite o primeiro numero");
    var n1 = IO.readln();
    IO.println("Digite o segundo numero: ");
    var n2 = IO.readln();
    var operacao = IO.readln("Qual operação? EX: +-*/");

    if (operacao.equals("+")){
        IO.println(Integer.parseInt(n1)+Integer.parseInt(n2));
    } else if (operacao.equals("-")) {
        IO.println(Integer.parseInt(n1)- Integer.parseInt(n2));
    } else if (operacao.equals("*")) {
        IO.println(Integer.parseInt(n1)* Integer.parseInt(n2));
    }
    else {
        IO.println(Integer.parseInt(n1)/ Integer.parseInt(n2));
    }
}