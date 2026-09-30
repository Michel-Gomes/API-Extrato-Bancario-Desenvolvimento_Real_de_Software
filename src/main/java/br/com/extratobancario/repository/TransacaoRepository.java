package br.com.extratobancario.repository;

import br.com.extratobancario.dto.GastoPorCategoriaDTO;
import br.com.extratobancario.entities.Transacao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t")
    BigDecimal calcularSaldoTotal();

    long countByDataGreaterThanEqualAndDataLessThan(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT t FROM Transacao t WHERE t.valor < 0 ORDER BY t.valor ASC")
    List<Transacao> buscarMaioresDespesas(Pageable pageable);

    @Query("""
        SELECT new br.com.extratobancario.dto.GastoPorCategoriaDTO(t.categoria, SUM(t.valor))
        FROM Transacao t
        WHERE t.valor < 0
        GROUP BY t.categoria
        ORDER BY SUM(t.valor) ASC
        """)
    List<GastoPorCategoriaDTO> agruparGastoPorCategoria();

    List<Transacao> findAllByOrderByDataAsc();
}
