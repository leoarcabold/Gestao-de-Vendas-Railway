package swagger.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import swagger.model.Produto;
import swagger.repository.ProdutoRepository;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }
    
    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }
    
    public List<Produto> findByNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public List<Produto> findByPrecoRange(BigDecimal min, BigDecimal max) {
        return produtoRepository.findByPrecoBetween(min, max);
    }
    
    public List<Produto> findComEstoque() {
        return produtoRepository.findByEstoqueGreaterThan(0);
    }
    
    public List<Produto> findSemEstoque() {
        return produtoRepository.findProdutosSemEstoque();
    }
    
    public List<Produto> search(String termo) {
        return produtoRepository.searchByNomeOrDescricao(termo);
    }
    
    @Transactional
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }
    
    @Transactional
    public Produto update(Long id, Produto produtoDetails) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
        
        produto.setNome(produtoDetails.getNome());
        produto.setDescricao(produtoDetails.getDescricao());
        produto.setPreco(produtoDetails.getPreco());
        produto.setEstoque(produtoDetails.getEstoque());
        
        return produtoRepository.save(produto);
    }
    
    @Transactional
    public void deleteById(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
        
        // Verifica se o produto está em algum pedido antes de deletar
        if (!produto.getItensPedido().isEmpty()) {
            throw new RuntimeException("Não é possível excluir produto com pedidos associados");
        }
        
        produtoRepository.deleteById(id);
    }
    
    @Transactional
    public Produto atualizarEstoque(Long id, Integer quantidade) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
        
        int novoEstoque = produto.getEstoque() + quantidade;
        if (novoEstoque < 0) {
            throw new RuntimeException("Estoque insuficiente. Disponível: " + produto.getEstoque());
        }
        
        produto.setEstoque(novoEstoque);
        return produtoRepository.save(produto);
    }
}