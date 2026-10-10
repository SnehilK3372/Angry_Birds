package io.github.some_example_name;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;


public class Packer {
    public static void main(String[] args) {
        TexturePacker.process("C:\\Users\\viren\\OneDrive\\Desktop\\AngryBirds\\assets\\Blocks", "C:\\Users\\viren\\OneDrive\\Desktop\\AngryBirds\\assets\\Blocks\\atlases", "spritesheet");
        TexturePacker.process("C:\\Users\\viren\\OneDrive\\Desktop\\AngryBirds\\assets\\Pigs\\chef", "C:\\Users\\viren\\OneDrive\\Desktop\\AngryBirds\\assets\\Pigs", "chef");
        TexturePacker.process("C:\\Users\\viren\\OneDrive\\Desktop\\AngryBirds\\assets\\Pigs\\king", "C:\\Users\\viren\\OneDrive\\Desktop\\AngryBirds\\assets\\Pigs", "king");
        TexturePacker.process("C:\\Users\\viren\\OneDrive\\Desktop\\AngryBirds\\assets\\Pigs\\space", "C:\\Users\\viren\\OneDrive\\Desktop\\AngryBirds\\assets\\Pigs", "space");

    }
}
