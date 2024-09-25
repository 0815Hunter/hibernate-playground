package ch.ti8m.playground.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Entwurf")
@Getter
@Setter
public class Entwurf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EntwurfId")
    private Integer entwurfId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "FK_ActiveBerichtId", referencedColumnName = "BerichtId")
    private Bericht bericht;

    @Column(name = "Content")
    private String content;

    public void setBericht(Bericht bericht) {
        bericht.setEntwurf(this);
        this.bericht = bericht;
    }
}
