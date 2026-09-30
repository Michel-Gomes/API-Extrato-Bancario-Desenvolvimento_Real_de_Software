package br.com.extratobancario.services;

import br.com.extratobancario.dto.GastoPorCategoriaDTO;
import br.com.extratobancario.dto.ResultadoLiquidoDTO;
import br.com.extratobancario.entities.Transacao;
import br.com.extratobancario.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstatisticaExtratoService {

    private final TransacaoRepository transacaoRepository;

    public ResultadoLiquidoDTO obterResultadoLiquido() {
        BigDecimal saldo = transacaoRepository.calcularSaldoTotal();
        String classificacao = saldo.compareTo(BigDecimal.ZERO) >= 0 ? "Lucro" : "Prejuízo";
        return new ResultadoLiquidoDTO(saldo, classificacao);
    }

    public long contarTransacoesDoMes(int ano, int mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        return transacaoRepository.countByDataGreaterThanEqualAndDataLessThan(
                yearMonth.atDay(1).atStartOfDay(),
                yearMonth.plusMonths(1).atDay(1).atStartOfDay());
    }

    public List<Transacao> obterTop10Despesas() {
        return transacaoRepository.buscarMaioresDespesas(PageRequest.of(0, 10));
    }

    public Optional<String> obterCategoriaComMaisGastos() {
        List<GastoPorCategoriaDTO> resultado = transacaoRepository.agruparGastoPorCategoria();
        return resultado.stream().findFirst().map(GastoPorCategoriaDTO::categoria);
    }
}