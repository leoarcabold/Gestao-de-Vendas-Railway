package swagger.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import swagger.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    List<Produto> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax);
    List<Produto> findByEstoqueGreaterThan(Integer estoque);
    
    @Query("SELECT p FROM Produto p WHERE p.estoque = 0")
    List<Produto> findProdutosSemEstoque();
    
    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Produto> searchByNomeOrDescricao(@Param("termo") String termo);
}


