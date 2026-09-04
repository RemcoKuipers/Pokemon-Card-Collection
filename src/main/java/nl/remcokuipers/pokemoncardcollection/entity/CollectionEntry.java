package nl.remcokuipers.pokemoncardcollection.entity;

import jakarta.persistence.*;
import nl.remcokuipers.pokemoncardcollection.enums.Condition;

@Entity
@Table(name = "collection_entries")
public class CollectionEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int quantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Condition condition;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "pokemon_card_id", nullable = false)
    private PokemonCard pokemonCard;

    public CollectionEntry() {

    }

    public CollectionEntry(int quantity, Condition condition, User user, PokemonCard pokemonCard) {
        this.quantity = quantity;
        this.condition = condition;
        this.user = user;
        this.pokemonCard = pokemonCard;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PokemonCard getPokemonCard() {
        return pokemonCard;
    }

    public void setPokemonCard(PokemonCard pokemonCard) {
        this.pokemonCard = pokemonCard;
    }

}
