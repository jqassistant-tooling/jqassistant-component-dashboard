package org.jqassistant.tooling.dashboard.api.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ContributorDTO {
    private String name;
    private String email;
    private String ident;
}
