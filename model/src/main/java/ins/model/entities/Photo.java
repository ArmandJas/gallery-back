package ins.model.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import ins.model.interfaces.PhotoBase;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "PHOTO")
public class Photo implements PhotoBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime uploadTimestamp;
    private String name;
    private String description;

    private byte[] image;
    private byte[] thumbnailImage;

    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
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
}
