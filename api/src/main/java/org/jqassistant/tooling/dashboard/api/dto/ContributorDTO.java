package org.jqassistant.tooling.dashboard.api.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ContributorDTO {
    private String name;
    private String email;
    private String ident;
}
