package br.com.extratobancario.export;

import br.com.extratobancario.entities.Transacao;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtratoCsvExporterTest {

    private final ExtratoCsvExporter exporter = new ExtratoCsvExporter();

    private String exportar(Transacao... transacoes) {
        return new String(exporter.exportar(List.of(transacoes)), StandardCharsets.UTF_8);
    }

    private Transacao transacao(String descricao, String categoria, String valor) {
        return Transacao.builder()
                .data(LocalDateTime.of(2026, 9, 1, 10, 30))
                .descricao(descricao)
                .categoria(categoria)
                .valor(new BigDecimal(valor))
                .build();
    }

    @Test
    void listaVaziaGeraSomenteCabecalho() {
        assertEquals("Data,Descricao,Categoria,Valor\n", exportar());
    }

    @Test
    void geraUmaLinhaPorTransacao() {
        String csv = exportar(transacao("Padaria", "Mercado", "-25.50"));

        assertEquals("Data,Descricao,Categoria,Valor\n2026-09-01T10:30,Padaria,Mercado,-25.50\n", csv);
    }

    @Test
    void descricaoComVirgulaVaiEntreAspas() {
        String csv = exportar(transacao("Pão, leite e café", "Mercado", "-10.00"));

        assertEquals("Data,Descricao,Categoria,Valor\n2026-09-01T10:30,\"Pão, leite e café\",Mercado,-10.00\n", csv);
    }

    @Test
    void aspasInternasSaoDuplicadas() {
        String csv = exportar(transacao("Loja \"Boa\"", "Compras", "-5.00"));

        assertEquals("Data,Descricao,Categoria,Valor\n2026-09-01T10:30,\"Loja \"\"Boa\"\"\",Compras,-5.00\n", csv);
    }

    @Test
    void camposNulosViramVazios() {
        String csv = exportar(transacao(null, null, "1.00"));

        assertEquals("Data,Descricao,Categoria,Valor\n2026-09-01T10:30,,,1.00\n", csv);
    }
}