package br.com.bluesoft.desafiov3.desafiov3.pedido.business;

import br.com.bluesoft.desafiov3.desafiov3.pedido.model.FormaPagamento;
import br.com.bluesoft.desafiov3.desafiov3.pedido.model.Pedido;
import br.com.bluesoft.desafiov3.desafiov3.pedido.repository.PedidoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoServiceTest {

    @Mock
    PedidoRepository pedidoRepository;

    @InjectMocks
    PedidoService pedidoService;

    Mockito mockito = new Mockito();

    List<Pedido> pedidos = new ArrayList<>();

    @Test
    void agruparPedidoPorFormaDePagamento() throws Exception {
        doReturn(pedidos).when(pedidoRepository).listarTodos();
        Map<FormaPagamento, Long> qtdPedidos = pedidoService.listarQuantidadeDePedidosPorFormaDePagamento();
        Assertions.assertNotNull(qtdPedidos);

    }

    @BeforeEach
    void criarListaMockPedidos() {
        for (int i =0; i <= 5; i++) {
            Pedido pedido = new Pedido();
            pedido.setId(Long.valueOf(i));
            pedido.setFormaPagamento(FormaPagamento.CREDITO);
            pedido.setItens(null);
            pedido.setRetiradaNaLoja(true);
            pedidos.add(pedido);
        }
    }
}