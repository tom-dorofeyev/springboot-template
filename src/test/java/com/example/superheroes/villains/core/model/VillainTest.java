package com.example.superheroes.villains.core.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VillainTest {

    private static final String VALID_NAME = "Loki";
    private static final String VALID_VILLAIN_NAME = "God of Mischief";
    private static final List<String> VALID_POWERS = List.of("Illusions", "Shapeshifting");
    private static final int VALID_DANGER_LEVEL = 8;

    @Nested
    class Create {

        @Test
        void setsAllFieldsCorrectly() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);

            assertNotNull(villain.getId());
            assertEquals(VALID_NAME, villain.getName());
            assertEquals(VALID_VILLAIN_NAME, villain.getVillainName());
            assertEquals(VALID_POWERS, villain.getPowers());
            assertEquals(VALID_DANGER_LEVEL, villain.getDangerLevel());
            assertNotNull(villain.getCreatedAt());
            assertNotNull(villain.getUpdatedAt());
        }

        @Test
        void generatesUniqueIdEachTime() {
            Villain first = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);
            Villain second = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);

            assertNotEquals(first.getId(), second.getId());
        }

        @Test
        void setsCreatedAtAndUpdatedAtToSameTime() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);

            assertEquals(villain.getCreatedAt(), villain.getUpdatedAt());
        }

        @Test
        void throwsWhenNameIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(null, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL));

            assertTrue(exception.getMessage().contains("name"));
        }

        @Test
        void throwsWhenNameIsBlank() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create("   ", VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL));

            assertTrue(exception.getMessage().contains("name"));
        }

        @Test
        void throwsWhenNameExceeds100Characters() {
            String longName = "A".repeat(101);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(longName, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL));

            assertTrue(exception.getMessage().contains("name"));
        }

        @Test
        void throwsWhenVillainNameIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(VALID_NAME, null, VALID_POWERS, VALID_DANGER_LEVEL));

            assertTrue(exception.getMessage().contains("villainName"));
        }

        @Test
        void throwsWhenVillainNameIsBlank() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(VALID_NAME, "   ", VALID_POWERS, VALID_DANGER_LEVEL));

            assertTrue(exception.getMessage().contains("villainName"));
        }

        @Test
        void throwsWhenVillainNameExceeds100Characters() {
            String longVillainName = "B".repeat(101);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(VALID_NAME, longVillainName, VALID_POWERS, VALID_DANGER_LEVEL));

            assertTrue(exception.getMessage().contains("villainName"));
        }

        @Test
        void throwsWhenPowersIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(VALID_NAME, VALID_VILLAIN_NAME, null, VALID_DANGER_LEVEL));

            assertTrue(exception.getMessage().contains("powers"));
        }

        @Test
        void throwsWhenPowersIsEmpty() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(VALID_NAME, VALID_VILLAIN_NAME, List.of(), VALID_DANGER_LEVEL));

            assertTrue(exception.getMessage().contains("powers"));
        }

        @Test
        void throwsWhenPowersContainsBlankEntry() {
            List<String> powersWithBlank = List.of("Illusions", "   ");

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(VALID_NAME, VALID_VILLAIN_NAME, powersWithBlank, VALID_DANGER_LEVEL));

            assertTrue(exception.getMessage().contains("power"));
        }

        @Test
        void throwsWhenPowersContainsNullEntry() {
            List<String> powersWithNull = new ArrayList<>();
            powersWithNull.add("Illusions");
            powersWithNull.add(null);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(VALID_NAME, VALID_VILLAIN_NAME, powersWithNull, VALID_DANGER_LEVEL));

            assertTrue(exception.getMessage().contains("power"));
        }

        @Test
        void throwsWhenDangerLevelBelowMinimum() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, 0));

            assertTrue(exception.getMessage().contains("dangerLevel"));
        }

        @Test
        void throwsWhenDangerLevelAboveMaximum() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, 11));

            assertTrue(exception.getMessage().contains("dangerLevel"));
        }

        @Test
        void acceptsDangerLevelAtMinimumBoundary() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, 1);

            assertEquals(1, villain.getDangerLevel());
        }

        @Test
        void acceptsDangerLevelAtMaximumBoundary() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, 10);

            assertEquals(10, villain.getDangerLevel());
        }
    }

    @Nested
    class Reconstitute {

        @Test
        void setsAllFieldsCorrectly() {
            UUID id = UUID.randomUUID();
            Instant createdAt = Instant.now().minusSeconds(3600);
            Instant updatedAt = Instant.now();

            Villain villain = Villain.reconstitute(id, VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS,
                    VALID_DANGER_LEVEL, createdAt, updatedAt);

            assertEquals(id, villain.getId());
            assertEquals(VALID_NAME, villain.getName());
            assertEquals(VALID_VILLAIN_NAME, villain.getVillainName());
            assertEquals(VALID_POWERS, villain.getPowers());
            assertEquals(VALID_DANGER_LEVEL, villain.getDangerLevel());
            assertEquals(createdAt, villain.getCreatedAt());
            assertEquals(updatedAt, villain.getUpdatedAt());
        }

        @Test
        void throwsWhenIdIsNull() {
            Instant now = Instant.now();

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> Villain.reconstitute(null, VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS,
                            VALID_DANGER_LEVEL, now, now));

            assertTrue(exception.getMessage().contains("id"));
        }

        @Test
        void throwsWhenCreatedAtIsNull() {
            UUID id = UUID.randomUUID();
            Instant now = Instant.now();

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> Villain.reconstitute(id, VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS,
                            VALID_DANGER_LEVEL, null, now));

            assertTrue(exception.getMessage().contains("createdAt"));
        }

        @Test
        void throwsWhenUpdatedAtIsNull() {
            UUID id = UUID.randomUUID();
            Instant now = Instant.now();

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> Villain.reconstitute(id, VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS,
                            VALID_DANGER_LEVEL, now, null));

            assertTrue(exception.getMessage().contains("updatedAt"));
        }
    }

    @Nested
    class Update {

        @Test
        void updatesAllMutableFieldsAndTimestamp() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);
            Instant originalUpdatedAt = villain.getUpdatedAt();
            String newName = "Thanos";
            String newVillainName = "The Mad Titan";
            List<String> newPowers = List.of("Super strength", "Infinity Gauntlet");
            int newDangerLevel = 10;

            villain.update(newName, newVillainName, newPowers, newDangerLevel);

            assertEquals(newName, villain.getName());
            assertEquals(newVillainName, villain.getVillainName());
            assertEquals(newPowers, villain.getPowers());
            assertEquals(newDangerLevel, villain.getDangerLevel());
            assertTrue(villain.getUpdatedAt().isAfter(originalUpdatedAt));
        }

        @Test
        void doesNotChangeIdAndCreatedAt() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);
            UUID originalId = villain.getId();
            Instant originalCreatedAt = villain.getCreatedAt();

            villain.update("Thanos", "The Mad Titan", List.of("Infinity Gauntlet"), 10);

            assertEquals(originalId, villain.getId());
            assertEquals(originalCreatedAt, villain.getCreatedAt());
        }

        @Test
        void throwsWithInvalidInput() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> villain.update("", VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL));
        }

        @Test
        void updatingDoesNotMutateOriginalPowersArgument() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, new ArrayList<>(VALID_POWERS), VALID_DANGER_LEVEL);
            List<String> newPowers = new ArrayList<>(List.of("Flight", "Super strength"));

            villain.update("Thanos", "The Mad Titan", newPowers, 10);

            newPowers.clear();
            assertFalse(villain.getPowers().isEmpty());
        }
    }

    @Nested
    class GetPowers {

        @Test
        void returnsUnmodifiableList() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);
            List<String> powers = villain.getPowers();

            assertThrows(UnsupportedOperationException.class, () -> powers.add("Flight"));
        }
    }

    @Nested
    class Equality {

        @Test
        void equalWhenSameId() {
            UUID sharedId = UUID.randomUUID();
            Instant now = Instant.now();
            Villain villain1 = Villain.reconstitute(sharedId, "Loki", "God of Mischief",
                    List.of("Illusions"), 8, now, now);
            Villain villain2 = Villain.reconstitute(sharedId, "Thanos", "The Mad Titan",
                    List.of("Infinity Gauntlet"), 10, now, now);

            assertEquals(villain1, villain2);
            assertEquals(villain1.hashCode(), villain2.hashCode());
        }

        @Test
        void notEqualWhenDifferentIds() {
            Villain villain1 = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);
            Villain villain2 = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);

            assertNotEquals(villain1, villain2);
        }

        @Test
        void notEqualToNull() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);

            assertNotEquals(null, villain);
        }

        @Test
        void notEqualToDifferentType() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);

            assertNotEquals("not a villain", villain);
        }

        @Test
        void equalToItself() {
            Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);

            assertEquals(villain, villain);
        }
    }
}