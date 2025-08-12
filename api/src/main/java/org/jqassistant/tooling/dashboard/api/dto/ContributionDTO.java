package org.jqassistant.tooling.dashboard.api.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ContributionDTO {
    private int commits;
    private ContributorDTO contributor;
}
