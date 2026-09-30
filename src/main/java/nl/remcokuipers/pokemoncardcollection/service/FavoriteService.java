package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.dto.FavoriteRequestDTO;
import nl.remcokuipers.pokemoncardcollection.dto.FavoriteResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.Favorite;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.exception.DuplicateResourceException;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.FavoriteMapper;
import nl.remcokuipers.pokemoncardcollection.repository.FavoriteRepository;
import nl.remcokuipers.pokemoncardcollection.repository.PokemonCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private final PokemonCardRepository pokemonCardRepository;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;

    public FavoriteService(
            PokemonCardRepository pokemonCardRepository,
            FavoriteRepository favoriteRepository,
            FavoriteMapper favoriteMapper
    ) {
        this.pokemonCardRepository = pokemonCardRepository;
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
    }

    public FavoriteResponseDTO addFavorite(FavoriteRequestDTO dto, User user) {

        PokemonCard pokemonCard = pokemonCardRepository.findById(dto.pokemonCardId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pokemon Card Not Found with ID: " + dto.pokemonCardId()
                        )
                );

        if (favoriteRepository.existsByUserAndPokemonCard(user, pokemonCard)) {
            throw new DuplicateResourceException(
                    "Pokemon Card with ID: " + dto.pokemonCardId() + " is already a favorite."
            );
        }

        Favorite favorite =
                favoriteMapper.mapToEntity(user, pokemonCard);

        Favorite savedFavorite = favoriteRepository.save(favorite);

        return favoriteMapper.mapToResponseDTO(savedFavorite);
    }

    public List<FavoriteResponseDTO> getFavoritesByUser(User user) {
       List<Favorite> favorites = favoriteRepository.findByUser(user);

       return favorites
               .stream()
               .map(favoriteMapper::mapToResponseDTO)
               .toList();
    }

    public void deleteFavorite(Long id, User user) {
        Favorite favorite =
                favoriteRepository.findByIdAndUser(id, user)
                        .orElseThrow(() -> new ResourceNotFoundException("Favorite Not Found with ID: " + id));
        favoriteRepository.delete(favorite);

    }
}
