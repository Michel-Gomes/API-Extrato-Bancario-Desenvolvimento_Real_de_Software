package br.com.extratobancario.export;

import br.com.extratobancario.entities.Transacao;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ExtratoCsvExporter implements ExtratoExporter {

    private static final String CABECALHO = "Data,Descricao,Categoria,Valor\n";

    @Override
    public byte[] exportar(List<Transacao> transacoes) {
        StringBuilder sb = new StringBuilder(CABECALHO);

        for (Transacao t : transacoes) {
            sb.append(escapar(t.getData())).append(',')
                    .append(escapar(t.getDescricao())).append(',')
                    .append(escapar(t.getCategoria())).append(',')
                    .append(escapar(t.getValor())).append('\n');
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    // Regra do CSV: campo com vírgula, aspas ou quebra de linha vai entre aspas,
    // e aspas internas são duplicadas.
    private String escapar(Object valor) {
        if (valor == null) {
            return "";
        }
        String texto = valor.toString();
        if (texto.contains(",") || texto.contains("\"") || texto.contains("\n") || texto.contains("\r")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }
}