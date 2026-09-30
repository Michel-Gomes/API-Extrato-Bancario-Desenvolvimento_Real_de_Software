package br.com.extratobancario.services;

import br.com.extratobancario.dto.GastoPorCategoriaDTO;
import br.com.extratobancario.dto.ResultadoLiquidoDTO;
import br.com.extratobancario.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstatisticaExtratoServiceTest {

    @Mock
    private TransacaoRepository repository;

    @InjectMocks
    private EstatisticaExtratoService service;

    @Test
    void saldoPositivoDeveSerLucro() {
        when(repository.calcularSaldoTotal()).thenReturn(new BigDecimal("150.00"));

        ResultadoLiquidoDTO resultado = service.obterResultadoLiquido();

        assertEquals("Lucro", resultado.classificacao());
        assertEquals(new BigDecimal("150.00"), resultado.saldoTotal());
    }

    @Test
    void saldoNegativoDeveSerPrejuizo() {
        when(repository.calcularSaldoTotal()).thenReturn(new BigDecimal("-10.00"));

        assertEquals("Prejuízo", service.obterResultadoLiquido().classificacao());
    }

    @Test
    void saldoZeroDeveSerLucro() {
        when(repository.calcularSaldoTotal()).thenReturn(BigDecimal.ZERO);

        assertEquals("Lucro", service.obterResultadoLiquido().classificacao());
    }

    @Test
    void contagemDoMesDeveIncluirOMesInteiro() {
        when(repository.countByDataGreaterThanEqualAndDataLessThan(any(), any())).thenReturn(3L);

        long total = service.contarTransacoesDoMes(2026, 2);

        ArgumentCaptor<LocalDateTime> inicio = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> fim = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(repository).countByDataGreaterThanEqualAndDataLessThan(inicio.capture(), fim.capture());

        assertEquals(3L, total);
        assertEquals(LocalDateTime.of(2026, 2, 1, 0, 0), inicio.getValue());
        assertEquals(LocalDateTime.of(2026, 3, 1, 0, 0), fim.getValue());
    }

    @Test
    void categoriaComMaisGastosDeveSerAPrimeiraDaLista() {
        when(repository.agruparGastoPorCategoria()).thenReturn(List.of(
                new GastoPorCategoriaDTO("Mercado", new BigDecimal("-500")),
                new GastoPorCategoriaDTO("Lazer", new BigDecimal("-100"))));

        assertEquals(Optional.of("Mercado"), service.obterCategoriaComMaisGastos());
    }

    @Test
    void semDespesasNaoHaCategoriaMaisGasta() {
        when(repository.agruparGastoPorCategoria()).thenReturn(List.of());

        assertTrue(service.obterCategoriaComMaisGastos().isEmpty());
    }
}