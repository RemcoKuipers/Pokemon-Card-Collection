INSERT INTO pokemon_cards (external_api_id, name, supertype, number, rarity, set_name)
VALUES ('test-pikachu-001',
        'Pikachu',
        'Pokémon',
        '1',
        'Common',
        'Testset'
       );

INSERT INTO users (username, email, password, enabled, keycloak_id)
VALUES ('testuser',
        'testuser@example.com',
        'placeholder',
        true,
        'fc91f345-48a3-45f6-bafa-f32188145337'
        );

INSERT INTO users (username, email, password, enabled, keycloak_id)
VALUES ('test2',
        'test2@example.com',
        'placeholder',
        true,
        'e345da06-d697-493c-9a6d-0d31bf94aba3'
        );
