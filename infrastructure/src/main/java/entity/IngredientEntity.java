package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import domain.model.Ingredient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredient")
public class IngredientEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "ingredients")
    @JsonIgnoreProperties(value = {"ingredients"}, allowSetters = true)
    private Set<RecipeEntity> recipes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RecipeEntity> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<RecipeEntity> recipes) {
        if (this.recipes != null) {
            this.recipes.forEach(i -> i.removeIngredient(this));
        }
        if (recipes != null) {
            recipes.forEach(i -> i.addIngredient(this));
        }
        this.recipes = recipes;
    }

    public void addRecipe(RecipeEntity recipe) {
        this.recipes.add(recipe);
        recipe.getIngredients().add(this);
    }

    public void removeRecipe(RecipeEntity recipe) {
        this.recipes.remove(recipe);
        recipe.getIngredients().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientEntity)) return false;
        IngredientEntity that = (IngredientEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "IngredientEntity{" +
                "id=" + id +
                ", name='" + name + "'" +
                "}";
    }

    public Ingredient toDomain() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(this.id);
        ingredient.setName(this.name);
        return ingredient;
    }

    public static IngredientEntity fromDomain(Ingredient ingredient) {
        IngredientEntity entity = new IngredientEntity();
        entity.setId(ingredient.getId());
        entity.setName(ingredient.getName());
        return entity;
    }
} 