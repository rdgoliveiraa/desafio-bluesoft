package br.com.bluesoft.desafiov3.desafiov3.pedido.model.exception;

public class QuantidadeDeItensInsuportavelParaPedidoException extends RuntimeException {

    public QuantidadeDeItensInsuportavelParaPedidoException() {
        super("Quantidade de itens de pedido maior que o valor permitido, para realização de pedido o valor maxímo de itens é de até 50!");
    }
}
