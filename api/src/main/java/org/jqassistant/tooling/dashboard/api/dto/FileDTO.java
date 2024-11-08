package org.jqassistant.tooling.dashboard.api.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDTO {

    private String fileName;

    private List<CapabilityDTO> providesCapabilities;

    private List<CapabilityDTO> requiresCapabilities;

}
