package swagger.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import swagger.model.Cliente;
import swagger.model.ItemPedido;
import swagger.model.Pedido;
import swagger.model.Produto;
import swagger.repository.ClienteRepository;
import swagger.repository.ItemPedidoRepository;
import swagger.repository.PedidoRepository;
import swagger.repository.ProdutoRepository;

@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    
    @Autowired
    private ProdutoService produtoService;
    
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }
    
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findByIdWithItens(id);
    }
    
    public List<Pedido> findByClienteId(Long clienteId) {
        return pedidoRepository.findByClienteIdWithItens(clienteId);
    }
    
    public List<Pedido> findByStatus(String status) {
        return pedidoRepository.findByStatus(status);
    }
    
    public List<Pedido> findByPeriodo(LocalDate inicio, LocalDate fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }
    
    @Transactional
    public Pedido save(Pedido pedido) {
        // Verifica se o cliente existe
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + pedido.getCliente().getId()));
        
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDate.now());
        
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        
        // Salva os itens do pedido
        for (ItemPedido item : pedido.getItens()) {
            item.setPedido(pedidoSalvo);
            
            // Verifica se o produto existe e tem estoque
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + item.getProduto().getId()));
            
            if (produto.getEstoque() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome() + 
                                          ". Disponível: " + produto.getEstoque());
            }
            
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());
            
            // Atualiza o estoque
            produtoService.atualizarEstoque(produto.getId(), -item.getQuantidade());
            
            itemPedidoRepository.save(item);
        }
        
        return pedidoRepository.findByIdWithItens(pedidoSalvo.getId())
            .orElseThrow(() -> new RuntimeException("Erro ao buscar pedido salvo"));
    }
    
    @Transactional
    public Pedido updateStatus(Long id, String novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
        
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }
    
    @Transactional
    public void deleteById(Long id) {
        Pedido pedido = pedidoRepository.findByIdWithItens(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
        
        // Devolve os produtos ao estoque se o pedido for cancelado/excluído
        if (!"CANCELADO".equals(pedido.getStatus())) {
            for (ItemPedido item : pedido.getItens()) {
                produtoService.atualizarEstoque(item.getProduto().getId(), item.getQuantidade());
            }
        }
        
        pedidoRepository.deleteById(id);
    }
    
    public BigDecimal calcularTotalPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findByIdWithItens(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + pedidoId));
        
        return pedido.getTotal();
    }
}