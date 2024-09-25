package ch.ti8m.playground.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "Bericht")
@Getter
@Setter
@SQLDelete(sql = "UPDATE Bericht SET Archived = 1 WHERE BerichtId = ?")
public class Bericht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BerichtId")
    private Integer berichtId;

    @Column(name = "Archived", nullable = false)
    private boolean archived = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_EntwurfId", referencedColumnName = "EntwurfId")
    private Entwurf entwurf;
}
