package nl.remcokuipers.pokemoncardcollection.controller;

import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryRequestDTO;
import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.enums.Condition;
import nl.remcokuipers.pokemoncardcollection.repository.CollectionEntryRepository;
import nl.remcokuipers.pokemoncardcollection.repository.PokemonCardRepository;
import nl.remcokuipers.pokemoncardcollection.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest()
public class CollectionEntryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PokemonCardRepository pokemonCardRepository;

    @Autowired
    private CollectionEntryRepository collectionEntryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private PokemonCard pokemonCard;

    @BeforeEach
    public void setup() {
        collectionEntryRepository.deleteAll();
        pokemonCardRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setUsername("test");
        user.setEmail("test@integrationtest.nl");
        user.setKeycloakId("test-integration-user");
        user = userRepository.save(user);

        pokemonCard = new PokemonCard();
        pokemonCard.setExternalApiId("test-pikachu-01");
        pokemonCard.setName("Pikachu");
        pokemonCard.setSupertype("Pokemon");
        pokemonCard.setNumber("1");
        pokemonCard.setRarity("common");
        pokemonCard.setSetName("Testset");
        pokemonCard = pokemonCardRepository.save(pokemonCard);

        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject(user.getKeycloakId())
                .claim("preferred_username", user.getUsername())
                .claim("email", user.getEmail())
                .build();
        SecurityContextHolder.getContext()
                .setAuthentication(new JwtAuthenticationToken(jwt));
    }

    @AfterEach
    public void teardown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCollectionEntryById_metOnbekendeId_geeft404() throws Exception {
        //Act & Assert
        var response = mockMvc.perform(get("/collectionentries/{id}", 9999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.message").value("Collection entry with ID 9999 not found."));
    }

    @Test
    void addCollectionEntry_metGeldigeGegevens_slaatEntryOp() throws Exception {
        //Arrange
        CollectionEntryRequestDTO collectionEntryRequestDTO = new CollectionEntryRequestDTO(
                pokemonCard.getId(),
                2,
                Condition.MINT
        );

        //Act & Assert
        var response = mockMvc.perform(post("/collectionentries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(collectionEntryRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pokemonCardId").value(pokemonCard.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.condition").value("MINT"));

        List<CollectionEntry> entries = collectionEntryRepository.findByUser(user);
        assertEquals(1, entries.size());

        CollectionEntry savedEntry = entries.get(0);
        assertEquals(2, savedEntry.getQuantity());
        assertEquals(Condition.MINT, savedEntry.getCondition());
        assertEquals(pokemonCard.getId(), savedEntry.getPokemonCard().getId());


    }

}

