package swagger.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @NotNull(message = "Preço é obrigatório")
    @Min(value = 0, message = "Preço deve ser maior ou igual a zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    
    @NotNull(message = "Estoque é obrigatório")
    @Min(value = 0, message = "Estoque não pode ser negativo")
    @Column(nullable = false)
    private Integer estoque;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ItemPedido> itensPedido = new ArrayList<>();
    

}
