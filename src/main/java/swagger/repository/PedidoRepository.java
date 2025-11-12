package swagger.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import swagger.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByStatus(String status);
    List<Pedido> findByDataPedidoBetween(LocalDate inicio, LocalDate fim);
    
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.id = :id")
    Optional<Pedido> findByIdWithItens(@Param("id") Long id);
    
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.cliente.id = :clienteId")
    List<Pedido> findByClienteIdWithItens(@Param("clienteId") Long clienteId);
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.status = :status")
    Long countByStatus(@Param("status") String status);
}

