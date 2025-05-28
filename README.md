# Design Explanation

**Project Title**: Solo Adventure Maze  
**Course**: ITM 413 – Computer Language  
**Team Members**: 23102020 Lee Sodam, 23102025 Lee Haneol

---

## 1. Overview of Object-Oriented Design

This project implements a solo, text-based maze game using Java, designed with strong object-oriented principles. Each interactive game component—such as the hero, monsters, weapons, potions, doors, and rooms—is modeled as a separate class. Common behaviors are abstracted via inheritance and interfaces. The game uses `.csv` files to store room layouts, making the game content easily editable and data-driven. A central game loop accepts user commands, supports combat, room transitions, and updates the game state dynamically.

---

## 2. Class Responsibilities

- **Hero.java**  
  Represents the player. Stores HP, position, equipped weapon, and key possession status.  
  - Supports movement (`move()`), combat (`attack()`), potion healing, and weapon swapping.  
  - On entering a Door tile, checks for key possession and transitions rooms.

- **Monster.java**  
  Represents enemies (Goblin, Orc, Troll). Stores HP and damage. Trolls drop a Key upon defeat.  
  Blocks movement and can be attacked when adjacent.

- **Weapon.java / Potion.java / Key.java**  
  - **Weapon**: Enhances damage dealt. Player chooses to switch if already armed.  
  - **Potion**: Heals hero automatically if HP is below max.  
  - **Key**: Required to exit through the master door. Dropped by the Troll.

- **Door.java**  
  Contains the target room filename and handles room transitions. Checks key possession if the door is a master door (`D`).

- **Room.java**  
  Core environment manager.  
  - Loads room layout from a `.csv` into a 2D `Cell[][]` grid.  
  - Tracks monsters using an `ArrayList<Monster>`.  
  - Saves state changes (items, monsters, hero position) when exiting.

- **Cell.java**  
  Represents a tile in the room. Stores a reference to a `GameObject`.  
  - Methods: `isEmpty()`, `getSymbol()`, `setObject()`.

- **GameObject.java (abstract)**  
  Parent class for all interactable objects.  
  - Defines `getSymbol()` to allow polymorphic rendering in rooms.

- **GameObjectFactory.java**  
  Translates characters from CSV files into instantiated `GameObject` subclasses.  
  - Promotes clean separation of parsing logic from object creation.

- **Game.java**  
  Manages the game loop.  
  - Accepts commands: move (`u/d/l/r`), attack (`a`), quit (`q`).  
  - Delegates actions to the hero and controls state transitions.

- **CSVUtils.java**  
  Utility class for reading and writing `.csv` files using Java I/O.  
  - Ensures consistent layout format and robust error handling.

---

## 3. Inheritance and Polymorphism

All interactable game objects inherit from `GameObject`, enabling consistent storage in `Cell` objects and polymorphic interactions (e.g., rendering symbols, picking up items). This structure simplifies handling various object types in a unified way.

---

## 4. Dynamic Object Management with ArrayList

The `Room` class uses `ArrayList<Monster>` to track active enemies.  
- Defeated monsters are removed from both the grid and the list.  
- This enables efficient iteration and dynamic room updates without scanning the entire grid.

---

## 5. Robust Exception Handling

File operations for loading/saving `.csv` files are wrapped in try-catch blocks.  
- This prevents crashes due to missing or malformed files.  
- Invalid user input is handled gracefully with helpful messages in the console.

---

## 6. Design Highlights and Justification

- **Maintainability**: Each class handles a specific responsibility with clean boundaries.  
- **Extensibility**: New items or monsters can be added easily by extending `GameObject`.  
- **Data-Driven**: Room layout and content are defined externally in `.csv` files.  
- **User Feedback**: Terminal clearly shows the map, monster HP, hero stats, and prompts.  
- **Rich Interaction**: Players can pick up items, fight monsters, and transition between rooms dynamically.
