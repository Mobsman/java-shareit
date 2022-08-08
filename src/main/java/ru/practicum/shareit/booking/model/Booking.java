package ru.practicum.shareit.booking.model;


import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name="start_date")
    private LocalDateTime start;

    @Column(name="end_date")
    private LocalDateTime end;

    @ManyToOne()
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne()
    @JoinColumn(name = "booker_id")
    private User booker;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state_id")
    private Status status;

}
