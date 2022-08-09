package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Entity
@Table(name = "items")
public class Item {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotNull
    @NotEmpty
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "is_available")
    private Boolean available;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY,
            mappedBy = "item")
    private List<Comment> comments;

}
