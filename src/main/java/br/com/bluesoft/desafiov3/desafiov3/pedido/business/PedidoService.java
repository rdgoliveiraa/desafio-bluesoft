package br.com.bluesoft.desafiov3.desafiov3.pedido.business;

import br.com.bluesoft.desafiov3.desafiov3.pedido.model.exception.QuantidadeDeItensInsuportavelParaPedidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.bluesoft.desafiov3.desafiov3.pedido.model.FormaPagamento;
import br.com.bluesoft.desafiov3.desafiov3.pedido.model.ItemPedido;
import br.com.bluesoft.desafiov3.desafiov3.pedido.model.Pedido;
import br.com.bluesoft.desafiov3.desafiov3.pedido.model.exception.EstoqueVazioException;
import br.com.bluesoft.desafiov3.desafiov3.pedido.repository.PedidoRepository;
import br.com.bluesoft.desafiov3.desafiov3.pedido.web.form.ItemPedidoFormulario;
import br.com.bluesoft.desafiov3.desafiov3.pedido.web.form.PedidoFormulario;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.*;

@Service
public class PedidoService {

    public static final int QUANTIDADE_MAXIMA_DE_ITENS_PEDIDO = 50;
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MovimentoEstoqueService movimentoEstoqueService;

    public PedidoService(PedidoRepository pedidoRepository, MovimentoEstoqueService movimentoEstoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.movimentoEstoqueService = movimentoEstoqueService;
    }

    @Transactional(rollbackFor = EstoqueVazioException.class)
    public Pedido novoPedido(PedidoFormulario pedidoFormulario) throws QuantidadeDeItensInsuportavelParaPedidoException, EstoqueVazioException {
        if(pedidoFormulario.getItens().size() > QUANTIDADE_MAXIMA_DE_ITENS_PEDIDO) {
            throw new QuantidadeDeItensInsuportavelParaPedidoException();
        }
        Pedido pedido = new Pedido();
        pedido.setFormaPagamento(pedidoFormulario.getFormaPagamento());
        pedido.setRetiradaNaLoja(pedidoFormulario.isRetiradaNaLoja());
        for (ItemPedidoFormulario item : pedidoFormulario.getItens()) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setDescricaoProduto(item.getDescricaoProduto());
            itemPedido.setQuantidade(item.getQuantidade());
            pedido.getItens().add(itemPedido);
        }

        final Pedido pedidoCriado = pedidoRepository.salvarPedido(pedido);


        movimentoEstoqueService.movimentarEstoquePedido(pedidoCriado, pedidoFormulario.isSimularFalha());


        return pedidoCriado;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.listarTodos();
    }

    public Map<FormaPagamento, Long> listarQuantidadeDePedidosPorFormaDePagamento() {
        final List<Pedido> todosOsPedidos = pedidoRepository.listarTodos();
        return agruparPedidoPorFormaDePagamento(todosOsPedidos);
    }

    private Map<FormaPagamento, Long> agruparPedidoPorFormaDePagamento(List<Pedido> todosOsPedidos) {
        Map<FormaPagamento, Long> mapPedidos = todosOsPedidos.stream()
                .collect(groupingBy(pedido -> pedido.getFormaPagamento(), Collectors.counting()));
        return mapPedidos;
    }

    public Pedido buscarPedido(Long pedidoId) {
        return pedidoRepository.buscarPedido(pedidoId);
    }

    public void deletarPedido(Long pedidoId) {
        final Pedido pedido = pedidoRepository.buscarPedido(pedidoId);
        pedidoRepository.deletarPedido(pedido);
    }
}
