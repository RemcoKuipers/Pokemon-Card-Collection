package nl.remcokuipers.pokemoncardcollection.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_card_id", nullable = false)
    private PokemonCard pokemonCard;

    public Favorite() {

    }

    public Favorite(User user, PokemonCard pokemonCard) {
        this.user = user;
        this.pokemonCard = pokemonCard;
    }

    public Long getId() {
        return id;
    }

    public PokemonCard getPokemonCard() {
        return pokemonCard;
    }

    public void setPokemonCard(PokemonCard pokemonCard) {
        this.pokemonCard = pokemonCard;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
