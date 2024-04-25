package com.pubfinder.pubfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UVPDTO {
    private PubDTO pubDTO;
    private LocalDateTime visitedDate;
}
