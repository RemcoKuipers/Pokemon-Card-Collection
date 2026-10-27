INSERT INTO pokemon_cards (external_api_id, name, supertype, number, rarity, set_name)
VALUES ('test-pikachu-001',
        'Pikachu',
        'Pokémon',
        '1',
        'Common',
        'Testset'
        )
ON CONFLICT DO NOTHING;

INSERT INTO users (username, email, keycloak_id, enabled)
VALUES ('testuser',
        'testuser@example.com',
        'fc91f345-48a3-45f6-bafa-f32188145337',
        true)
    ON CONFLICT DO NOTHING;

INSERT INTO users (username, email, keycloak_id, enabled)
VALUES ('test2',
        'test2@example.com',
        'e345da06-d697-493c-9a6d-0d31bf94aba3',
        true)
    ON CONFLICT DO NOTHING;
