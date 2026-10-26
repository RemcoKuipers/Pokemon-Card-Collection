package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.client.TcgdexClient;
import nl.remcokuipers.pokemoncardcollection.dto.*;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.PokemonCardMapper;
import nl.remcokuipers.pokemoncardcollection.repository.PokemonCardRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PokemonCardService {
    private final PokemonCardRepository pokemonCardRepository;
    private final PokemonCardMapper pokemonCardMapper;
    private final TcgdexClient tcgdexClient;

    public PokemonCardService(
            PokemonCardRepository pokemonCardRepository,
            PokemonCardMapper pokemonCardMapper,
            TcgdexClient tcgdexClient
    ) {
        this.pokemonCardRepository = pokemonCardRepository;
        this.pokemonCardMapper = pokemonCardMapper;
        this.tcgdexClient = tcgdexClient;
    }

    public List<PokemonCardResponseDTO> getAllPokemonCards() {
        return pokemonCardRepository.findAll()
                .stream()
                .map(pokemonCardMapper::mapToResponseDTO)
                .toList();
    }

    public PokemonCardResponseDTO getPokemonCardById(Long id) {
        PokemonCard pokemonCard = pokemonCardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "PokemonCard with id " + id + " not found!"
                        )
                );
        return pokemonCardMapper.mapToResponseDTO(pokemonCard);
    }

    public PokemonCardResponseDTO getPokemonCardByExternalApiId(String externalApiId) {
        Optional<PokemonCard> existingCard = pokemonCardRepository.findByExternalApiId(externalApiId);
        if (existingCard.isPresent()) {
            return pokemonCardMapper.mapToResponseDTO(existingCard.get());
        }
        TcgdexCardResponseDTO dto = tcgdexClient.getCardById(externalApiId);
        PokemonCard pokemonCard = pokemonCardMapper.mapToEntity(dto);
        PokemonCard savedPokemonCard = pokemonCardRepository.save(pokemonCard);

        return pokemonCardMapper.mapToResponseDTO(savedPokemonCard);
    }

    public List<TcgdexCardSummaryResponseDTO> searchByName(String name) {
        List<TcgdexCardSummaryResponseDTO> cards = tcgdexClient.searchByName(name);
        return cards;
    }

    private BigDecimal calculateDifferenceFromAvg30(BigDecimal trend, BigDecimal avg30){
        if (trend == null || avg30 == null){
            return null;
        } else return trend.subtract(avg30);
    }

    public PokemonCardPriceResponseDTO getPokemonCardPriceByExternalApiId(String externalApiId) {
        TcgdexCardResponseDTO card = tcgdexClient.getCardById(externalApiId);
        if (card.pricing() == null || card.pricing().cardmarket() == null){
            throw new ResourceNotFoundException("Price for PokemonCard with id " + externalApiId + " not found!");
        }
        TcgdexCardmarketResponseDTO price = card.pricing().cardmarket();
        BigDecimal normalDifference = calculateDifferenceFromAvg30(price.trend(), price.avg30());
        BigDecimal holoDifference = calculateDifferenceFromAvg30(price.trendHolo(), price.avg30Holo());

        return new PokemonCardPriceResponseDTO(externalApiId, price, normalDifference, holoDifference);
    }

    public PokemonCardResponseDTO updatePokemonCard(Long id, PokemonCardUpdateDTO dto){
        Optional<PokemonCard> pokemonCard = pokemonCardRepository.findById(id);
        if (pokemonCard.isPresent()) {
            PokemonCard foundCard = pokemonCard.get();
            foundCard.setName(dto.name());
            foundCard.setSupertype(dto.supertype());
            foundCard.setNumber(dto.number());
            foundCard.setRarity(dto.rarity());
            foundCard.setSetName(dto.setName());
            foundCard.setTypes(dto.types());
            foundCard.setSubtypes(dto.subtypes());
            foundCard = pokemonCardRepository.save(foundCard);
            return pokemonCardMapper.mapToResponseDTO(foundCard);
        } throw new ResourceNotFoundException("PokemonCard with id " + id + " not found!");

    }
}
