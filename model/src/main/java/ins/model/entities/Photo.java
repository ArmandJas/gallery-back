package ins.model.entities;

import java.sql.Timestamp;
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

    private Timestamp uploadTimestamp;
    private String name;
    private String description;
    private byte[] image;

    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "photo_tag",
            joinColumns = @JoinColumn(name = "photo_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            indexes = {
                    @Index(name = "idx_photo_id", columnList = "photo_id"),
                    @Index(name = "idx_tag_id", columnList = "tag_id")
            }
    )
    private Set<Tag> tags = new HashSet<>();

    public Photo(String name, byte[] image) {
        this.name = name;
        this.image = image;
    }

    public Photo() {
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getPhotos().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getPhotos().remove(this);
    }

    private static Timestamp createTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Photo)) {
            return false;
        }
        return id != null && id.equals(((Photo) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
