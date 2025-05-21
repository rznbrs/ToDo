package com.todo.todoapp;

import com.todo.todoapp.Entity.Task;
import com.todo.todoapp.Repository.TaskRepository;
import com.todo.todoapp.Service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        List<Task> mockTasks = Arrays.asList(new Task(), new Task());
        when(taskRepository.findAll()).thenReturn(mockTasks);

        List<Task> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(2);
        verify(taskRepository).findAll();
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> found = taskService.getTaskById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
    }

    @Test
    void createTask_ShouldSaveAndReturnTask() {
        Task task = new Task();
        when(taskRepository.save(task)).thenReturn(task);

        Task saved = taskService.createTask(task);

        assertThat(saved).isEqualTo(task);
        verify(taskRepository).save(task);
    }

    @Test
    void deleteTask_ShouldCallRepositoryDelete() {
        Long id = 1L;

        taskService.deleteTask(id);

        verify(taskRepository).deleteById(id);
    }

    @Test
    void updateTask_ShouldSetIdAndSave() {
        Long id = 1L;
        Task task = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updated = taskService.updateTask(id, task);

        assertThat(updated).isEqualTo(task);
        verify(taskRepository).save(task);
        assertThat(task.getId()).isEqualTo(id);
    }

    @Test
    void markTaskAsCompleted_ShouldSetCompletedTrueAndSave() {
        Task task = new Task();
        task.setId(1L);
        task.setCompleted(false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task updated = taskService.markTaskAsCompleted(1L);

        assertThat(updated.isCompleted()).isTrue();
        verify(taskRepository).save(task);
    }
}
