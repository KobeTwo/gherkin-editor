package de.gherkineditor.controller;

import de.gherkineditor.facade.ProjectFacade;
import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Folder;
import de.gherkineditor.model.Project;
import de.gherkineditor.model.Scenario;
import de.gherkineditor.repository.FeatureRepository;
import de.gherkineditor.repository.FolderRepository;
import de.gherkineditor.repository.ProjectRepository;
import de.gherkineditor.repository.ScenarioRepository;
import de.gherkineditor.util.Util;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;


@RestController
public class ProjectRestController {

    @Autowired
    ProjectFacade projectFacade;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ScenarioRepository scenarioRepository;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    Client client;

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

    @RequestMapping(value = "project/{projectId}", produces = "application/json", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable(name = "projectId", required = true) String projectId) {
        Optional<Project> projectOptional = this.projectRepository.findById(projectId);
        if (projectOptional.isPresent()) {
            String path = "/";
            Iterable<Scenario> scenariosToDelete = this.scenarioRepository.findChildrenRecursive(projectOptional.get().getId(), path, null);
            Iterable<Feature> featuresToDelete = this.featureRepository.findChildrenRecursive(projectOptional.get().getId(), path, null);
            Iterable<Folder> foldersToDelete = this.folderRepository.findChildrenRecursive(projectOptional.get().getId(), path, null);
            this.scenarioRepository.deleteAll(scenariosToDelete);
            this.featureRepository.deleteAll(featuresToDelete);
            this.folderRepository.deleteAll(foldersToDelete);
            this.projectRepository.delete(projectOptional.get());
        }
    }
}
