package br.com.extratobancario.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoRequestDTO(String nome, String descricao, String categoria, BigDecimal valor, LocalDateTime data){}
