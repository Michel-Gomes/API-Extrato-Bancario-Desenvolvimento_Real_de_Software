package br.com.extratobancario.controller;

import br.com.extratobancario.dto.ResultadoLiquidoDTO;
import br.com.extratobancario.dto.TransacaoRequestDTO;
import br.com.extratobancario.entities.Transacao;
import br.com.extratobancario.services.EstatisticaExtratoService;
import br.com.extratobancario.services.ExtratoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extrato")
@RequiredArgsConstructor
@Tag(name = "Extrato Controller", description = "Endpoints para gerenciamento de extratos bancários")
public class ExtratoController {

    private final ExtratoService extratoService;
    private final EstatisticaExtratoService estatisticasService;

    @Operation(summary = "Resultado Líquido", description = "Obtém o resultado líquido do extrato")
    @GetMapping("/resultado-liquido")
    public ResponseEntity<ResultadoLiquidoDTO> resultadoLiquido() {
        return ResponseEntity.ok(estatisticasService.obterResultadoLiquido());
    }

    @Operation(summary = "Quantidade de transações do Mês", description = "Contagem de transações do mês")
    @GetMapping("/transacoes-mes")
    public ResponseEntity<Long> transacoesDoMes(
            @Parameter(description = "Ano com quatro dígitos, por exemplo 2026")@RequestParam int ano,
            @Parameter(description = "Mês (1-12)")@RequestParam int mes) {
        return ResponseEntity.ok(estatisticasService.contarTransacoesDoMes(ano, mes));
    }

    @Operation(summary = "Top 10 Despesas", description = "Obtém as top 10 despesas")
    @GetMapping("/top-despesas")
    public ResponseEntity<List<Transacao>> topDespesas() {
        return ResponseEntity.ok(estatisticasService.obterTop10Despesas());
    }

    @Operation(summary = "Categoria com Mais Gastos", description = "Obtém a categoria com mais gastos")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada")
    @ApiResponse(responseCode = "204", description = "Não há despesas cadastradas")
    @GetMapping("/categoria-mais-gasta")
    public ResponseEntity<String> categoriaMaisGasta() {
        return estatisticasService.obterCategoriaComMaisGastos()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(summary = "Exportar CSV", description = "Exporta o extrato em formato CSV")
    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportarCsv() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=extrato.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(extratoService.exportarExtrato());
    }

    @Operation(summary = "Registrar Transação", description = "Registra uma nova transação")
    @ApiResponse(responseCode = "201", description = "Transação registrada com sucesso")
    @PostMapping
    public ResponseEntity<Transacao> registrarTransacao(@RequestBody TransacaoRequestDTO dto) {
        Transacao transacaoSalva = extratoService.registrarTransacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoSalva);
    }
}