package nl.remcokuipers.pokemoncardcollection.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pokemon_cards")
public class PokemonCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String externalApiId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String supertype;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false)
    private String rarity;
    @Column(nullable = false)
    private String setName;
    private String localImagePath;

    @ElementCollection
    @CollectionTable(
            name = "pokemon_card_types",
            joinColumns = @JoinColumn(name = "pokemon_card_id")
    )
    @Column(name = "type")
    private List<String> types = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "pokemon_card_subtypes",
            joinColumns = @JoinColumn(name = "pokemon_card_id")
    )
    @Column(name = "subtype")
    private List<String> subtypes = new ArrayList<>();

    public PokemonCard() {

    }

    public PokemonCard(String externalApiId, String name, String supertype, String number, String rarity, String setName) {
        this.externalApiId = externalApiId;
        this.name = name;
        this.supertype = supertype;
        this.number = number;
        this.rarity = rarity;
        this.setName = setName;
    }

    public String getExternalApiId() {
        return externalApiId;
    }

    public void setExternalApiId(String externalApiId) {
        this.externalApiId = externalApiId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Long getId() {
        return id;
    }

    public String getLocalImagePath() {
        return localImagePath;
    }

    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public List<String> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(List<String> subtypes) {
        this.subtypes = subtypes;
    }

    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }
}
