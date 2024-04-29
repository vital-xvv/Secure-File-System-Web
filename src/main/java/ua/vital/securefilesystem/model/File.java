package ua.vital.securefilesystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Main entity in relation Many-to-One with {@link User}
 * @author Vitalii Huzii
 */
@Entity
@Table(name = "Files")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private int size;
    private String extension;
    @Column(nullable = false)
    private List<Language> languages;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

}