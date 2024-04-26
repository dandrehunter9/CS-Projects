package tests;

import animals.AnimalType;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Snake;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SnakeTests {

    private static Snake actualSnake;

    @BeforeAll
    public static void createAnimals()
    {
        actualSnake = new Snake(AnimalType.DOMESTIC, Skin.FUR, Gender.UNKNOWN, Breed.UNKNOWN);
    }


    @Test
    @Order(1)
    @DisplayName("Animal Test Type Tests Domestic")
    public void animalTypeTests()
    {
        assertEquals(AnimalType.DOMESTIC, actualSnake.getAnimalType(), "Animal Type Expected[" + AnimalType.DOMESTIC
                + "] Actual[" + actualSnake.getAnimalType() + "]");
    }

    @Test
    @Order(1)
    @DisplayName("Snake Speak Slither Tests")
    public void snakeGoesTest()
    {
        assertEquals("The snake goes slither! slither!", actualSnake.speak(), "I was expecting slitter");
    }

    @Test
    @Order(1)
    @DisplayName("Snake scales is it Hyperallergetic")
    public void snakeHyperallergeticTests()
    {
        assertEquals("The snake is not hyperallergetic!", actualSnake.snakeHypoallergenic(),
                "The snake is not hyperallergetic!");
    }

    @Test
    @Order(1)
    @DisplayName("Snake has legs value Test")
    public void legTests()
    {
        Assertions.assertNotNull(actualSnake.getNumberOfLegs());
    }

    @Test
    @Order(2)
    @DisplayName("Snake Gender Test FeMale")
    public void genderTestFeMale()
    {
        actualSnake = new Snake(AnimalType.WILD, Skin.UNKNOWN,Gender.FEMALE, Breed.UNKNOWN);
        assertEquals(Gender.FEMALE, actualSnake.getGender(), "Expecting Male Gender!");
    }

    @Test
    @Order(2)
    @DisplayName("Snake Breed Test COPPERHEAD")
    public void genderSnakeBreed()
    {
        actualSnake = new Snake(AnimalType.WILD, Skin.UNKNOWN,Gender.FEMALE, Breed.COPPERHEAD);
        assertEquals(Breed.COPPERHEAD, actualSnake.getBreed(), "Expecting Breed copperhead!");
    }

    @Test
    @Order(2)
    @DisplayName("Snake Speak Hiss Tests")
    public void snakeGoesHissTest()
    {
        actualSnake = new Snake(AnimalType.WILD, Skin.UNKNOWN,Gender.UNKNOWN, Breed.UNKNOWN);
        assertEquals("The snake goes hiss! hiss!", actualSnake.speak(), "I was expecting hiss");
    }

    @Test
    @Order(2)
    @DisplayName("Snake Speak Psss Tests")
    public void snakeGoesPsssTest()
    {
        actualSnake = new Snake(AnimalType.UNKNOWN, Skin.UNKNOWN,Gender.UNKNOWN, Breed.UNKNOWN);
        assertEquals("The snake goes Psss! Psss!", actualSnake.speak(), "I was expecting psss");
    }
    @Test
    @Order(2)
    @DisplayName("Snake Legs Test")
    public void testSetNumberOfLegs() {
        actualSnake.setNumberOfLegs(1);
        assertEquals(1, actualSnake.getNumberOfLegs(),"expect 1 leg");
    }
}

