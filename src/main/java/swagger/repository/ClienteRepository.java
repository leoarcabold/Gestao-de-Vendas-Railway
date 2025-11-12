package swagger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import swagger.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.pedidos WHERE c.id = :id")
    Optional<Cliente> findByIdWithPedidos(@Param("id") Long id);
}
