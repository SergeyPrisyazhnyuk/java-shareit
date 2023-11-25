package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "available", nullable = false)
    private Boolean available;

/*    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude*/
    @Column(name = "owner_id", nullable = false)
    private Long owner;

/*    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude*/
   @Column(name = "request_id", nullable = false)
   private Long request;

    public Item(String name, String description, Boolean available, Long owner, Long request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}
