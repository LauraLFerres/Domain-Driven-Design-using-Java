//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    var dolarAm = new Moeda();
    dolarAm.nome = "Dolar Americano";
    dolarAm.cifra = "$";
    dolarAm.valor = 1;

    var realBr = new Moeda();
    realBr.nome = "Real Brasileiro";
    realBr.cifra = "R$";
    realBr.valor = 5.15;

    IO.println("""
            Bem vindo ao sistema de conversão de moedas.
            Digite a opção de moeda a ser convertida:
            1 - Dolar
            2 - Real
    """);

    var moeda1 = IO.readln();
    IO.println("""
            Digite a opção de moeda para converter
            1 - Dolar
            2 - Real
    """);
    var moeda2 = IO.readln();

    if(moeda1.equals("1") && moeda2.equals("2")) {
        IO.println("Digite o valor da moeda:");
        var valor = Float.parseFloat(IO.readln());
        IO.print(dolarAm.cifra);
        IO.print(valor + "=");
        IO.print((valor * dolarAm.valor) * realBr.valor);
        IO.print(realBr.cifra);
    }


}
