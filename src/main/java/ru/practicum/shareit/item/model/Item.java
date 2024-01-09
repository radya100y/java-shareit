package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.item.dto.ItemSmallDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item implements ItemSmallDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(nullable = false)
    @Size(max = 255)
    private String name;

    @NotBlank
    @Column(nullable = false)
    @Size(max = 512)
    private String description;

    @NotNull
    @Column(nullable = false)
    private Boolean available;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private long owner;
}
