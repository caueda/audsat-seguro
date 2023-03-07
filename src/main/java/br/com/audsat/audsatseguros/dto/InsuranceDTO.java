package br.com.audsat.audsatseguros.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceDTO {
    private Long customerId;
    private Long carId;
    private boolean active;
}
