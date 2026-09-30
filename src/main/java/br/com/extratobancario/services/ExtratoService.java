package br.com.extratobancario.services;

import br.com.extratobancario.dto.TransacaoRequestDTO;
import br.com.extratobancario.entities.Transacao;
import br.com.extratobancario.export.ExtratoExporter;
import br.com.extratobancario.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExtratoService {

    private final TransacaoRepository transacaoRepository;
    private final ExtratoExporter extratoExporter;

    public Transacao registrarTransacao(TransacaoRequestDTO dto) {
        Transacao transacao = Transacao.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .valor(dto.valor())
                .data(dto.data())
                .categoria(dto.categoria())
                .build();

        return transacaoRepository.save(transacao);
    }

    public byte[] exportarExtrato() {
        return extratoExporter.exportar(transacaoRepository.findAllByOrderByDataAsc());
    }
}