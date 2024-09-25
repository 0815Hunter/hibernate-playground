package ch.ti8m.playground.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Bericht")
@Getter
@Setter
public class Bericht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BerichtId")
    private Integer berichtId;

    @Column(name = "Archived")
    private boolean archived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_EntwurfId", referencedColumnName = "EntwurfId")
    private Entwurf entwurf;
}
