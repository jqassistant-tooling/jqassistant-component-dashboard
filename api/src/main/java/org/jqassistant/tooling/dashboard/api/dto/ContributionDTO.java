package org.jqassistant.tooling.dashboard.api.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ContributionDTO {
    private long commits;
    private ContributorDTO contributor;
}
