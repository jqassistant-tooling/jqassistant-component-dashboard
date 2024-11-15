package org.jqassistant.tooling.dashboard.api.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VersionDTO {

    String version;

    List<FileDTO> containsFiles;

}
