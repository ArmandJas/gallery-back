package ins.model.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PHOTO")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime uploadTimestamp;
    private String name;
    private String description;
    private byte[] image;

    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST
            })
    @JoinTable(name = "photo_tag",
            joinColumns = @JoinColumn(name = "photo_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            indexes = {
                    @Index(name = "idx_relation_photo_id", columnList = "photo_id"),
                    @Index(name = "idx_relation_tag_id", columnList = "tag_id")
            }
    )
    private Set<Tag> tags = new HashSet<>();

    public Photo() {
    }
}
