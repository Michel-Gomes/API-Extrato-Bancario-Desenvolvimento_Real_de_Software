package br.com.extratobancario.export;

import br.com.extratobancario.entities.Transacao;

import java.util.List;

public interface ExtratoExporter {

    byte[] exportar(List<Transacao> transacoes);
}