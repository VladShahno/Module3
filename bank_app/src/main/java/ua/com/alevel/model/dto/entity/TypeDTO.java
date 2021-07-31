package ua.com.alevel.model.dto.entity;

public class TypeDTO {

    private Long id;

    private String name;

    public TypeDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TypeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
