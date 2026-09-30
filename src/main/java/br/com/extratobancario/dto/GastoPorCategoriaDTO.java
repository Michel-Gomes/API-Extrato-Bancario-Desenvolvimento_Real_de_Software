package br.com.extratobancario.dto;

import java.math.BigDecimal;

public record GastoPorCategoriaDTO(String categoria, BigDecimal total) {
}
