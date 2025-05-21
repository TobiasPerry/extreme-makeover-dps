package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import domain.model.Recipe;
import domain.model.RecipeCategory;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipe")
public class RecipeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category")
    @Enumerated(value = EnumType.STRING)
    private RecipeCategory category;

    @Column(name = "servings")
    private Integer servings;

    @Column(name = "instructions")
    private String instructions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rel_recipe__ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    @JsonIgnoreProperties(value = {"recipes"}, allowSetters = true)
    private Set<IngredientEntity> ingredients = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecipeCategory getCategory() {
        return category;
    }

    public void setCategory(RecipeCategory category) {
        this.category = category;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Set<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(IngredientEntity ingredient) {
        this.ingredients.add(ingredient);
        ingredient.getRecipes().add(this);
    }

    public void removeIngredient(IngredientEntity ingredient) {
        this.ingredients.remove(ingredient);
        ingredient.getRecipes().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeEntity)) return false;
        RecipeEntity that = (RecipeEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RecipeEntity{" +
                "id=" + id +
                ", category='" + category + "'" +
                ", servings=" + servings +
                ", instructions='" + instructions + "'" +
                "}";
    }

    public Recipe toDomain() {
        Recipe recipe = new Recipe();
        recipe.setId(this.id);
        recipe.setCategory(this.category);
        recipe.setServings(this.servings);
        recipe.setInstructions(this.instructions);
        return recipe;
    }

    public static RecipeEntity fromDomain(Recipe recipe) {
        RecipeEntity entity = new RecipeEntity();
        entity.setId(recipe.getId());
        entity.setCategory(recipe.getCategory());
        entity.setServings(recipe.getServings());
        entity.setInstructions(recipe.getInstructions());
        return entity;
    }
} 