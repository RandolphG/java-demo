package com.autodesk.fusioncloud.service;

import com.autodesk.fusioncloud.model.Project;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ProjectService {
    private static final String PROJECT_FILE_PATH = "/static/project.txt";

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        InputStream inputStream = getClass().getResourceAsStream(PROJECT_FILE_PATH);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Stream<String> lines = reader.lines();
        lines.forEach(line -> {
            String[] details = line.split(",");
            Project project = new Project();
            project.setId(Integer.parseInt(details[0]));
            project.setName(details[1]);
            projects.add(project);
        });
        return projects;
    }

    public Optional<Project> getProject(int id) {
        InputStream inputStream = getClass().getResourceAsStream(PROJECT_FILE_PATH);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Stream<String> lines = reader.lines();
        Project project = new Project();
        Optional<String> lineOptional = lines.filter(line -> line.startsWith(id + ",")).findFirst();

        if (lineOptional.isPresent()) {
            String[] details = lineOptional.get().split(",");
            project.setId(Integer.parseInt(details[0]));
            project.setName(details[1]);
            return Optional.of(project);
        } else {
            return Optional.empty();
        }
    }

    public void createProject(Project newProject) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(PROJECT_FILE_PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            reader.close();

            URI uri = getClass().getResource(PROJECT_FILE_PATH).toURI();
            Path path = Paths.get(uri);

            String newLine = "\n" + newProject.getId() + "," + newProject.getName();
            Files.write(path, newLine.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void deleteProject(int id) throws URISyntaxException {
        InputStream inputStream = getClass().getResourceAsStream(PROJECT_FILE_PATH);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Stream<String> lines = reader.lines();
        List<String> out = new ArrayList<>();
        lines.filter(line -> !line.startsWith(id + ",")).forEach(out::add);

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outputStr = String.join("\n", out);

        URI uri = getClass().getResource(PROJECT_FILE_PATH).toURI();
        Path path = Paths.get(uri);

        try {
            Files.write(path, outputStr.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
