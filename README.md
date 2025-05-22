# Final Project: Solo Adventure Maze

## Time Allotment for the Project: 4 weeks (completed in pairs)

### Overview:
Develop a text-based solo adventure game where a hero navigates through a maze of interconnected rooms. Each room is stored as a CSV file (rooms can be of any size) and loaded into a 2D grid. The hero explores the maze, battles monsters, collects weapons and healing potions, and must ultimately obtain a key (dropped by a specific monster) to unlock the door and escape.

### System Overview:

#### Rooms:
- Each room is defined in a CSV (comma separated values) file with a variable number of rows and columns.
  - The first line of the file indicates the number of rows and columns that define the room
- The room is displayed with surrounding walls (using ASCII characters) for better visualization.
- Each cell in the grid represents (character inside the parenthesis represents the character that will appear in the files):
  - Blank space ( ): where the hero can move.
  - The hero: represented by a symbol (@).
  - Weapons: three types are available:
    - Stick(S): (damage = 1)  
    - Weak Sword(W): (damage = 2)  
    - Strong Sword(X): (damage = 3)  
  - Monsters: three types are available with specified hit points (HP) and damage:
    - Goblin(G:HP): HP = 3, Damage = 1  
    - Orc(O:HP): HP = 8, Damage = 3  
    - Troll(T:HP): HP = 15, Damage = 4  
    - Monsters remain static. Their health is updated and displayed in the action menu (when the hero is adjacent) so the player can see the monster's remaining HP before deciding to attack. A monster is only removed when its HP is fully depleted.
  - Healing Potions: two types are available:
    - Minor Flask(m): restores +6 HP  
    - Big Flask(B): restores +12 HP  
  - Doors(D): each door cell contains a file address (the next room's CSV file) and is displayed with a special symbol (D).
  - Key(*): not initially present; a specific monster drops the key when defeated.

#### Hero:
- The hero (@) starts with a base HP of 25.
- The hero can move throughout the room using directional commands (u=up, d=down, r=right, l=left).
- Hero positioning rules at the start of the game:
  - If the room1.csv file contains the @ symbol, the hero is placed at that position.
  - If no @ is found in the file, the hero is placed at position (1,1).
  - If position (1,1) is occupied by another object (e.g., item, monster), the hero is placed randomly in any empty space in the room.
- The hero can pick up weapons and healing potions.
  - When the hero moves to a cell with a healing potion, it will automatically consume it.  
  - If the hero is fully healed, the potion is left in the room after moving out from the cell.
  - When the hero moves to a cell with a weapon it will equip the weapon.
  - When encountering a new weapon while already armed, a prompt allows the hero to decide whether to switch weapons. If the hero switches, the old weapon is left in the room; otherwise, the new weapon remains.
- Attack Mechanics:
  - The hero may only attack if armed.
  - When the hero is adjacent to a monster, an action menu is displayed. This menu shows the monster’s current HP along with available actions (attack/don’t attack).
  - If the hero attacks, the monster’s HP is reduced by the weapon’s damage, and simultaneously, the hero suffers damage equal to the monster's attack value.
  - A monster is defeated and removed from the room only when its HP reaches zero.
  - One designated monster will drop the key upon defeat. Only Trolls can drop the key.

### File I/O and Exception Handling:
- When the game starts, a copy of the original room files is made in subfolder that represents one journey of the game. Every run will be saved in a different folder.
- After that, room data is loaded from the copied CSV files.
- Updated room state is saved back to the copied CSV file upon exit.
  - When the hero exits a room (via a door), the current state (item pickups, monsters HP or defeated monsters, and weapon swaps) is saved back to the corresponding CSV file.
- Robust exception handling must be implemented to manage file errors and invalid inputs.

### User Interface:
- Each room is printed with walls drawn using ASCII characters (e.g., +, -, |).
- The hero’s stats (HP, current weapon, key possession) are displayed at the top of the room.
- Unicode characters might be used for visual distinction (not required). For example:
  - Hero: ☺ (Unicode U+263A).
  - Strong Sword: ⚔ (Unicode U+2694), etc.

### Project Requirements:

#### 1. File Handling and Room Management:
- Load room data from CSV files into a 2D array.
- Save the room’s state back to its file upon exit, including any changes (e.g., defeated monsters, item pickups, weapon swaps).

#### 2. Navigation and Game Mechanics:
- Allow the hero to move within the room grid using commands (e.g., u/d/r/l).
- Display the room with walls and the hero’s current stats at the top.
- Item Interactions:
  - Weapons:
    - The hero picks up a weapon if unarmed.
    - If already armed, a prompt allows the hero to choose whether to switch weapons. If switched, the old weapon is left behind.
  - Healing Potions:
    - Restore HP accordingly when picked up.
    - If hero is fully healed, the item has no effect.
- Combat:
  - When a monster is adjacent, display an action menu that shows the monster's current HP and available actions (attack, ignore, etc.).
  - If attacking, reduce the monster’s HP by the weapon’s damage and reduce the hero’s HP by the monster’s attack value.
  - Remove monsters when their HP reaches zero.
  - A designated monster will drop a key upon defeat.
- Door and Key Mechanics:
  - A single specific door can be unlocked only if the hero has obtained the key.
  - Upon stepping on a door cell, if the hero has the key, the room state is saved and the next room (specified by the door’s file address) is loaded.

#### 3. OOP Design and Technical Requirements:
- Use classes and objects to represent the hero, rooms, weapons, monsters, healing potions, and doors.
  - Include a class diagram
- Organize your project using a clear, modular OOP file structure (multiple class files).
- Employ inheritance and interfaces where applicable.
- Use ArrayLists to manage dynamic objects.
- Use loops and conditionals for game logic.
- Implement robust exception handling for file I/O and user input.
- The program must produce clear, well-formatted console output.
- Code is well-commented, follows Java naming conventions, and includes robust exception handling.

#### 4. Additional Gameplay:
- The hero’s stats (HP, current weapon, key status) must be displayed at the top of the room display each time the room is printed.
- The action menu for combat must show the monster’s current HP to help the hero decide whether to attack.

### Project Deliverables:
- Code: Fully functional Java code with a modular OOP file structure.
- Design Artifacts: Class diagrams and a written explanation of your design choices.
- Live Demonstration: The project will be evaluated live by the instructor and TA; be prepared to explain your implementation.

### Assessment details. (15 points)

| Category                         | Functionality Description                                                                                                                                     | Points |
|----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|--------|
| **File Handling and Room Management** | - Load room data from CSV files into a 2D array<br>- Save the room’s state back to its file                                                       | 20     |
| **Navigation and Game Mechanics**     | - Hero movement within the grid (n/s/e/w) (5 pts)<br>- Display room with walls and hero's stats (5 pts)<br>- Weapons pickup/switch (8 pts)<br>- Healing potions (4 pts)<br>- Combat mechanics (10 pts)<br>- Door and key mechanics (3 pts) | 35     |
| **OOP Design and Technical Requirements** | - Class design and modular file structure (8 pts)<br>- Use of inheritance/interfaces (5 pts)<br>- Use of ArrayLists (5 pts)<br>- Implementation of loops and conditionals (4 pts)<br>- Exception handling (4 pts)<br>- Code quality and console output (4 pts) | 30     |
| **Additional Gameplay**               | - Display hero’s stats at the top (5 pts)<br>- Combat action menu showing monster HP (5 pts)<br>- Overall integration of gameplay features (5 pts)        | 15     |
| **Total**                            |                                                                                                                                                              | **100** |
