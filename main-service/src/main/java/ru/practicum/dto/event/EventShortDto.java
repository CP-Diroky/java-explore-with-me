package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
     private String annotation;
     private CategoryDto category;
     private int confirmedRequests;
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     private LocalDateTime eventDate;
     private Long id;
     private UserShortDto initiator;
     private boolean paid;
     private String title;
     private Long views;
}
