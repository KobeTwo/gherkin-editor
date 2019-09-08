package de.gherkineditor.service;

import de.gherkineditor.model.Feature;
import de.gherkineditor.model.Project;
import de.gherkineditor.model.Step;
import de.gherkineditor.repository.ProjectRepository;
import de.gherkineditor.util.Util;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ByteSource;
import org.zeroturnaround.zip.ZipEntryCallback;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;

@Service
public class DefaultProjectService extends AbstractModelService<Project> implements ProjectService {

    Logger logger = LoggerFactory.getLogger(DefaultProjectService.class);

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FeatureService featureService;




    @Override
    public Iterable<Project> getAllProjects() {
        Iterable<Project> projects = this.projectRepository.findAll();
        return projects;
    }

    @Override
    public Project getProject(String id) {
        Optional<Project> project = this.projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new IllegalArgumentException("Project not found");
        }
        return project.get();
    }

    @Override
    public Project importProject(String projectId, InputStream zipFileInputStream) throws IOException {
        Project project = new Project(projectId);
        this.create(project);
        ZipUtil.iterate(zipFileInputStream, new ZipEntryCallback() {
            @Override
            public void process(InputStream in, ZipEntry zipEntry) throws IOException {
                if (zipEntry.getName().endsWith(".feature")) {
                    DefaultProjectService.this.logger.info("Found feature file in zip " + zipEntry.getName() + "for project import " + projectId);
                    String content = IOUtils.toString(in, StandardCharsets.UTF_8);
                    DefaultProjectService.this.featureService.createFeature(project, "/" + zipEntry.getName(), content);
                }
            }
        });
        return project;
    }

    @Override
    public ByteArrayOutputStream exportProject(String projectId) {
        Project project = this.getProject(projectId);

        ArrayList<ZipEntrySource> sourceList = new ArrayList<>();

        Iterable<Feature> features = this.featureService.listFeatures(projectId);

        for (Feature feature : features) {
            sourceList.add(new ByteSource(Util.getConcatenatedPath(feature.getPath(), feature.getFileName()), this.featureService.getFeatureContent(feature).getBytes()));
        }

        ZipEntrySource[] entries = new ZipEntrySource[sourceList.size()];
        sourceList.toArray(entries);

        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();
            ZipUtil.pack(entries, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
        return out;
    }


    @Override
    public Project create(Project project) {
        validateCreate(project);
        return this.projectRepository.save(project);
    }

    @Override
    public Project update(Project project) {
        validateUpdate(project);
        return this.projectRepository.save(project);
    }
}
