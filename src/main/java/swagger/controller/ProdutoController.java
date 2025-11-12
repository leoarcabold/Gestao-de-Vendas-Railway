package swagger.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import swagger.model.Produto;
import swagger.service.ProdutoService;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;
    
    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.findById(id);
        return produto.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public List<Produto> searchProdutos(@RequestParam String nome) {
        return produtoService.findByNome(nome);
    }
    
    @GetMapping("/search-termo")
    public List<Produto> searchProdutosByTermo(@RequestParam String termo) {
        return produtoService.search(termo);
    }
    
    @GetMapping("/preco-range")
    public List<Produto> getProdutosByPrecoRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return produtoService.findByPrecoRange(min, max);
    }
    
    @GetMapping("/com-estoque")
    public List<Produto> getProdutosComEstoque() {
        return produtoService.findComEstoque();
    }
    
    @GetMapping("/sem-estoque")
    public List<Produto> getProdutosSemEstoque() {
        return produtoService.findSemEstoque();
    }
    
    @PostMapping
    public ResponseEntity<Produto> createProduto(@Valid @RequestBody Produto produto) {
        try {
            Produto savedProduto = produtoService.save(produto);
            return ResponseEntity.ok(savedProduto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @Valid @RequestBody Produto produtoDetails) {
        try {
            Produto updatedProduto = produtoService.update(id, produtoDetails);
            return ResponseEntity.ok(updatedProduto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        try {
            produtoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PatchMapping("/{id}/estoque")
    public ResponseEntity<Produto> atualizarEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        try {
            Produto produto = produtoService.atualizarEstoque(id, quantidade);
            return ResponseEntity.ok(produto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
