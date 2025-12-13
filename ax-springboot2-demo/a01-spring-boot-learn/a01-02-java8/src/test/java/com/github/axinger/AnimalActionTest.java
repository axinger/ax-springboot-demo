package com.github.axinger;

import com.github.axinger.impl.AnimalActionImpl;
import com.github.axinger.impl.DogActionImpl;
import org.junit.jupiter.api.Test;

class AnimalActionTest {

    @Test
    void test1() {
        Dog dog = new Dog();

        AnimalAction<Dog> animalAction = new AnimalActionImpl<>();
        final Dog dog1 = animalAction.show(dog);
        System.out.println("dog1 = " + dog1);

        AnimalAction<Dog> animalAction2 = new DogActionImpl();
        final Dog dog2 = animalAction2.show2();
        System.out.println("dog2 = " + dog2);
    }

    @Test
    void test2() {
        Cat cat = new Cat();
        cat.setName("猫");
        AnimalAction<Cat> animalAction = new AnimalActionImpl<Cat>();
        cat = animalAction.show(cat);
        System.out.println("cat = " + cat.getName());

        final Cat cat1 = animalAction.show1(Cat.class);
        System.out.println("cat1 = " + cat1);
        cat1.setName("c1");
        System.out.println("cat1 = " + cat1);

        final Cat cat2 = animalAction.show2();
        System.out.println("cat2 = " + cat2);


    }


}
