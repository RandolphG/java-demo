package com.autodesk.fusioncloud;

import com.autodesk.fusioncloud.model.Project;
import com.autodesk.fusioncloud.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getProjectsTest() throws Exception {
        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setId(2);
        project2.setName("Project2");

        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project1, project2));

        mockMvc.perform(get("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id': 1,'name': 'Project1'},{'id': 2,'name': 'Project2'}]"));
    }

    @Test
    public void getProjectByIdTest() throws Exception {
        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Project1");

        when(projectService.getProject(1)).thenReturn(Optional.of(project1));

        mockMvc.perform(get("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id': 1,'name': 'Project1'}"));
    }

    @Test
    public void addProjectTest() throws Exception {
        Project newProject = new Project();
        newProject.setId(1);
        newProject.setName("Project1");

        doNothing().when(projectService).createProject(newProject);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProject)))
                .andExpect(status().isCreated());

        verify(projectService, times(1)).createProject(any(Project.class));
    }


    @Test
    public void deleteProjectTest() throws Exception {
        int projectId = 1;

        doNothing().when(projectService).deleteProject(projectId);

        mockMvc.perform(delete("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService, times(1)).deleteProject(projectId);
    }

}

