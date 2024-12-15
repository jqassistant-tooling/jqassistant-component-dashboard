package org.jqassistant.tooling.dashboard.api.dto;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VersionDTO {

    String version;

    ZonedDateTime updatedAt;

    List<FileDTO> containsFiles;

}
