package de.gherkineditor.controller;

import de.gherkineditor.facade.ProjectFacade;
import de.gherkineditor.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


@RestController
public class ProjectRestController {

    @Autowired
    ProjectFacade projectFacade;

    @RequestMapping(value = "/rest/api/project/import", method = RequestMethod.POST, produces = "application/json")
    public Project importProject(@RequestParam(name = "file", required = true) MultipartFile file, @RequestParam(name = "projectId", required = true) String projectId) {
        return this.projectFacade.importProject(projectId, file);
    }

    @RequestMapping(path = "/rest/api/project/export", method = RequestMethod.GET)
    public ResponseEntity<Resource> exportProject(@RequestParam(name = "projectId", required = true) String projectId) throws IOException {

        OutputStream out = this.projectFacade.exportProject(projectId);

        String fileName = projectId + "_export.zip";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        ByteArrayResource resource = new ByteArrayResource(((ByteArrayOutputStream) out).toByteArray());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
