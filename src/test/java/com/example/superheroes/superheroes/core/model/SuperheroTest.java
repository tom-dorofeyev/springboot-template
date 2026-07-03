package com.example.superheroes.superheroes.core.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SuperheroTest {

    private static final String VALID_NAME = "Spider-Man";
    private static final String VALID_ALIAS = "Peter Parker";
    private static final List<String> VALID_POWERS = List.of("Web-slinging", "Spider-sense");
    private static final int VALID_STRENGTH = 7;

    @Nested
    class Create {

        @Test
        void setsAllFieldsCorrectly() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

            assertNotNull(hero.getId());
            assertEquals(VALID_NAME, hero.getName());
            assertEquals(VALID_ALIAS, hero.getAlias());
            assertEquals(VALID_POWERS, hero.getPowers());
            assertEquals(VALID_STRENGTH, hero.getStrengthLevel());
            assertNotNull(hero.getCreatedAt());
            assertNotNull(hero.getUpdatedAt());
        }

        @Test
        void generatesUniqueIdEachTime() {
            Superhero first = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);
            Superhero second = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

            assertNotEquals(first.getId(), second.getId());
        }

        @Test
        void setsCreatedAtAndUpdatedAtToSameTime() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

            assertEquals(hero.getCreatedAt(), hero.getUpdatedAt());
        }

        @Test
        void throwsWhenNameIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(null, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH));

            assertTrue(exception.getMessage().contains("name"));
        }

        @Test
        void throwsWhenNameIsBlank() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create("   ", VALID_ALIAS, VALID_POWERS, VALID_STRENGTH));

            assertTrue(exception.getMessage().contains("name"));
        }

        @Test
        void throwsWhenNameExceeds100Characters() {
            String longName = "A".repeat(101);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(longName, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH));

            assertTrue(exception.getMessage().contains("name"));
        }

        @Test
        void throwsWhenAliasIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(VALID_NAME, null, VALID_POWERS, VALID_STRENGTH));

            assertTrue(exception.getMessage().contains("alias"));
        }

        @Test
        void throwsWhenAliasIsBlank() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(VALID_NAME, "   ", VALID_POWERS, VALID_STRENGTH));

            assertTrue(exception.getMessage().contains("alias"));
        }

        @Test
        void throwsWhenAliasExceeds100Characters() {
            String longAlias = "B".repeat(101);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(VALID_NAME, longAlias, VALID_POWERS, VALID_STRENGTH));

            assertTrue(exception.getMessage().contains("alias"));
        }

        @Test
        void throwsWhenPowersIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(VALID_NAME, VALID_ALIAS, null, VALID_STRENGTH));

            assertTrue(exception.getMessage().contains("powers"));
        }

        @Test
        void throwsWhenPowersIsEmpty() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(VALID_NAME, VALID_ALIAS, List.of(), VALID_STRENGTH));

            assertTrue(exception.getMessage().contains("powers"));
        }

        @Test
        void throwsWhenPowersContainsBlankEntry() {
            List<String> powersWithBlank = List.of("Web-slinging", "   ");

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(VALID_NAME, VALID_ALIAS, powersWithBlank, VALID_STRENGTH));

            assertTrue(exception.getMessage().contains("power"));
        }

        @Test
        void throwsWhenPowersContainsNullEntry() {
            List<String> powersWithNull = new ArrayList<>();
            powersWithNull.add("Web-slinging");
            powersWithNull.add(null);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(VALID_NAME, VALID_ALIAS, powersWithNull, VALID_STRENGTH));

            assertTrue(exception.getMessage().contains("power"));
        }

        @Test
        void throwsWhenStrengthLevelBelowMinimum() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, 0));

            assertTrue(exception.getMessage().contains("strengthLevel"));
        }

        @Test
        void throwsWhenStrengthLevelAboveMaximum() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, 11));

            assertTrue(exception.getMessage().contains("strengthLevel"));
        }

        @Test
        void acceptsStrengthLevelAtMinimumBoundary() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, 1);

            assertEquals(1, hero.getStrengthLevel());
        }

        @Test
        void acceptsStrengthLevelAtMaximumBoundary() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, 10);

            assertEquals(10, hero.getStrengthLevel());
        }

        @Test
        void acceptsNameAtMaxLength() {
            String maxName = "A".repeat(100);

            Superhero hero = Superhero.create(maxName, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

            assertEquals(maxName, hero.getName());
        }

        @Test
        void acceptsAliasAtMaxLength() {
            String maxAlias = "B".repeat(100);

            Superhero hero = Superhero.create(VALID_NAME, maxAlias, VALID_POWERS, VALID_STRENGTH);

            assertEquals(maxAlias, hero.getAlias());
        }
    }

    @Nested
    class Reconstitute {

        @Test
        void setsAllFieldsCorrectly() {
            UUID id = UUID.randomUUID();
            Instant createdAt = Instant.now().minusSeconds(3600);
            Instant updatedAt = Instant.now();

            Superhero hero = Superhero.reconstitute(id, VALID_NAME, VALID_ALIAS, VALID_POWERS,
                    VALID_STRENGTH, createdAt, updatedAt);

            assertEquals(id, hero.getId());
            assertEquals(VALID_NAME, hero.getName());
            assertEquals(VALID_ALIAS, hero.getAlias());
            assertEquals(VALID_POWERS, hero.getPowers());
            assertEquals(VALID_STRENGTH, hero.getStrengthLevel());
            assertEquals(createdAt, hero.getCreatedAt());
            assertEquals(updatedAt, hero.getUpdatedAt());
        }

        @Test
        void throwsWhenIdIsNull() {
            Instant now = Instant.now();

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> Superhero.reconstitute(null, VALID_NAME, VALID_ALIAS, VALID_POWERS,
                            VALID_STRENGTH, now, now));

            assertTrue(exception.getMessage().contains("id"));
        }

        @Test
        void throwsWhenCreatedAtIsNull() {
            UUID id = UUID.randomUUID();
            Instant now = Instant.now();

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> Superhero.reconstitute(id, VALID_NAME, VALID_ALIAS, VALID_POWERS,
                            VALID_STRENGTH, null, now));

            assertTrue(exception.getMessage().contains("createdAt"));
        }

        @Test
        void throwsWhenUpdatedAtIsNull() {
            UUID id = UUID.randomUUID();
            Instant now = Instant.now();

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> Superhero.reconstitute(id, VALID_NAME, VALID_ALIAS, VALID_POWERS,
                            VALID_STRENGTH, now, null));

            assertTrue(exception.getMessage().contains("updatedAt"));
        }
    }

    @Nested
    class Update {

        @Test
        void updatesAllMutableFieldsAndTimestamp() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);
            Instant originalUpdatedAt = hero.getUpdatedAt();
            String newName = "Iron Man";
            String newAlias = "Tony Stark";
            List<String> newPowers = List.of("Powered armor", "Genius intellect");
            int newStrength = 9;

            hero.update(newName, newAlias, newPowers, newStrength);

            assertEquals(newName, hero.getName());
            assertEquals(newAlias, hero.getAlias());
            assertEquals(newPowers, hero.getPowers());
            assertEquals(newStrength, hero.getStrengthLevel());
            assertTrue(hero.getUpdatedAt().isAfter(originalUpdatedAt));
        }

        @Test
        void doesNotChangeIdAndCreatedAt() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);
            UUID originalId = hero.getId();
            Instant originalCreatedAt = hero.getCreatedAt();

            hero.update("Iron Man", "Tony Stark", List.of("Powered armor"), 9);

            assertEquals(originalId, hero.getId());
            assertEquals(originalCreatedAt, hero.getCreatedAt());
        }

        @Test
        void throwsWithInvalidInput() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> hero.update("", VALID_ALIAS, VALID_POWERS, VALID_STRENGTH));
        }

        @Test
        void updatingDoesNotMutateOriginalPowersArgument() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, new ArrayList<>(VALID_POWERS), VALID_STRENGTH);
            List<String> newPowers = new ArrayList<>(List.of("Flight", "Super strength"));

            hero.update("Iron Man", "Tony Stark", newPowers, 9);

            newPowers.clear();
            assertFalse(hero.getPowers().isEmpty());
        }
    }

    @Nested
    class GetPowers {

        @Test
        void returnsUnmodifiableList() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);
            List<String> powers = hero.getPowers();

            assertThrows(UnsupportedOperationException.class, () -> powers.add("Flight"));
        }
    }

    @Nested
    class Equality {

        @Test
        void equalWhenSameId() {
            UUID sharedId = UUID.randomUUID();
            Instant now = Instant.now();
            Superhero hero1 = Superhero.reconstitute(sharedId, "Spider-Man", "Peter Parker",
                    List.of("Web-slinging"), 7, now, now);
            Superhero hero2 = Superhero.reconstitute(sharedId, "Iron Man", "Tony Stark",
                    List.of("Powered armor"), 9, now, now);

            assertEquals(hero1, hero2);
            assertEquals(hero1.hashCode(), hero2.hashCode());
        }

        @Test
        void notEqualWhenDifferentIds() {
            Superhero hero1 = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);
            Superhero hero2 = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

            assertNotEquals(hero1, hero2);
        }

        @Test
        void notEqualToNull() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

            assertNotEquals(null, hero);
        }

        @Test
        void notEqualToDifferentType() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

            assertNotEquals("not a superhero", hero);
        }

        @Test
        void equalToItself() {
            Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

            assertEquals(hero, hero);
        }
    }
}
