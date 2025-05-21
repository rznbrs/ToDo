package com.todo.todoapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.todoapp.Controller.TaskController;
import com.todo.todoapp.Entity.Task;
import com.todo.todoapp.Service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)  // Sadece TaskController test edilir
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;  // Service katmanı mocklanır

    @Autowired
    private ObjectMapper objectMapper; // JSON dönüşümleri için

    private Task task1;

    @BeforeEach
    public void setup() {
        task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Test Task");
        task1.setCompleted(false);
    }

    @Test
    public void testGetAllTasks() throws Exception {
        List<Task> tasks = Arrays.asList(task1);

        Mockito.when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(task1.getId()))
                .andExpect(jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("$[0].completed").value(task1.isCompleted()));
    }

    @Test
    public void testCreateTask() throws Exception {
        Mockito.when(taskService.createTask(any(Task.class))).thenReturn(task1);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task1.getId()))
                .andExpect(jsonPath("$.title").value(task1.getTitle()));
    }

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(taskService).deleteTask(1L);
    }

    @Test
    public void testUpdateTask() throws Exception {
        Mockito.when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(task1);

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task1.getId()))
                .andExpect(jsonPath("$.title").value(task1.getTitle()));
    }

    @Test
    public void testMarkTaskAsCompleted() throws Exception {
        task1.setCompleted(true);
        Mockito.when(taskService.markTaskAsCompleted(1L)).thenReturn(task1);

        mockMvc.perform(put("/api/tasks/{id}/complete", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }
//    @Test
//    public void testMarkTaskAsCompleted_TaskNotFound() throws Exception {
//        Long taskId = 999L;
//
//        // Mock servis, task bulunamadığında NoSuchElementException fırlatsın
//        Mockito.when(taskService.markTaskAsCompleted(taskId)).thenThrow(new NoSuchElementException("Task not found"));
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{id}/complete", taskId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError());
//        // Eğer ControllerAdvice ile 404 dönüyorsan .isNotFound() yap
//    }
//    @Test
//    public void testCreateTask_InvalidInput() throws Exception {
//        String invalidJson = "{\"name\":\"\"}"; // boş name
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(invalidJson))
//                .andExpect(status().isBadRequest());
//    }


}
