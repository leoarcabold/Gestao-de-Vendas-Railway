package swagger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import swagger.model.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedidoId(Long pedidoId);
    List<ItemPedido> findByProdutoId(Long produtoId);
    
    @Query("SELECT ip FROM ItemPedido ip WHERE ip.pedido.id = :pedidoId AND ip.produto.id = :produtoId")
    Optional<ItemPedido> findByPedidoAndProduto(@Param("pedidoId") Long pedidoId, @Param("produtoId") Long produtoId);
    
    @Query("SELECT SUM(ip.quantidade) FROM ItemPedido ip WHERE ip.produto.id = :produtoId")
    Integer findTotalVendidoByProdutoId(@Param("produtoId") Long produtoId);
}
