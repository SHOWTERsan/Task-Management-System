package ru.santurov.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO для пагинированного ответа")
public class PaginatedResponseDTO<T> {

    @Schema(description = "Список данных на текущей странице")
    private List<T> data;

    @Schema(description = "Текущая страница", example = "0")
    private int currentPage;

    @Schema(description = "Общее количество страниц", example = "10")
    private int totalPages;

    @Schema(description = "Общее количество элементов", example = "100")
    private long totalItems;

    public PaginatedResponseDTO(List<T> data, int currentPage, int totalPages, long totalItems) {
        this.data = data;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }
}
